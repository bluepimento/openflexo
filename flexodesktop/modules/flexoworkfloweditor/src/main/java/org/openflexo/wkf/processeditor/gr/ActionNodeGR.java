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
package org.openflexo.wkf.processeditor.gr;

import java.awt.Color;

import org.openflexo.fge.graphics.BackgroundStyle;
import org.openflexo.fge.graphics.ForegroundStyle;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.wkf.node.ActionNode;
import org.openflexo.wkf.processeditor.ProcessRepresentation;
import org.openflexo.wkf.swleditor.SWLEditorConstants;
import org.openflexo.wkf.utils.ActionNodeShapePainter;

public class ActionNodeGR extends AbstractActionNodeGR {

	private ForegroundStyle foreground;
	private BackgroundStyle background;

	public ActionNodeGR(ActionNode actionNode, ProcessRepresentation aDrawing, boolean isInPalet) {
		super(actionNode, aDrawing, isInPalet);
		setIsFloatingLabel(true);
		setWidth(30);
		setHeight(30);
		updateImageBackground();
		setDimensionConstraints(DimensionConstraints.UNRESIZABLE);
		setShapePainter(new ActionNodeShapePainter(this));
	}

	/**
	 *
	 */
	private void updateImageBackground() {
		/*if (getActionNode().getImageIcon() != null) {
			foreground = ForegroundStyle.makeNone();
			background = BackgroundStyle.makeImageBackground(getActionNode().getImageIcon());
			((BackgroundImage)background).setScaleX(1);
			((BackgroundImage)background).setScaleY(1);
			if (getActionNode().getActionType() == ActionType.FLEXO_ACTION) {
				((BackgroundImage)background).setDeltaX(-2);
				((BackgroundImage)background).setDeltaY(-3);
			}
			else if (getActionNode().getActionType() == ActionType.DISPLAY_ACTION) {
				((BackgroundImage)background).setDeltaX(-3);
				((BackgroundImage)background).setDeltaY(-2);
			}
		}
		else {*/
		foreground = ForegroundStyle.makeStyle(Color.RED);
		foreground.setLineWidth(0.6);
		background = BackgroundStyle.makeColoredBackground(getMainBgColor());
		// }
		setForeground(foreground);
		setBackground(background);
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		if (dataModification.propertyName() != null && dataModification.propertyName().equals("actionType")) {
			updateImageBackground();
		}
		super.update(observable, dataModification);
	}

	/**
	 * Overriden to implement defaut automatic layout
	 */
	@Override
	public double _getDefaultX() {
		int index = getActionNode().getParentPetriGraph().getIndexForNormalNode(getActionNode());
		/*		double xOffset = 0;
				Vector<FlexoNode> allBeginNodes = getActionNode().getParentPetriGraph().getAllBeginNodes();
				if (allBeginNodes.size()>0) {
					ShapeGraphicalRepresentation<?> gr = (ShapeGraphicalRepresentation<?>) getGraphicalRepresentation(allBeginNodes.firstElement());
					if (gr!=null && allBeginNodes.firstElement()!=getActionNode() && allBeginNodes.firstElement().hasLocationForContext(BASIC_PROCESS_EDITOR))
						xOffset = gr.getX();
				}
				return (index % 4 ) * 50 + xOffset + 75;
		*/
		return (index % 4) * 50 + 50;
	}

	/**
	 * Overriden to implement defaut automatic layout
	 */
	@Override
	public double _getDefaultY() {
		int index = getActionNode().getParentPetriGraph().getIndexForNormalNode(getActionNode());
		/*		double yOffset = 0;
				Vector<FlexoNode> allBeginNodes = getActionNode().getParentPetriGraph().getAllBeginNodes();
				if (allBeginNodes.size()>0) {
					ShapeGraphicalRepresentation<?> gr = (ShapeGraphicalRepresentation<?>) getGraphicalRepresentation(allBeginNodes.firstElement());
					if (gr!=null && allBeginNodes.firstElement()!=getActionNode() && allBeginNodes.firstElement().hasLocationForContext(BASIC_PROCESS_EDITOR))
						yOffset = gr.getY();
				}
				return ((index / 4 )) * 50+yOffset;
		*/
		return (index / 4) * 50;
	}

	@Override
	public double getDefaultLabelX() {
		if (getModel().hasLabelLocationForContext(SWLEditorConstants.SWIMMING_LANE_EDITOR)) {
			return getModel().getLabelLocation(SWLEditorConstants.SWIMMING_LANE_EDITOR).getX();
		}
		return getLeftBorder() + 15;
	}

	@Override
	public double getDefaultLabelY() {
		if (getModel().hasLabelLocationForContext(SWLEditorConstants.SWIMMING_LANE_EDITOR)) {
			return getModel().getLabelLocation(SWLEditorConstants.SWIMMING_LANE_EDITOR).getY();
		}
		return getTopBorder() + 40;
	}

}
