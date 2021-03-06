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
package org.openflexo.wkf.view;

import java.util.logging.Logger;

import org.openflexo.ch.FCH;
import org.openflexo.components.browser.view.BrowserView;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.ie.cl.ComponentDefinition;
import org.openflexo.foundation.wkf.WKFObject;
import org.openflexo.module.ModuleLoader;
import org.openflexo.module.ModuleLoadingException;
import org.openflexo.module.external.ExternalIEModule;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.wkf.controller.ProcessBrowser;
import org.openflexo.wkf.controller.WKFController;

/**
 * Represents the view for WKF Browser
 * 
 * @author sguerin
 * 
 */
public class ProcessBrowserView extends BrowserView {

	private static final Logger logger = Logger.getLogger(ProcessBrowserView.class.getPackage().getName());

	// ==========================================================================
	// ============================= Variables
	// ==================================
	// ==========================================================================

	protected WKFController _controller;

	// ==========================================================================
	// ============================= Constructor
	// ================================
	// ==========================================================================

	public ProcessBrowserView(ProcessBrowser processBrowser, WKFController controller) {
		super(processBrowser, controller.getKeyEventListener(), controller.getEditor());
		_controller = controller;
		FCH.setHelpItem(this, "process-browser");
	}

	@Override
	public void treeSingleClick(FlexoModelObject object) {
		/*
		 * FlexoProcess currentProcess = _controller.getCurrentFlexoProcess(); FlexoProcessView currentProcessView =
		 * _controller.getCurrentFlexoProcessView();
		 * 
		 * if (object instanceof WKFObject) { FlexoProcess objectProcess = ((WKFObject)object).getProcess(); if (objectProcess ==
		 * currentProcess) { // This is the current displayed process // I may select the view (if this view exists) if
		 * (_controller.setSelectedObject((WKFObject)object)) { // I have selected the view, the inspector has already been set // I return.
		 * return; } } else { // This is not the current displayed process, i continue } }
		 */

		// If this object is inspectable, inspect it.
		/*
		 * if (object instanceof InspectableObject) { _controller.setCurrentInspectedObject((InspectableObject) object); }
		 */

	}

	@Override
	public void treeDoubleClick(FlexoModelObject object) {
		if (object instanceof WKFObject) {
			treeSingleClick(object);
		} else if (object instanceof ComponentDefinition) {
			ExternalIEModule ieModule = null;
			try {
				ieModule = getModuleLoader().getIEModule(object.getProject());
			} catch (ModuleLoadingException e) {
				FlexoController.notify("Cannot load Screen Editor. Exception : " + e.getMessage());
				e.printStackTrace();
			}
			if (ieModule == null) {
				return;
			}
			ieModule.focusOn();
			ieModule.showScreenInterface(((ComponentDefinition) object).getDummyComponentInstance());
		}
	}

	private ModuleLoader getModuleLoader() {
		return ModuleLoader.instance();
	}

}
