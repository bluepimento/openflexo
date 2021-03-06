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

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.cg.action.ExportTOCAsTemplate;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class ExportTOCAsTemplateInitializer extends ActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	ExportTOCAsTemplateInitializer(DGControllerActionInitializer actionInitializer) {
		super(ExportTOCAsTemplate.actionType, actionInitializer);
	}

	@Override
	protected DGControllerActionInitializer getControllerActionInitializer() {
		return (DGControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<ExportTOCAsTemplate> getDefaultInitializer() {
		return new FlexoActionInitializer<ExportTOCAsTemplate>() {
			@Override
			public boolean run(ActionEvent e, ExportTOCAsTemplate action) {

				JFileChooser chooser = new JFileChooser();
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setDialogTitle(FlexoLocalization.localizedForKey("save_as_image", chooser));

				File dest = null;
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.CANCEL_OPTION) {
					return false;
				}
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (isValidProjectName(chooser.getSelectedFile().getName())) {
						dest = chooser.getSelectedFile();
						if (!(dest.getName().toLowerCase().endsWith(".xml"))) {
							dest = new File(dest.getAbsolutePath() + ".xml");
						}
					} else {
						if (logger.isLoggable(Level.WARNING)) {
							logger.warning("Invalid file name. The following characters are not allowed: "
									+ FileUtils.BAD_CHARACTERS_FOR_FILE_NAME_REG_EXP);
						}
						FlexoController.notify(FlexoLocalization.localizedForKey("file_name_cannot_contain_\\___&_#_{_}_[_]_%_~"));
					}
				} else {
					if (logger.isLoggable(Level.WARNING)) {
						logger.warning("No project specified !");
					}
				}

				if (!dest.exists()) {
					try {
						FileUtils.createNewFile(dest);
					} catch (IOException e1) {
						e1.printStackTrace();
						return false;
					}
				}
				action.setDestinationFile(dest);
				return true;
			}

			private boolean isValidProjectName(String absolutePath) {
				return absolutePath != null && absolutePath.trim().length() > 0
						&& !FileUtils.BAD_CHARACTERS_FOR_FILE_NAME_PATTERN.matcher(absolutePath).find();
			}
		};
	}

}
