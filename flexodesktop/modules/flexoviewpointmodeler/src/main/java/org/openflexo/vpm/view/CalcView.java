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

import org.openflexo.fib.controller.FIBComponentDynamicModel;
import org.openflexo.fib.controller.FIBTableDynamicModel;
import org.openflexo.fib.model.listener.FIBMouseClickListener;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.viewpoint.EditionPattern;
import org.openflexo.foundation.viewpoint.ExampleDrawingShema;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointPalette;
import org.openflexo.view.FIBModuleView;
import org.openflexo.vpm.CEDCst;
import org.openflexo.vpm.controller.CEDController;
import org.openflexo.vpm.controller.ViewPointPerspective;

/**
 * Please comment this class
 * 
 * @author sguerin
 * 
 */
public class CalcView extends FIBModuleView<ViewPoint> implements FIBMouseClickListener {

	public CalcView(ViewPoint viewPoint, CEDController controller) {
		super(viewPoint, controller, CEDCst.CALC_VIEW_FIB);

		controller.manageResource(viewPoint);
	}

	@Override
	public CEDController getFlexoController() {
		return (CEDController) super.getFlexoController();
	}

	@Override
	public ViewPointPerspective getPerspective() {
		return getFlexoController().VIEW_POINT_PERSPECTIVE;
	}

	@Override
	public void mouseClicked(FIBComponentDynamicModel data, int clickCount) {
		if (data instanceof FIBTableDynamicModel && ((FIBTableDynamicModel) data).selected instanceof FlexoModelObject && clickCount == 2) {
			FlexoModelObject o = (FlexoModelObject) ((FIBTableDynamicModel) data).selected;
			if (o instanceof ViewPoint || o instanceof EditionPattern || o instanceof ExampleDrawingShema || o instanceof ViewPointPalette) {
				getFlexoController().selectAndFocusObject(o);
			}
		}
	}

}
