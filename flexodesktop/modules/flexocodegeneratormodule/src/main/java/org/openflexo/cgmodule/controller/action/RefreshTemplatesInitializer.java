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
package org.openflexo.cgmodule.controller.action;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.cg.templates.action.RefreshTemplates;
import org.openflexo.generator.ProjectGenerator;
import org.openflexo.icon.IconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class RefreshTemplatesInitializer extends ActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	RefreshTemplatesInitializer(GeneratorControllerActionInitializer actionInitializer) {
		super(RefreshTemplates.actionType, actionInitializer);
	}

	@Override
	protected GeneratorControllerActionInitializer getControllerActionInitializer() {
		return (GeneratorControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<RefreshTemplates> getDefaultInitializer() {
		return new FlexoActionInitializer<RefreshTemplates>() {
			@Override
			public boolean run(ActionEvent e, RefreshTemplates action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<RefreshTemplates> getDefaultFinalizer() {
		return new FlexoActionFinalizer<RefreshTemplates>() {
			@Override
			public boolean run(ActionEvent e, RefreshTemplates action) {
				// Chaque refresh doit faire un clear du cache des TemplateLocator de tous les project generator
				for (Enumeration<ProjectGenerator> en = getControllerActionInitializer().getGeneratorController().getProjectGenerators(); en
						.hasMoreElements();) {
					en.nextElement().getTemplateLocator().notifyTemplateModified();
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return IconLibrary.REFRESH_ICON;
	}

	@Override
	protected Icon getDisabledIcon() {
		return IconLibrary.REFRESH_DISABLED_ICON;
	}

}
