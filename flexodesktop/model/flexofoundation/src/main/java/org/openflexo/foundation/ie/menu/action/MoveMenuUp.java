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
package org.openflexo.foundation.ie.menu.action;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.ie.menu.FlexoItemMenu;
import org.openflexo.logging.FlexoLogger;

/**
 * @author gpolet
 * 
 */
public class MoveMenuUp extends FlexoAction {

	protected static final Logger logger = FlexoLogger.getLogger(MoveMenuUp.class.getPackage().getName());

	public static FlexoActionType actionType = new FlexoActionType("move_up", FlexoActionType.defaultGroup) {

		/**
		 * Factory method
		 */
		@Override
		public FlexoAction makeNewAction(FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor) {
			return new MoveMenuUp(focusedObject, globalSelection, editor);
		}

		@Override
		protected boolean isVisibleForSelection(FlexoModelObject object, Vector globalSelection) {
			return true;
		}

		@Override
		protected boolean isEnabledForSelection(FlexoModelObject object, Vector globalSelection) {
			return (object != null) && (object instanceof FlexoItemMenu) && (((FlexoItemMenu) object).getFather() != null)
					&& ((((FlexoItemMenu) object).getFather()).getSubItems().indexOf(object) > 0);
		}

	};

	private FlexoItemMenu itemMenu;

	/**
	 * @param actionType
	 * @param focusedObject
	 * @param globalSelection
	 */
	protected MoveMenuUp(FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	/**
	 * Overrides doAction
	 * 
	 * @see org.openflexo.foundation.action.FlexoAction#doAction(java.lang.Object)
	 */
	@Override
	protected void doAction(Object context) throws FlexoException {
		FlexoItemMenu item = getItemMenu();
		if ((item.getFather() == null) || (item.getFather().getSubItems().indexOf(item) == 0)) {
			return;
		}
		item.getFather().switchItems(item.getFather().getSubItems().get(item.getFather().getSubItems().indexOf(item) - 1), item);
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Move menu " + item.getMenuLabel() + " up");
		}
	}

	public FlexoItemMenu getItemMenu() {
		return itemMenu;
	}

	public void setItemMenu(FlexoItemMenu itemMenu) {
		this.itemMenu = itemMenu;
	}

}
