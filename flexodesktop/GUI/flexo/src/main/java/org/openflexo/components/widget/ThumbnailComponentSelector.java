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
package org.openflexo.components.widget;

import org.openflexo.foundation.ie.IERegExp;
import org.openflexo.foundation.ie.cl.ComponentDefinition;
import org.openflexo.foundation.ie.cl.FlexoComponentFolder;
import org.openflexo.foundation.ie.cl.TabComponentDefinition;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.FlexoController;

/**
 * Widget allowing to select a ThumbnailComponent while browing the component library
 * 
 * @author sguerin
 * 
 */
public class ThumbnailComponentSelector extends AbstractComponentSelector<TabComponentDefinition> {

	public ThumbnailComponentSelector(FlexoProject project, TabComponentDefinition thumbnailComponent) {
		super(project, thumbnailComponent, TabComponentDefinition.class);
	}

	public ThumbnailComponentSelector(FlexoProject project, TabComponentDefinition thumbnailComponent, int cols) {
		super(project, thumbnailComponent, TabComponentDefinition.class, cols);
	}

	@Override
	public void newComponent() {
		FlexoComponentFolder folder = null;
		if (getSelectedObject() instanceof FlexoComponentFolder) {
			folder = (FlexoComponentFolder) getSelectedObject();
		} else if (getSelectedObject() instanceof ComponentDefinition) {
			folder = ((ComponentDefinition) getSelectedObject()).getFolder();
		} else {
			folder = getComponentLibrary().getRootFolder();
		}
		String newComponentName = FlexoController.askForStringMatchingPattern(FlexoLocalization.localizedForKey("enter_a_component_name"),
				IERegExp.JAVA_CLASS_NAME_PATTERN,
				FlexoLocalization.localizedForKey("must_start_with_a_letter_followed_by_any_letter_or_number"));
		try {
			TabComponentDefinition newComponent = new TabComponentDefinition(newComponentName, getComponentLibrary(), folder, getProject());
			setEditedObject(newComponent);
		} catch (DuplicateResourceException e) {
			// Warns about the exception
			FlexoController.notify(FlexoLocalization.localizedForKey("invalid_name_a_component_with_this_name_already_exists"));
		}

	}
}
