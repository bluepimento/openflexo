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
package org.openflexo.fge.cp;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import org.openflexo.fge.GeometricGraphicalRepresentation;
import org.openflexo.fge.controller.DrawingController;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.area.FGEArea;

public abstract class GeometryAdjustingControlPoint<O extends FGEArea> extends ControlPoint {

	private static final Logger logger = Logger.getLogger(GeometryAdjustingControlPoint.class.getPackage().getName());

	private String name;

	public GeometryAdjustingControlPoint(GeometricGraphicalRepresentation<?> gr, String aName, FGEPoint pt) {
		super(gr, pt);
		name = aName;
	}

	@Override
	public GeometricGraphicalRepresentation<?> getGraphicalRepresentation() {
		return (GeometricGraphicalRepresentation<?>) super.getGraphicalRepresentation();
	}

	@Override
	public Cursor getDraggingCursor() {
		return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	}

	@Override
	public boolean isDraggable() {
		return true;
	}

	@Override
	public void startDragging(DrawingController<?> controller, FGEPoint startPoint) {
	}

	@Override
	public boolean dragToPoint(FGEPoint newRelativePoint, FGEPoint pointRelativeToInitialConfiguration, FGEPoint newAbsolutePoint,
			FGEPoint initialPoint, MouseEvent event) {
		return true;
	}

	@Override
	public void stopDragging(DrawingController<?> controller) {
	}

	public abstract void update(O geometricObject);

	public String getName() {
		return name;
	}

}
