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
package org.openflexo.dgmodule.controller.action;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.action.CompareTemplatesInNewWindowInitializer;
import org.openflexo.dg.ProjectDocGenerator;
import org.openflexo.dgmodule.controller.DGController;
import org.openflexo.dgmodule.controller.DGSelectionManager;
import org.openflexo.doceditor.controller.action.DEControllerActionInitializer;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoActionizer;
import org.openflexo.foundation.action.OpenFileInExplorer;
import org.openflexo.foundation.cg.CGFile;
import org.openflexo.foundation.cg.DGRepository;
import org.openflexo.foundation.cg.GenerationRepository;
import org.openflexo.foundation.cg.templates.CGTemplate;
import org.openflexo.foundation.cg.templates.CGTemplateFile;
import org.openflexo.foundation.cg.templates.CGTemplateObject;
import org.openflexo.foundation.cg.templates.action.EditCustomTemplateFile;
import org.openflexo.foundation.cg.templates.action.OpenTemplateFileInNewWindow;
import org.openflexo.foundation.cg.templates.action.RedefineCustomTemplateFile;
import org.openflexo.generator.action.GCAction;
import org.openflexo.generator.exception.GenerationException;

public class DGControllerActionInitializer extends DEControllerActionInitializer {

	protected static final Logger logger = Logger.getLogger(DGControllerActionInitializer.class.getPackage().getName());

	static {
		FlexoModelObject.addActionForClass(OpenFileInExplorer.actionType, CGFile.class);
	}

	public DGControllerActionInitializer(DGController controller) {
		super(controller);
		deController = controller;
	}

	protected DGController getDGController() {
		return (DGController) deController;
	}

	@Override
	protected DGSelectionManager getDGSelectionManager() {
		return getDGController().getSelectionManager();
	}

	@Override
	public void initializeActions() {
		super.initializeActions();

		new AddGeneratedCodeRepositoryInitializer(this).init();
		new RemoveGeneratedCodeRepositoryInitializer(this).init();

		(new DGSetPropertyInitializer(this)).init();
		(new OpenFileInExplorerInitializer(this)).init();
		new CompareTemplatesInNewWindowInitializer(this).init();
		(new GenerateProcessScreenshotInitializer(this)).init();
		(new GenerateActivityScreenshotInitializer(this)).init();
		(new GenerateComponentScreenshotInitializer(this)).init();
		(new GenerateOperationScreenshotInitializer(this)).init();

		// Doc generation
		getProject().getGeneratedDoc().setFactory(new GCAction.ProjectGeneratorFactory() {
			/**
			 * Overrides generatorForRepository
			 * 
			 * @throws GenerationException
			 * @see org.openflexo.dg.action.DGAction.ProjectDocGeneratorFactory#generatorForRepository(org.openflexo.foundation.cg.DGRepository)
			 */
			@Override
			public ProjectDocGenerator generatorForRepository(GenerationRepository repository) {
				if (repository instanceof DGRepository) {
					return getDGController().getProjectGenerator((DGRepository) repository);
				} else {
					if (logger.isLoggable(Level.SEVERE)) {
						logger.severe("Cannot create project generator for " + repository);
					}
				}
				return null;
			}
		});
		(new SynchronizeRepositoryCodeGenerationInitializer(this)).init();
		(new GenerateSourceCodeInitializer(this)).init();
		(new GenerateAndWriteCodeInitializer(this)).init();
		(new ForceRegenerateSourceCodeInitializer(this)).init();
		(new RegenerateAndOverrideInitializer(this)).init();
		(new IncludeFromGenerationInitializer(this)).init();
		(new ExcludeFromGenerationInitializer(this)).init();
		(new ExportTOCAsTemplateInitializer(this)).init();
		(new WriteModifiedGeneratedFilesInitializer(this)).init();
		(new DismissUnchangedGeneratedFilesInitializer(this)).init();

		// Refreshing
		(new RefreshDGStructureInitializer(this)).init();

		// Edition
		(new EditGeneratedFileInitializer(this)).init();
		(new SaveGeneratedFileInitializer(this)).init();
		(new RevertToSavedGeneratedFileInitializer(this)).init();

		// Merge
		(new MarkAsMergedInitializer(this)).init();
		(new MarkAsUnmergedInitializer(this)).init();
		(new MarkAsMergedAllTrivialMergableFilesInitializer(this)).init();
		(new OverrideWithVersionInitializer(this)).init();
		(new CancelOverrideWithVersionInitializer(this)).init();

		// Accept disk version
		(new AcceptDiskUpdateInitializer(this)).init();

		// Versionning
		(new RegisterNewCGReleaseInitializer(this)).init();
		(new RevertRepositoryToVersionInitializer(this)).init();
		(new RevertToHistoryVersionInitializer(this)).init();

		(new CleanIntermediateFilesInitializer(this)).init();
		(new RefreshHistoryInitializer(this)).init();

		(new ShowReleaseHistoryInitializer(this)).init();
		(new ShowFileHistoryInitializer(this)).init();
		(new ShowDifferencesInitializer(this)).init();

		// PDF management
		(new GeneratePDFInitializer(this)).init();

		// DOCX management
		(new GenerateDocxInitializer(this)).init();
		(new ReinjectDocxInitializer(this)).init();

		// ZIP management
		(new GenerateZipInitializer(this)).init();

		// Repository management
		(new ConnectCGRepositoryInitializer(this)).init();
		(new DisconnectCGRepositoryInitializer(this)).init();

		// Show/view
		(new ShowFileVersionInitializer(this)).init();
		(new OpenDiffEditorInitializer(this)).init();

		// Templates management
		(new AddCustomTemplateRepositoryInitializer(this)).init();
		(new RemoveCustomTemplateRepositoryInitializer(this)).init();
		(new RedefineCustomTemplateFileInitializer(this)).init();
		(new EditCustomTemplateFileInitializer(this)).init();
		(new SaveCustomTemplateFileInitializer(this)).init();
		(new RefreshTemplatesInitializer(this)).init();
		(new OpenTemplateFileInNewWindowInitializer(this)).init();
		(new CancelEditionOfCustomTemplateFileInitializer(this)).init();
		(new RemoveTemplateFileInitializer(this)).init();
		// Initialize actions available using inspector (template tab)

		CGFile.showTemplateActionizer = new FlexoActionizer<OpenTemplateFileInNewWindow, CGTemplate, CGTemplateObject>(
				OpenTemplateFileInNewWindow.actionType, getDGController().getEditor());

		CGFile.editCustomTemplateActionizer = new FlexoActionizer<EditCustomTemplateFile, CGTemplateFile, CGTemplateObject>(
				EditCustomTemplateFile.actionType, getDGController().getEditor());

		CGFile.redefineTemplateActionizer = new FlexoActionizer<RedefineCustomTemplateFile, CGTemplate, CGTemplateObject>(
				RedefineCustomTemplateFile.actionType, getDGController().getEditor());

	}

}