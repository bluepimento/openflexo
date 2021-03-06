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
package org.openflexo.dgmodule.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.openflexo.FlexoCst;
import org.openflexo.dg.file.DGImageFile;
import org.openflexo.dg.file.DGScreenshotFile;
import org.openflexo.dgmodule.DGCst;
import org.openflexo.dgmodule.controller.DGController;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.FlexoObserver;
import org.openflexo.foundation.action.FlexoActionSource;
import org.openflexo.foundation.cg.CGFile;
import org.openflexo.foundation.cg.CGFile.FileContentEditor;
import org.openflexo.foundation.cg.generator.IFlexoResourceGenerator;
import org.openflexo.foundation.cg.generator.IGenerationException;
import org.openflexo.foundation.rm.FlexoCopiedResource;
import org.openflexo.foundation.rm.GeneratedResourceData;
import org.openflexo.foundation.rm.cg.CGRepositoryFileResource;
import org.openflexo.foundation.rm.cg.CopyOfFileResource;
import org.openflexo.foundation.rm.cg.GenerationStatus;
import org.openflexo.generator.ContentRegenerated;
import org.openflexo.generator.action.AcceptDiskUpdate;
import org.openflexo.generator.action.CancelOverrideWithVersion;
import org.openflexo.generator.action.DismissUnchangedGeneratedFiles;
import org.openflexo.generator.action.EditGeneratedFile;
import org.openflexo.generator.action.ForceRegenerateSourceCode;
import org.openflexo.generator.action.GenerateSourceCode;
import org.openflexo.generator.action.MarkAsMerged;
import org.openflexo.generator.action.RevertToSavedGeneratedFile;
import org.openflexo.generator.action.SaveGeneratedFile;
import org.openflexo.generator.action.SynchronizeRepositoryCodeGeneration;
import org.openflexo.generator.action.WriteModifiedGeneratedFiles;
import org.openflexo.generator.file.CGPackagedResourceFile;
import org.openflexo.generator.rm.GenerationAvailableFileResource;
import org.openflexo.icon.FilesIconLibrary;
import org.openflexo.icon.GeneratorIconLibrary;
import org.openflexo.icon.IconLibrary;
import org.openflexo.jedit.JEditTextArea.DisplayContext;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.swing.VerticalLayout;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.ModuleView;
import org.openflexo.view.listener.FlexoActionButton;

/**
 * @author sylvain
 */
public class DGFileModuleView extends JPanel implements ModuleView<CGFile>, FlexoObserver, FlexoActionSource, FileContentEditor {
	/**
	 * @author gpolet
	 * 
	 */
	public class ScreenshotDisplayer extends JPanel {
		private JLabel image;

		private final CGFile screenshotFile;

		/**
         * 
         */
		public ScreenshotDisplayer(CGFile screenshotFile) {
			super(new FlowLayout(FlowLayout.LEFT));
			this.screenshotFile = screenshotFile;
			update();
		}

		public void update() {
			if (image == null) {
				image = new JLabel();
				this.add(image);
				validate();
			}
			String imagePath = null;
			CGRepositoryFileResource<? extends GeneratedResourceData, ? extends IFlexoResourceGenerator, ? extends CGFile> resource = screenshotFile
					.getResource();
			if (resource instanceof CopyOfFileResource) {
				imagePath = ((CopyOfFileResource) resource).getResourceToCopy().getAbsolutePath();
			} else if (resource instanceof FlexoCopiedResource) {
				imagePath = ((FlexoCopiedResource) resource).getResourceToCopy().getFile().getAbsolutePath();
			}
			if (imagePath != null) {
				Image i = Toolkit.getDefaultToolkit().createImage(imagePath);
				ImageIcon icon = new ImageIcon(i);
				image.setText("");
				image.setIcon(icon);
			} else {
				image.setText(FlexoLocalization.localizedForKey("image_file_not_found", image));
			}
		}

	}

	private final Logger logger = FlexoLogger.getLogger(DGFileModuleView.class.getPackage().getName());

	protected CGFile _cgFile;

	protected DGController _controller;

	public DGFileModuleView(CGFile cgFile, DGController controller) {
		super(new BorderLayout());
		_controller = controller;
		_cgFile = cgFile;
		_cgFile.addObserver(this);
		updateView(true);
	}

	private GenerationStatus generationStatus = GenerationStatus.Unknown;

	private boolean isEdited = false;

	private boolean needsMemoryGeneration = false;

	public void refresh() {
		updateView(true);
	}

	private void updateView(boolean forceRebuild) {
		if ((forceRebuild) || (generationStatus == GenerationStatus.Unknown) || (generationStatus != _cgFile.getGenerationStatus())
				|| (generationStatus == GenerationStatus.GenerationError) || (isEdited != _cgFile.isEdited())
				|| (needsMemoryGeneration != _cgFile.needsMemoryGeneration())) {
			logger.fine("CGFileModuleView :" + _cgFile.getFileName() + " rebuild view for new status " + _cgFile.getGenerationStatus());
			rebuildView();
			repaint();
		}

		else {
			if (_header != null) {
				_header.update();
			}
			if (_codeDisplayer != null) {
				_codeDisplayer.update();
			}
			if (_screenshotDisplayer != null) {
				_screenshotDisplayer.update();
			}
		}
		validate();
		doLayout();
		repaint();
	}

	private DocDisplayer _codeDisplayer;

	private ScreenshotDisplayer _screenshotDisplayer;

	private ViewHeader _header;

	protected class ViewHeader extends JPanel {
		JLabel icon;

		JLabel title;

		JLabel subTitle;

		JPanel controlPanel;

		Vector<FlexoActionButton> actionButtons = new Vector<FlexoActionButton>();

		protected ViewHeader() {
			super(new BorderLayout());
			icon = new JLabel(FilesIconLibrary.mediumIconForFileFormat(_cgFile.getFileFormat()));
			icon.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			add(icon, BorderLayout.WEST);
			title = new JLabel(_cgFile.getFileName(), SwingConstants.LEFT);
			// title.setVerticalAlignment(JLabel.BOTTOM);
			title.setFont(DGCst.HEADER_FONT);
			title.setForeground(Color.BLACK);
			title.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
			subTitle = new JLabel(subTitleForFile(), SwingConstants.LEFT);
			// title.setVerticalAlignment(JLabel.BOTTOM);
			subTitle.setFont(DGCst.SUB_TITLE_FONT);
			subTitle.setForeground(Color.GRAY);
			subTitle.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

			JPanel labelsPanel = new JPanel(new GridLayout(2, 1));
			labelsPanel.add(title);
			labelsPanel.add(subTitle);
			add(labelsPanel, BorderLayout.CENTER);

			controlPanel = new JPanel(new FlowLayout());

			if (isEdited) {
				FlexoActionButton saveAction = new FlexoActionButton(SaveGeneratedFile.actionType, "save", DGFileModuleView.this,
						_controller.getEditor());
				FlexoActionButton revertToSavedAction = new FlexoActionButton(RevertToSavedGeneratedFile.actionType, "revert_to_saved",
						DGFileModuleView.this, _controller.getEditor());
				actionButtons.add(saveAction);
				actionButtons.add(revertToSavedAction);
				controlPanel.add(saveAction);
				controlPanel.add(revertToSavedAction);
			} else {
				FlexoActionButton editFileAction = new FlexoActionButton(EditGeneratedFile.actionType, "edit", DGFileModuleView.this,
						_controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(editFileAction);
				}

				FlexoActionButton generateAction = new FlexoActionButton(GenerateSourceCode.actionType, "regenerate",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(generateAction);
				}

				FlexoActionButton writeFileAction = new FlexoActionButton(WriteModifiedGeneratedFiles.actionType, "write_file",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(writeFileAction);
				}

				FlexoActionButton dismissUnchangedFileAction = new FlexoActionButton(DismissUnchangedGeneratedFiles.actionType,
						"dismiss_as_unchanged", DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(dismissUnchangedFileAction);
				}

				FlexoActionButton forceRegenerateAction = new FlexoActionButton(ForceRegenerateSourceCode.actionType, "force_regenerate",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(forceRegenerateAction);
				}

				FlexoActionButton acceptDiskVersionAction = new FlexoActionButton(AcceptDiskUpdate.actionType, "accept_disk_version",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(acceptDiskVersionAction);
				}

				FlexoActionButton markAsMergedAction = new FlexoActionButton(MarkAsMerged.actionType, "mark_as_merged",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(markAsMergedAction);
				}

				FlexoActionButton cancelOverridingAction = new FlexoActionButton(CancelOverrideWithVersion.actionType, "cancel_override",
						DGFileModuleView.this, _controller.getEditor());
				if (!_cgFile.getMarkedAsDoNotGenerate()) {
					actionButtons.add(cancelOverridingAction);
				}

				if (_cgFile.getGenerationStatus() == GenerationStatus.UpToDate) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(forceRegenerateAction);
					}
				}

				if ((_cgFile.getGenerationStatus() == GenerationStatus.UpToDate) || (_cgFile.getGenerationStatus().isGenerationModified())
						|| (_cgFile.getGenerationStatus().isDiskModified())
						|| (_cgFile.getGenerationStatus() == GenerationStatus.ConflictingMarkedAsMerged)) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(editFileAction);
					}
				}

				if ((_cgFile.getGenerationStatus().isGenerationModified()) || (_cgFile.getGenerationStatus().isConflicting())
						|| (_cgFile.getGenerationStatus() == GenerationStatus.GenerationError)) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(generateAction);
					}
				}

				if ((_cgFile.getGenerationStatus().isGenerationModified())
						|| (_cgFile.getGenerationStatus() == GenerationStatus.ConflictingMarkedAsMerged)) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(writeFileAction);
					}
				}

				if (_cgFile.getResource().doesGenerationKeepFileUnchanged()) {
					if ((_cgFile.getGenerationStatus().isGenerationModified())
							|| (_cgFile.getGenerationStatus() == GenerationStatus.ConflictingMarkedAsMerged)
							|| (_cgFile.getGenerationStatus() == GenerationStatus.OverrideScheduled)) {
						controlPanel.add(dismissUnchangedFileAction);
					}
				}

				if (_cgFile.getGenerationStatus().isDiskModified()) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(acceptDiskVersionAction);
					}
				}

				if (_cgFile.getGenerationStatus() == GenerationStatus.ConflictingUnMerged) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(markAsMergedAction);
					}
				}

				if (_cgFile.getGenerationStatus() == GenerationStatus.OverrideScheduled) {
					if (!_cgFile.getMarkedAsDoNotGenerate()) {
						controlPanel.add(cancelOverridingAction);
					}
				}

			}

			controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			add(controlPanel, BorderLayout.EAST);

			if (!isEdited) {

				if (_cgFile.getResource() instanceof GenerationAvailableFileResource) {
					GenerationAvailableFileResource resource = (GenerationAvailableFileResource) _cgFile.getResource();
					IFlexoResourceGenerator generator = resource.getGenerator();
					if (generator != null) {
						if (generator.hasFormattingException()) {
							addInfoPanel(IconLibrary.UNFIXABLE_WARNING_ICON,
									FlexoLocalization.localizedForKey("java_formatting_warning_title"),
									FlexoLocalization.localizedForKey("java_formatting_warning_description"));
						}
					}
				}

				if (_cgFile.needsMemoryGeneration()) {
					addInfoPanel(GeneratorIconLibrary.NEEDS_REGENERATE_ICON, FlexoLocalization.localizedForKey("needs_regenerate_warning"),
							FlexoLocalization.localizedForKey("needs_regenerate_warning_description"));
				}

				if (_cgFile.hasGenerationErrors()) {
					addInfoPanel(IconLibrary.UNFIXABLE_ERROR_ICON, FlexoLocalization.localizedForKey("generation_error"),
							FlexoLocalization.localizedForKey("generation_error_description"));
				}
			}

			update();
			validate();
		}

		private void addInfoPanel(Icon icon, String titleString, String textString) {
			JLabel regenerateIcon = new JLabel(icon);
			JLabel title = new JLabel(titleString, SwingConstants.LEFT);
			title.setFont(FlexoCst.BOLD_FONT);
			JTextArea text = new JTextArea(textString);
			text.setBackground(null);
			text.setEditable(false);
			text.setFont(FlexoCst.NORMAL_FONT);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));
			JPanel infoPanel = new JPanel(new VerticalLayout());
			JPanel titlePanel = new JPanel(new FlowLayout());
			titlePanel.add(regenerateIcon);
			titlePanel.add(title);
			infoPanel.add(titlePanel);
			infoPanel.add(text);
			add(infoPanel, BorderLayout.SOUTH);

		}

		private String subTitleForFile() {
			if (_cgFile.getMarkedAsDoNotGenerate()) {
				return FlexoLocalization.localizedForKey("file_marked_as_do_not_generate");
			}
			GenerationStatus generationStatus = _cgFile.getGenerationStatus();
			String returned = "";
			if (isEdited) {
				returned = FlexoLocalization.localizedForKey("edition_of_file_on_disk");
			} else {
				returned = generationStatus.getLocalizedStringRepresentation();
				if (!generationStatus.isAbnormal() && (generationStatus != GenerationStatus.CodeGenerationNotSynchronized)) {
					if (_cgFile.needsMemoryGeneration()) {
						returned += ", " + FlexoLocalization.localizedForKey("generation_required");
					} else {
						returned += ", " + FlexoLocalization.localizedForKey("generation_is_up_to_date");
					}
				}
			}

			return returned;
		}

		protected void update() {
			title.setText(_cgFile.getFileName());
			subTitle.setText(subTitleForFile());
			for (FlexoActionButton button : actionButtons) {
				button.update();
			}
		}

	}

	private void rebuildView() {
		DisplayContext previousDisplayContext = null;
		if (_codeDisplayer != null) {
			previousDisplayContext = _codeDisplayer.getDisplayContext();
		}
		removeAll();

		isEdited = _cgFile.isEdited();

		_header = new ViewHeader();

		add(_header, BorderLayout.NORTH);
		validate();
		generationStatus = _cgFile.getGenerationStatus();
		needsMemoryGeneration = _cgFile.needsMemoryGeneration();
		_codeDisplayer = null;
		_screenshotDisplayer = null;

		if (_cgFile.getMarkedAsDoNotGenerate()) {
			add(new JLabel(FlexoLocalization.localizedForKey("file_marked_as_do_not_generate"), SwingConstants.CENTER), BorderLayout.CENTER);
			validate();
			return;
		} else if (isEdited && (_cgFile.getResource() instanceof GenerationAvailableFileResource)) {
			_codeDisplayer = new DocEditor((GenerationAvailableFileResource) _cgFile.getResource(), _controller);
			_codeDisplayer.getComponent().validate();
			add(_codeDisplayer.getComponent());
			validate();
			_codeDisplayer.getComponent().requestFocus();
		} else {

			if (_cgFile.getGenerationStatus() == GenerationStatus.CodeGenerationNotAvailable) {
				add(new JLabel(FlexoLocalization.localizedForKey("sorry_code_generator_not_available_int_this_version"),
						SwingConstants.CENTER), BorderLayout.CENTER);
				validate();
				return;
			} else if (_cgFile.getGenerationStatus() == GenerationStatus.CodeGenerationNotSynchronized) {
				FlexoActionButton syncButton = new FlexoActionButton(SynchronizeRepositoryCodeGeneration.actionType, "synchronize_now",
						new FlexoActionSource() {
							@Override
							public FlexoModelObject getFocusedObject() {
								return _cgFile.getRepository();
							}

							@Override
							public Vector getGlobalSelection() {
								return null;
							}

							@Override
							public FlexoEditor getEditor() {
								return _controller.getEditor();
							}
						}, _controller.getEditor());
				JPanel panel = new JPanel(new VerticalLayout());
				panel.add(new JLabel(FlexoLocalization.localizedForKey("please_synchronize_code_generation"), SwingConstants.CENTER));
				JPanel buttonPanel = new JPanel(new FlowLayout());
				buttonPanel.add(syncButton);
				buttonPanel.validate();
				panel.add(buttonPanel);
				panel.validate();
				panel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));
				add(panel, BorderLayout.CENTER);
				validate();
				return;
			} else if (_cgFile.getGenerationStatus() == GenerationStatus.NotGenerated) {
				add(new JLabel(FlexoLocalization.localizedForKey("code_has_not_been_generated"), SwingConstants.CENTER),
						BorderLayout.CENTER);
				return;
			}
			if (_cgFile.getResource() instanceof GenerationAvailableFileResource) {
				GenerationAvailableFileResource resource = (GenerationAvailableFileResource) _cgFile.getResource();
				IFlexoResourceGenerator generator = resource.getGenerator();

				if (_cgFile.getGenerationStatus() == GenerationStatus.GenerationError) {
					ExceptionPanel exceptionPanel = new ExceptionPanel(generator.getGenerationException());
					add(exceptionPanel);
					return;
				}

				if (_cgFile.getGenerationStatus().isGenerationModified() || _cgFile.getGenerationStatus().isDiskModified()) {
					_codeDisplayer = new DiffCodeDisplayer(resource, _controller);
					if (_codeDisplayer.getComponent() != null) {
						add(_codeDisplayer.getComponent());
					}
				}

				else if ((_cgFile.getGenerationStatus() == GenerationStatus.UpToDate)) {
					_codeDisplayer = new DocDisplayer(resource, _controller);
					if (_codeDisplayer.getComponent() != null) {
						add(_codeDisplayer.getComponent());
					}
				}

				else if ((_cgFile.getGenerationStatus().isConflicting()) && (_cgFile.isOverrideScheduled())) {
					_codeDisplayer = new DiffCodeDisplayer(resource, _controller);
					if (_codeDisplayer.getComponent() != null) {
						add(_codeDisplayer.getComponent());
					}
				}

				else if ((_cgFile.getGenerationStatus().isConflicting())) {
					_codeDisplayer = new MergeDocDisplayer(resource, _controller);
					if (_codeDisplayer.getComponent() != null) {
						_codeDisplayer.getComponent().revalidate();
						add(_codeDisplayer.getComponent());
					}
					((MergeDocDisplayer) _codeDisplayer).setEditable(_cgFile.getGenerationStatus() == GenerationStatus.ConflictingUnMerged);
				}

				else {
					logger.warning("I should not come here !");
				}
				validate();
			} else {
				if (_cgFile instanceof DGScreenshotFile) {
					_screenshotDisplayer = new ScreenshotDisplayer(_cgFile);
					add(_screenshotDisplayer);
					validate();
				} else if ((_cgFile instanceof CGPackagedResourceFile)
						&& ((CGPackagedResourceFile) _cgFile).getResource().getResourceFormat().isImage()) {
					_screenshotDisplayer = new ScreenshotDisplayer(_cgFile);
					add(_screenshotDisplayer);
					validate();
				} else if (_cgFile instanceof DGImageFile) {
					_screenshotDisplayer = new ScreenshotDisplayer(_cgFile);
					add(_screenshotDisplayer);
					validate();
				}
			}
		}
		if ((previousDisplayContext != null) && (_codeDisplayer != null)) {
			_codeDisplayer.setDisplayContext(previousDisplayContext);
		}
	}

	public DGController getController() {
		return _controller;
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("CGFileModuleView : RECEIVED " + dataModification + " for " + observable);
		}
		if (!isShowing()) {
			return;
		}
		if (dataModification instanceof ContentRegenerated) {
			updateView(true);
		} else {
			updateView(false);
		}
	}

	@Override
	public void deleteModuleView() {
		logger.info("CGFileModuleView view deleted");
		getController().removeModuleView(this);
		_cgFile.deleteObserver(this);
	}

	@Override
	public FlexoPerspective<FlexoModelObject> getPerspective() {
		return _controller.CODE_GENERATOR_PERSPECTIVE;
	}

	@Override
	public CGFile getRepresentedObject() {
		return _cgFile;
	}

	@Override
	public void willHide() {
		// Releases the screenshot displayer to free memory
		_screenshotDisplayer = null;
	}

	@Override
	public void willShow() {
		refresh();
	}

	protected class ExceptionPanel extends JPanel {
		protected ExceptionPanel(IGenerationException exception) {
			super(new VerticalLayout());

			// Top
			JLabel exceptionLabel = new JLabel(FlexoLocalization.localizedForKey("exception"));
			exceptionLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JTextField exceptionTF = new JTextField(exception.getLocalizedMessage());
			exceptionTF.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JPanel topPanel = new JPanel(new BorderLayout());
			topPanel.add(exceptionLabel, BorderLayout.WEST);
			topPanel.add(exceptionTF, BorderLayout.CENTER);
			add(topPanel);

			// Details
			JLabel detailsLabel = new JLabel(FlexoLocalization.localizedForKey("details"), SwingConstants.LEFT);
			detailsLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JTextArea detailsTA = new JTextArea(exception.getDetails());
			detailsTA.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			detailsTA.setFont(FlexoCst.MEDIUM_FONT);
			add(detailsLabel);
			add(detailsTA);

			// Stack trace
			JLabel stLabel = new JLabel(FlexoLocalization.localizedForKey("stacktrace"), SwingConstants.LEFT);
			stLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			String stackTrace = null;
			if (exception.getTargetException() != null) {
				StringWriter w = new StringWriter();
				exception.getTargetException().printStackTrace(new PrintWriter(w));
				stackTrace = w.toString();
			} else {
				stackTrace = FlexoLocalization.localizedForKey("stack_trace_not_available");
			}
			JTextArea stackTraceTA = new JTextArea(stackTrace);
			stackTraceTA.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			stackTraceTA.setFont(FlexoCst.CODE_FONT);
			stackTraceTA.setForeground(Color.DARK_GRAY);
			add(stLabel);
			add(stackTraceTA);

			// Cause stack trace
			if ((exception.getTargetException() != null) && (exception.getTargetException().getCause() != null)) {
				JLabel causeSTLabel = new JLabel(FlexoLocalization.localizedForKey("cause_stacktrace"), SwingConstants.LEFT);
				causeSTLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				String causeStackTrace = null;
				StringWriter w = new StringWriter();
				exception.getTargetException().getCause().printStackTrace(new PrintWriter(w));
				causeStackTrace = w.toString();
				JTextArea causeStackTraceTA = new JTextArea(causeStackTrace);
				causeStackTraceTA.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				causeStackTraceTA.setFont(FlexoCst.CODE_FONT);
				causeStackTraceTA.setForeground(Color.DARK_GRAY);
				add(causeSTLabel);
				add(causeStackTraceTA);
			}

			validate();
		}

	}

	/**
	 * Returns flag indicating if this view is itself responsible for scroll management When not, Flexo will manage it's own scrollbar for
	 * you
	 * 
	 * @return
	 */
	@Override
	public boolean isAutoscrolled() {
		// Only exception panel requires Flexo scroll scheme
		// Other panels are autonomous
		return (_cgFile.getGenerationStatus() != GenerationStatus.GenerationError) && !(_cgFile instanceof DGScreenshotFile);
	}

	@Override
	public FlexoModelObject getFocusedObject() {
		return getRepresentedObject();
	}

	@Override
	public Vector getGlobalSelection() {
		return null;
	}

	@Override
	public String getEditedContentForKey(String contentKey) {
		if (isEdited) {
			return ((FileContentEditor) _codeDisplayer).getEditedContentForKey(contentKey);
		}
		logger.warning("getEditedContentForKey() called for a non edited file");
		return null;
	}

	@Override
	public void setEditedContent(CGFile file) {
		if (isEdited) {
			((FileContentEditor) _codeDisplayer).setEditedContent(file);
		} else {
			logger.warning("setEditedContent() called for a non edited file");
		}
	}

	@Override
	public FlexoEditor getEditor() {
		return _controller.getEditor();
	}

}
