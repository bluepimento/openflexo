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
package org.openflexo.vpm.view;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.GraphicalFlexoObserver;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.vpm.controller.CEDController;

/**
 * Represents the main pane for this module
 * 
 * @author yourname
 */
public class CEDMainPane extends FlexoMainPane implements GraphicalFlexoObserver {

	public CEDMainPane(ModuleView moduleView, CEDFrame mainFrame, CEDController controller) {
		super(moduleView, mainFrame, controller);
	}

	public void showBrowser() {
		showLeftView();
	}

	public void hideBrowser() {
		hideLeftView();
	}

	@Override
	protected FlexoModelObject getParentObject(FlexoModelObject object) {
		// Implements it if required
		return null;
	}

}
