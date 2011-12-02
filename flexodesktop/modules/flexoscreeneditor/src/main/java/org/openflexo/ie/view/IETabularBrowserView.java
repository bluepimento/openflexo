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
package org.openflexo.ie.view;

import org.openflexo.components.tabularbrowser.TabularBrowserModel;
import org.openflexo.components.tabularbrowser.TabularBrowserView;
import org.openflexo.ie.view.controller.IEController;

public class IETabularBrowserView extends TabularBrowserView {

	public IETabularBrowserView(IEController controller, TabularBrowserModel model, int visibleRowCount) {
		this(controller, model);
		setVisibleRowCount(visibleRowCount);
	}

	public IETabularBrowserView(IEController controller, TabularBrowserModel model) {
		super(controller, model, controller.getEditor());
		setSynchronizeWithSelectionManager(true);
	}

}
