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
package org.openflexo.fge.connectors.rpc;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.controller.DrawingController;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGERectPolylin;
import org.openflexo.fge.geom.area.FGEArea;

public class AdjustableEndControlPoint extends RectPolylinAdjustableControlPoint {
	static final Logger logger = Logger.getLogger(AdjustableEndControlPoint.class.getPackage().getName());

	private FGEArea draggingAuthorizedArea;

	public AdjustableEndControlPoint(FGEPoint point, RectPolylinConnector connector) {
		super(point, connector);
	}

	private FGEArea retrieveDraggingAuthorizedArea() {
		return getConnector().retrieveAllowedEndArea(false);

		/*FGEShape<?> shape = getConnector().getEndObject().getShape().getOutline();
		FGEShape<?> endArea = (FGEShape<?>) shape.transform(GraphicalRepresentation.convertNormalizedCoordinatesAT(getConnector().getEndObject(), getGraphicalRepresentation()));
		//endArea.setIsFilled(false);
		
		if (getConnector().getPrimitiveAllowedEndOrientations().size() > 0 
				&& getConnector().getPrimitiveAllowedEndOrientations().size() < 4) {
			// Some directions may not be available
			Vector<FGEArea> allowedAreas = new Vector<FGEArea>();
			for (SimplifiedCardinalDirection o : getConnector().getPrimitiveAllowedEndOrientations()) {
				allowedAreas.add(endArea.getAnchorAreaFrom(o));
			}
			
			return FGEUnionArea.makeUnion(allowedAreas);			
		}
		
		return endArea;*/
	}

	@Override
	public FGEArea getDraggingAuthorizedArea() {
		return draggingAuthorizedArea;
	}

	@Override
	public void startDragging(DrawingController controller, FGEPoint startPoint) {
		super.startDragging(controller, startPoint);
		draggingAuthorizedArea = retrieveDraggingAuthorizedArea();
		logger.info("draggingAuthorizedArea=" + draggingAuthorizedArea);
	}

	@Override
	public boolean dragToPoint(FGEPoint newRelativePoint, FGEPoint pointRelativeToInitialConfiguration, FGEPoint newAbsolutePoint,
			FGEPoint initialPoint, MouseEvent event) {
		FGEPoint pt = getNearestPointOnAuthorizedArea(newRelativePoint);
		if (pt == null) {
			logger.warning("Cannot nearest point for point " + newRelativePoint + " and area " + getDraggingAuthorizedArea());
			return false;
		}
		setPoint(pt);
		FGEPoint ptRelativeToEndObject = GraphicalRepresentation.convertNormalizedPoint(getGraphicalRepresentation(), pt, getConnector()
				.getEndObject());
		getConnector().setFixedEndLocation(ptRelativeToEndObject);
		switch (getConnector().getAdjustability()) {
		case AUTO_LAYOUT:
			// Nothing special to do
			break;
		case BASICALLY_ADJUSTABLE:
			// Nothing special to do
			break;
		case FULLY_ADJUSTABLE:
			if (initialPolylin.getSegmentNb() == 1 && getConnector()._updateAsFullyAdjustableForUniqueSegment(pt)
					&& !getConnector().getIsStartingLocationFixed()) {
				// OK this is still a unique segment, nice !
			} else {
				FGERectPolylin newPolylin = initialPolylin.clone();
				newPolylin.updatePointAt(newPolylin.getPointsNb() - 1, pt);
				getConnector().updateWithNewPolylin(newPolylin, true);
			}
			break;
		default:
			break;
		}
		getConnector()._connectorChanged(true);
		getGraphicalRepresentation().notifyConnectorChanged();

		return true;
	}

}
