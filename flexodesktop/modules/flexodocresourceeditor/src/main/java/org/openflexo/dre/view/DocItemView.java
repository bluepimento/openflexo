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
package org.openflexo.dre.view;

import org.openflexo.dre.AbstractDocItemView;
import org.openflexo.dre.controller.DREController;
import org.openflexo.drm.DRMObject;
import org.openflexo.drm.DocItem;
import org.openflexo.drm.DocResourceManager;
import org.openflexo.view.FlexoPerspective;

public class DocItemView extends AbstractDocItemView {

	public DocItemView(DocItem docItem, DREController controller) {
		super(docItem, controller, controller.getEditor());
	}

	public DREController getDREController() {
		return (DREController) _controller;
	}

	@Override
	public void deleteModuleView() {
		getDREController().removeModuleView(this);
	}

	@Override
	public FlexoPerspective<DRMObject> getPerspective() {
		return getDREController().DRE_PERSPECTIVE;
	}

	@Override
	public void updateViewFromModel() {
		super.updateViewFromModel();
		getDREController().getSaveDocumentationCenterButton().setEnabled(getDocResourceManager().needSaving());
	}

	@Override
	public DocResourceManager getDocResourceManager() {
		return getDREController().getDocResourceManager();
	}

	/**
	 * Overrides willShow
	 * 
	 * @see org.openflexo.view.ModuleView#willShow()
	 */
	@Override
	public void willShow() {

	}

	/**
	 * Overrides willHide
	 * 
	 * @see org.openflexo.view.ModuleView#willHide()
	 */
	@Override
	public void willHide() {

	}

	/**
	 * Returns flag indicating if this view is itself responsible for scroll management When not, Flexo will manage it's own scrollbar for
	 * you
	 * 
	 * @return
	 */
	@Override
	public boolean isAutoscrolled() {
		return false;
	}

}
