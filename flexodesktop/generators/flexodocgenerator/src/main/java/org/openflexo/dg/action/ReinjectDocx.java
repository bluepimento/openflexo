/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.dg.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openflexo.docxparser.DocxFileParser;
import org.openflexo.docxparser.dto.ParsedDocx;
import org.openflexo.docxparser.dto.api.IParsedFlexoObject;
import org.openflexo.docxparser.dto.api.IParsedHtml;
import org.openflexo.docxparser.dto.api.IParsedHtmlResource;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.cg.CGObject;
import org.openflexo.foundation.cg.GenerationRepository;
import org.openflexo.foundation.cg.action.AbstractGCAction;
import org.openflexo.foundation.rm.FlexoStorageResource;
import org.openflexo.foundation.rm.StorageResourceData;
import org.openflexo.foundation.toc.TOCEntry;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.toolbox.StringUtils;

public class ReinjectDocx extends AbstractGCAction<ReinjectDocx, CGObject> {
	private static final Logger logger = Logger.getLogger(ReinjectDocx.class.getPackage().getName());

	private File docxToReinject;
	private int numberOfDescriptionUpdated;
	private int numberOfNameUpdated;
	private int numberOfTocEntryTitleUpdated;
	private int numberOfTocEntryContentUpdated;
	private int numberOfObjectNotFound;
	private StringBuilder errorReport;

	public static final FlexoActionType<ReinjectDocx, CGObject, CGObject> actionType = new FlexoActionType<ReinjectDocx, CGObject, CGObject>(
			"reinject_docx", FlexoActionType.importMenu, FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		@Override
		protected boolean isEnabledForSelection(CGObject object, Vector<CGObject> globalSelection) {
			return true;
		}

		@Override
		protected boolean isVisibleForSelection(CGObject object, Vector<CGObject> globalSelection) {
			return true;
		}

		@Override
		public ReinjectDocx makeNewAction(CGObject focusedObject, Vector<CGObject> globalSelection, FlexoEditor editor) {
			return new ReinjectDocx(focusedObject, globalSelection, editor);
		}

	};

	protected ReinjectDocx(CGObject focusedObject, Vector<CGObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	static {
		FlexoModelObject.addActionForClass(ReinjectDocx.actionType, GenerationRepository.class);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {
		numberOfDescriptionUpdated = 0;
		numberOfNameUpdated = 0;
		numberOfTocEntryTitleUpdated = 0;
		numberOfTocEntryContentUpdated = 0;
		numberOfObjectNotFound = 0;
		errorReport = new StringBuilder();

		FileInputStream in;
		try {
			in = new FileInputStream(getDocxToReinject());
		} catch (IOException e) {
			throw new IOFlexoException(e);
		}

		try {
			makeFlexoProgress(FlexoLocalization.localizedForKey("reinjecting_docx_file"), 2);
			setProgress(FlexoLocalization.localizedForKey("parsing"));

			File imagesDir = getEditor().getProject().getImportedImagesDir();
			imagesDir = new File(imagesDir, "DocxReinject");
			imagesDir.mkdirs();
			String imageRelativePath = FileUtils.makeFilePathRelativeToDir(imagesDir, getEditor().getProject().getProjectDirectory());
			DocxFileParser docxParser = new DocxFileParser(in, getAvailableCssClasses(), imageRelativePath);

			ParsedDocx parsedDocx = docxParser.getParsedDocx();
			setProgress(FlexoLocalization.localizedForKey("reinjecting_docx_file_in_model"));
			resetSecondaryProgress(parsedDocx.getAllParsedFlexoDescriptions().size() + parsedDocx.getAllParsedFlexoNames().size()
					+ parsedDocx.getAllParsedFlexoTitles().size() + parsedDocx.getAllParsedFlexoContents().size() + 1);

			// First load all unloaded resources
			for (FlexoStorageResource<? extends StorageResourceData> resource : getFocusedObject().getProject().getStorageResources()) {
				resource.getResourceData();// no need to mark as modified .setIsModified();
			}

			// Build a hash containing all flexoobject by "FlexoID_UserID"
			Map<String, FlexoModelObject> allObjects = new HashMap<String, FlexoModelObject>();
			List<FlexoModelObject> registeredObjects = new ArrayList<FlexoModelObject>();
			registeredObjects.addAll(getFocusedObject().getProject().getAllRegisteredObjects()); // Create new to avoid concurrent
																									// modification exception
			for (FlexoModelObject flexoObject : registeredObjects) {
				allObjects.put(flexoObject.getSerializationIdentifier(), flexoObject);
			}

			Map<FlexoModelObject, String[]> duplicatedFlexoNames = new HashMap<FlexoModelObject, String[]>();
			for (IParsedFlexoObject parsedFlexoObject : parsedDocx.getAllParsedFlexoObjects()) {

				FlexoModelObject flexoObject = allObjects.get(FlexoModelObject.getSerializationIdentifier(parsedFlexoObject.getUserId(),
						parsedFlexoObject.getFlexoId()));

				if (flexoObject != null) {
					if (parsedFlexoObject.getParsedFlexoDescription() != null) {
						numberOfDescriptionUpdated++;
						setSecondaryProgress(FlexoLocalization.localizedForKey("reinjecting_description_in_model_for_object") + " "
								+ flexoObject.getName());

						for (String target : parsedFlexoObject.getParsedFlexoDescription().getAllTargets()) {
							logger.info("Updating object '" + flexoObject.getSerializationIdentifier() + "' description"
									+ (target.length() > 0 ? " for target '" + target + "'" : ""));
							IParsedHtml parsedHtml = parsedFlexoObject.getParsedFlexoDescription().getHtmlDescription(target);
							if (target.length() == 0) {
								flexoObject.setDescription(parsedHtml.getHtml());
							} else {
								flexoObject.setSpecificDescriptionsForKey(parsedHtml.getHtml(), target);
								flexoObject.setHasSpecificDescriptions(true);
							}

							copyNeededResourcesFromParsedHtml(parsedHtml, imagesDir);
						}
					}

					if (parsedFlexoObject.getParsedFlexoName() != null) {
						setSecondaryProgress(FlexoLocalization.localizedForKey("reinjecting_name_in_model_for_object") + " "
								+ flexoObject.getName());

						String oldName = flexoObject.getName();
						String newName = parsedFlexoObject.getParsedFlexoName().getFlexoName();

						if (newName != null
								&& (oldName == null || !StringUtils.replaceBreakLinesBy(newName, " ").equals(
										StringUtils.replaceBreakLinesBy(oldName, " ")))) {
							try {
								logger.info("Updating object '" + flexoObject.getSerializationIdentifier() + "' name");
								flexoObject.setName(newName);
								numberOfNameUpdated++;
							} catch (Exception e) {
								// Can be a duplicate Exception -> try to add a unique suffix to allow name switch between two objects
								// This suffix will be removed at the end of the process. Either the object name will be set to the newly
								// set one if possible or it will be reset to its old name.
								// Ideally, this should be performed only on duplicateException but there is too much different possible
								// duplicate Exception type (with no common hierarchy).

								try {
									flexoObject.setName(newName + flexoObject.getUserIdentifier() + flexoObject.getFlexoID());
									duplicatedFlexoNames.put(flexoObject, new String[] { oldName, newName });
								} catch (Exception e1) {
									logger.log(Level.WARNING, "Reinject docx: cannot set name for object '" + flexoObject.getName() + " ("
											+ flexoObject.getUserIdentifier() + "_" + flexoObject.getFlexoID() + ")' to '" + newName + "'",
											e1);
									addToErrorReport("Cannot change name of object '" + oldName + "' to '" + newName + "': "
											+ (e.getMessage() != null ? e.getMessage() : e.getClass()));
								}
							}
						}
					}

					if (parsedFlexoObject.getParsedFlexoTitle() != null && flexoObject instanceof TOCEntry) {
						TOCEntry tocEntry = (TOCEntry) flexoObject;
						setSecondaryProgress(FlexoLocalization.localizedForKey("reinjecting_title_in_model_for_tocentry") + " "
								+ tocEntry.getTitle());

						String oldTitle = tocEntry.getTitle();
						String newTitle = parsedFlexoObject.getParsedFlexoTitle().getFlexoTitle();

						if (newTitle != null
								&& (oldTitle == null || !StringUtils.replaceBreakLinesBy(newTitle, " ").equals(
										StringUtils.replaceBreakLinesBy(oldTitle, " ")))) {
							logger.info("Updating object '" + flexoObject.getSerializationIdentifier() + "' title");
							tocEntry.setTitle(newTitle);
							numberOfTocEntryTitleUpdated++;
						}
					}

					if (parsedFlexoObject.getParsedFlexoContent() != null && flexoObject instanceof TOCEntry) {
						TOCEntry tocEntry = (TOCEntry) flexoObject;
						setSecondaryProgress(FlexoLocalization.localizedForKey("reinjecting_content_in_model_for_tocentry") + " "
								+ tocEntry.getTitle());

						logger.info("Updating object '" + flexoObject.getSerializationIdentifier() + "' content");
						try {
							tocEntry.setContent(parsedFlexoObject.getParsedFlexoContent().getFlexoContent().getHtml());
							numberOfTocEntryContentUpdated++;

							copyNeededResourcesFromParsedHtml(parsedFlexoObject.getParsedFlexoContent().getFlexoContent(), imagesDir);
						} catch (IllegalAccessException e) {
							logger.log(Level.WARNING, "Reinject docx: cannot set content for toc entry '" + tocEntry.getTitle() + " ("
									+ flexoObject.getUserIdentifier() + "_" + flexoObject.getFlexoID() + ")'", e);
							addToErrorReport("Cannot change content of toc entry '" + tocEntry.getTitle() + "': "
									+ (e.getMessage() != null ? e.getMessage() : e.getClass()));
						}
					}
				} else {
					setSecondaryProgress(""); // Skip one, not found
					logger.warning("ReinjectDocx: cannot find object with flexoid '" + parsedFlexoObject.getFlexoId() + "' and user id '"
							+ parsedFlexoObject.getUserId() + "'");
					numberOfObjectNotFound++;
				}
			}

			// Handle duplicated names
			for (FlexoModelObject flexoObject : duplicatedFlexoNames.keySet()) {
				try {
					flexoObject.setName(duplicatedFlexoNames.get(flexoObject)[1]); // Try to set the new name
					numberOfNameUpdated++;
				} catch (Exception e) {
					try {
						// Ok cannot set the new name -> rollback to the old one
						addToErrorReport("Cannot change name of object '" + duplicatedFlexoNames.get(flexoObject)[0] + "' to '"
								+ duplicatedFlexoNames.get(flexoObject)[1] + "': the new name already exists");
						flexoObject.setName(duplicatedFlexoNames.get(flexoObject)[0]);
					} catch (Exception e1) { // Should not occur
						logger.log(
								Level.WARNING,
								"Reinject docx: cannot roll back name for object '" + flexoObject.getName() + " ("
										+ flexoObject.getUserIdentifier() + "_" + flexoObject.getFlexoID() + ")' to '"
										+ duplicatedFlexoNames.get(flexoObject)[0] + "'", e1);
						addToErrorReport("Cannot rollback name of object '" + flexoObject.getName() + "' to '"
								+ duplicatedFlexoNames.get(flexoObject)[0] + "': " + e1.getMessage());
					}
				}
			}
		} catch (InvalidFormatException e) {
			throw new InvalidArgumentException(e.getMessage());
		} catch (IOException e) {
			throw new FlexoException("IO exception while trying to handle docx reinjection file resources", e);
		} finally {
			hideFlexoProgress();
			try {
				in.close();
			} catch (IOException e) {
				throw new IOFlexoException(e);
			}
		}
	}

	private void copyNeededResourcesFromParsedHtml(IParsedHtml parsedHtml, File imagesDir) {
		for (IParsedHtmlResource htmlResource : parsedHtml.getNeededResources()) { // Copy needed resources (images) in imported images
																					// directory
			try {
				File resourceFile = new File(imagesDir, htmlResource.getIdentifier());
				resourceFile.createNewFile();
				FileOutputStream outStream = new FileOutputStream(resourceFile);
				outStream.write(htmlResource.getFile());
			} catch (IOException e) {
				logger.log(Level.WARNING, "Reinject docx: cannot write resource file with identifier '" + htmlResource.getIdentifier()
						+ "'", e);
				addToErrorReport("Cannot write resource file with identifier '" + htmlResource.getIdentifier() + "'");
			}
		}
	}

	private Set<String> getAvailableCssClasses() {
		try {
			Set<String> availableCssClasses = new HashSet<String>();

			Enumeration<?> en = getEditor().getProject().getDocumentationCssResource().getStyleSheet().getStyleNames();
			while (en.hasMoreElements()) {
				String styleName = String.valueOf(en.nextElement());

				if (styleName.startsWith(".") && styleName.trim().length() > 1) {
					availableCssClasses.add(styleName.substring(1).trim());
				}
			}

			return availableCssClasses;
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "Cannot load css for reinject.", e);
			return new HashSet<String>();
		}
	}

	private void addToErrorReport(String msg) {
		if (errorReport == null) {
			errorReport = new StringBuilder();
		} else if (errorReport.length() > 0) {
			errorReport.append("\n");
		}
		errorReport.append("* " + msg);
	}

	public File getDocxToReinject() {
		return docxToReinject;
	}

	public void setDocxToReinject(File docxToReinject) {
		this.docxToReinject = docxToReinject;
	}

	public int getNumberOfDescriptionUpdated() {
		return numberOfDescriptionUpdated;
	}

	public int getNumberOfNameUpdated() {
		return numberOfNameUpdated;
	}

	public int getNumberOfTocEntryTitleUpdated() {
		return numberOfTocEntryTitleUpdated;
	}

	public int getNumberOfTocEntryContentUpdated() {
		return numberOfTocEntryContentUpdated;
	}

	public int getNumberOfObjectNotFound() {
		return numberOfObjectNotFound;
	}

	public String getErrorReport() {
		return errorReport != null ? errorReport.toString() : "";
	}

	public boolean hasError() {
		return errorReport != null && errorReport.length() > 0;
	}
}
