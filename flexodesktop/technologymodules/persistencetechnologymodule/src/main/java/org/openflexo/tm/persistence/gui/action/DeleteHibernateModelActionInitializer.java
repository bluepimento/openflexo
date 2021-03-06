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
package org.openflexo.tm.persistence.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.icon.SGIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.sgmodule.controller.action.SGControllerActionInitializer;
import org.openflexo.tm.persistence.impl.HibernateModel;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DeleteHibernateModelActionInitializer extends ActionInitializer<DeleteHibernateModelAction, HibernateModel, HibernateModel> {

	public DeleteHibernateModelActionInitializer(SGControllerActionInitializer actionInitializer) {
		super(DeleteHibernateModelAction.actionType, actionInitializer);
	}

	@Override
	protected SGControllerActionInitializer getControllerActionInitializer() {
		return (SGControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<DeleteHibernateModelAction> getDefaultInitializer() {
		return new FlexoActionInitializer<DeleteHibernateModelAction>() {
			@Override
			public boolean run(ActionEvent e, DeleteHibernateModelAction action) {
				return FlexoController.confirmWithWarning(FlexoLocalization.localizedForKey("are_you_sure_you_want_to_delete "));
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return SGIconLibrary.DELETE_ICON;
	}

}
