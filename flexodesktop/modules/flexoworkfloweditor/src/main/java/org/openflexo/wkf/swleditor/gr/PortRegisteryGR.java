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
package org.openflexo.wkf.swleditor.gr;

import static org.openflexo.wkf.swleditor.gr.ContainerGR.logger;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.openflexo.fge.FGEConstants;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.controller.CustomDragControlAction;
import org.openflexo.fge.controller.DrawingController;
import org.openflexo.fge.controller.MouseDragControl;
import org.openflexo.fge.cp.ControlArea;
import org.openflexo.fge.geom.FGEGeometricObject.SimplifiedCardinalDirection;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGESegment;
import org.openflexo.fge.geom.area.FGEArea;
import org.openflexo.fge.geom.area.FGEHalfLine;
import org.openflexo.fge.geom.area.FGEUnionArea;
import org.openflexo.fge.graphics.BackgroundStyle;
import org.openflexo.fge.graphics.BackgroundStyle.ColorGradient.ColorGradientDirection;
import org.openflexo.fge.graphics.DecorationPainter;
import org.openflexo.fge.graphics.FGEGraphics;
import org.openflexo.fge.graphics.ForegroundStyle;
import org.openflexo.fge.graphics.ForegroundStyle.CapStyle;
import org.openflexo.fge.graphics.ForegroundStyle.DashStyle;
import org.openflexo.fge.graphics.ForegroundStyle.JoinStyle;
import org.openflexo.fge.graphics.TextStyle;
import org.openflexo.fge.shapes.Shape.ShapeType;
import org.openflexo.fge.view.ShapeView;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.wkf.action.OpenPortRegistery;
import org.openflexo.foundation.wkf.dm.ObjectVisibilityChanged;
import org.openflexo.foundation.wkf.dm.PortInserted;
import org.openflexo.foundation.wkf.dm.PortRemoved;
import org.openflexo.foundation.wkf.dm.WKFAttributeDataModification;
import org.openflexo.foundation.wkf.ws.PortRegistery;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.toolbox.ConcatenedList;
import org.openflexo.wkf.WKFPreferences;
import org.openflexo.wkf.swleditor.SwimmingLaneRepresentation;

public class PortRegisteryGR extends SWLObjectGR<PortRegistery> implements SWLContainerGR {

	protected Color mainColor;
	protected Color backColor;
	protected BackgroundStyle background;
	protected ForegroundStyle decorationForeground;
	protected BackgroundStyle decorationBackground;
	protected ForegroundStyle closingBoxForeground;

	protected SWLContainerControls controlsArea;

	public PortRegisteryGR(PortRegistery portRegistery, SwimmingLaneRepresentation aDrawing) {
		super(portRegistery, ShapeType.RECTANGLE, aDrawing);
		setLayer(ROLE_LAYER);
		setBorder(new ShapeGraphicalRepresentation.ShapeBorder(0, 12, 0, 0));

		updatePropertiesFromWKFPreferences();

		setDecorationPainter(new DecorationPainter() {
			@Override
			public void paintDecoration(org.openflexo.fge.graphics.FGEShapeDecorationGraphics g) {
				double arcSize = 25;
				g.useBackgroundStyle(decorationBackground);
				g.fillRoundRect(0, 0, g.getWidth() - 1, g.getHeight() - 1 + CONTAINER_LABEL_HEIGHT, arcSize, arcSize);
				g.useForegroundStyle(decorationForeground);
				g.drawRoundRect(0, 0, g.getWidth() - 1, g.getHeight() - 1 + CONTAINER_LABEL_HEIGHT, arcSize, arcSize);
				g.fillArc(0, g.getHeight() + CONTAINER_LABEL_HEIGHT - arcSize, arcSize, arcSize, 180, 90);
				g.fillArc(g.getWidth() - arcSize, g.getHeight() + CONTAINER_LABEL_HEIGHT - arcSize, arcSize, arcSize, 270, 90);
				g.fillRect(arcSize / 2, g.getHeight() - arcSize / 2 + CONTAINER_LABEL_HEIGHT, g.getWidth() - arcSize + 1, arcSize / 2);
				g.fillRect(0, g.getHeight() - arcSize / 2 - 2 + CONTAINER_LABEL_HEIGHT, g.getWidth(), 3);

				Rectangle closingBoxRect = new Rectangle(5, 5, 15, 15);
				int crossBorder = 4;

				g.useBackgroundStyle(BackgroundStyle.makeColoredBackground(Color.WHITE));
				g.fillRoundRect(closingBoxRect.x, closingBoxRect.y, closingBoxRect.width, closingBoxRect.height, 10, 10);
				g.useForegroundStyle(closingBoxForeground);
				g.drawRoundRect(closingBoxRect.x, closingBoxRect.y, closingBoxRect.width, closingBoxRect.height, 10, 10);
				g.useForegroundStyle(ForegroundStyle.makeStyle(mainColor, 2.0f, JoinStyle.JOIN_MITER, CapStyle.CAP_ROUND,
						DashStyle.PLAIN_STROKE));
				g.drawLine(closingBoxRect.x + crossBorder, closingBoxRect.y + crossBorder, closingBoxRect.x + closingBoxRect.width
						- crossBorder, closingBoxRect.y + closingBoxRect.height - crossBorder);
				g.drawLine(closingBoxRect.x + closingBoxRect.width - crossBorder, closingBoxRect.y + crossBorder, closingBoxRect.x
						+ crossBorder, closingBoxRect.y + closingBoxRect.height - crossBorder);

				g.useTextStyle(TextStyle.makeTextStyle(Color.WHITE, FGEConstants.DEFAULT_TEXT_FONT));
				g.drawString(getLabel(), g.getWidth() / 2, g.getHeight() - 9 + CONTAINER_LABEL_HEIGHT, TextAlignment.CENTER);

				/*g.drawImage(IconLibrary.TRIANGLE_UP.getImage(), new FGEPoint(controls.upRect.getX()*g.getScale(),controls.upRect.getY()*g.getScale()));
				g.drawImage(IconLibrary.TRIANGLE_DOWN.getImage(), new FGEPoint(controls.downRect.getX()*g.getScale(),controls.downRect.getY()*g.getScale()));
				g.drawImage(IconLibrary.MINUS.getImage(), new FGEPoint(controls.minusRect.getX()*g.getScale(),controls.minusRect.getY()*g.getScale()));
				g.drawImage(IconLibrary.PLUS.getImage(), new FGEPoint(controls.plusRect.getX()*g.getScale(),controls.plusRect.getY()*g.getScale()));*/
			};

			@Override
			public boolean paintBeforeShape() {
				return true;
			}
		});

		setForeground(ForegroundStyle.makeNone());
		setBackground(BackgroundStyle.makeEmptyBackground());

		portRegistery.addObserver(this);

		addToMouseDragControls(new PortRegisteryCloser(), true);

		setDimensionConstraints(DimensionConstraints.UNRESIZABLE);

		setLocationConstraints(LocationConstraints.AREA_CONSTRAINED);
		setLocationConstrainedArea(FGEHalfLine.makeHalfLine(new FGEPoint(SWIMMING_LANE_BORDER, SWIMMING_LANE_BORDER),
				SimplifiedCardinalDirection.SOUTH));

		anchorLocation();

		updateControlArea();
	}

	public PortRegistery getPortRegistery() {
		return getDrawable();
	}

	public String getLabel() {
		return FlexoLocalization.localizedForKey("port_registery");
	}

	@Override
	protected boolean supportShadow() {
		return false;
	}

	public class PortRegisteryCloser extends MouseDragControl {

		public PortRegisteryCloser() {
			super("Closer", MouseButton.LEFT, new CustomDragControlAction() {
				@Override
				public boolean handleMousePressed(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller,
						MouseEvent event) {
					logger.info("handleMousePressed");
					if (isInsideClosingBox(graphicalRepresentation, controller, event)) {
						logger.info("Closing container");
						closingRequested();
						return true;
					}
					return false;
				}

				@Override
				public boolean handleMouseReleased(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller,
						MouseEvent event, boolean isSignificativeDrag) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean handleMouseDragged(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller,
						MouseEvent event) {
					// TODO Auto-generated method stub
					return false;
				}
			}, false, false, false, false);
		}

		@Override
		public boolean isApplicable(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller, MouseEvent e) {
			// TODO Auto-generated method stub
			return super.isApplicable(graphicalRepresentation, controller, e) && isInsideClosingBox(graphicalRepresentation, controller, e);
		}

	}

	protected static boolean isInsideClosingBox(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller,
			MouseEvent event) {
		ShapeView view = (ShapeView) controller.getDrawingView().viewForObject(graphicalRepresentation);
		Rectangle closingBoxRect = new Rectangle((int) (5 * controller.getScale()), (int) (5 * controller.getScale()),
				(int) (15 * controller.getScale()), (int) (15 * controller.getScale()));
		Point clickLocation = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), view);
		return closingBoxRect.contains(clickLocation);
	}

	public void closingRequested() {
		OpenPortRegistery.actionType.makeNewAction(getPortRegistery().getProcess(), null, getDrawing().getEditor()).doAction();
		// Is now performed by receiving notification
		// getDrawing().updateGraphicalObjectsHierarchy();
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		// logger.info(">>>>>>>>>>>  Notified "+dataModification+" for "+observable);
		if (observable == getModel()) {
			if ((dataModification instanceof PortInserted) || (dataModification instanceof PortRemoved)) {
				getDrawing().updateGraphicalObjectsHierarchy();
				notifyShapeNeedsToBeRedrawn();
				notifyObjectMoved();
				notifyObjectResized();
			} else if (dataModification instanceof ObjectVisibilityChanged) {
				getDrawing().updateGraphicalObjectsHierarchy();
			} else if (dataModification instanceof WKFAttributeDataModification) {
				if (((WKFAttributeDataModification) dataModification).getAttributeName().equals(getDrawing().SWIMMING_LANE_NB_KEY())) {
					getDrawing().invalidateGraphicalObjectsHierarchy(getPortRegistery());
					getDrawing().updateGraphicalObjectsHierarchy();
					for (GraphicalRepresentation<?> gr : getDrawing().getDrawingGraphicalRepresentation()
							.getContainedGraphicalRepresentations()) {
						if (gr instanceof ShapeGraphicalRepresentation<?>) {
							((ShapeGraphicalRepresentation<?>) gr).notifyObjectHasMoved();
						}
					}
				} else if (((WKFAttributeDataModification) dataModification).getAttributeName().equals(
						getDrawing().SWIMMING_LANE_HEIGHT_KEY())) {
					getDrawing().invalidateGraphicalObjectsHierarchy(getPortRegistery());
					getDrawing().updateGraphicalObjectsHierarchy();
					for (GraphicalRepresentation<?> gr : getDrawing().getDrawingGraphicalRepresentation()
							.getContainedGraphicalRepresentations()) {
						if (gr instanceof ShapeGraphicalRepresentation<?>) {
							((ShapeGraphicalRepresentation<?>) gr).notifyObjectHasMoved();
							((ShapeGraphicalRepresentation<?>) gr).notifyShapeNeedsToBeRedrawn();
						}
					}
				} else if (((WKFAttributeDataModification) dataModification).getAttributeName().equals(
						getDrawing().SWIMMING_LANE_INDEX_KEY())) {
					getDrawing().reindexForNewObjectIndex(getPortRegistery());
				} else {
					notifyShapeNeedsToBeRedrawn();
				}
			}
		}
	}

	@Override
	public double getWidth() {
		return getDrawingGraphicalRepresentation().getWidth() - 2 * SWIMMING_LANE_BORDER;
	}

	@Override
	public double getHeight() {
		return getSwimmingLaneHeight() * getSwimmingLaneNb();
	}

	@Override
	public void updatePropertiesFromWKFPreferences() {
		super.updatePropertiesFromWKFPreferences();

		updateDecorationBackground();
		updateDecorationForeground();
	}

	protected void updateDecorationBackground() {
		backColor = OPERATION_PG_BACK_COLOR;
		if (getModel().getWorkflow().getUseTransparency(WKFPreferences.getUseTransparency())) {
			decorationBackground = BackgroundStyle.makeColorGradientBackground(backColor, Color.WHITE,
					ColorGradientDirection.SOUTH_EAST_NORTH_WEST);
			decorationBackground.setUseTransparency(true);
			decorationBackground.setTransparencyLevel(0.9f);
		} else {
			decorationBackground = BackgroundStyle.makeColoredBackground(backColor);
		}
	}

	protected void updateDecorationForeground() {
		mainColor = PORT_REGISTRY_PG_COLOR;
		decorationForeground = ForegroundStyle.makeStyle(mainColor);
		decorationForeground.setLineWidth(0.2);

		closingBoxForeground = ForegroundStyle.makeStyle(mainColor);
		closingBoxForeground.setLineWidth(0.2);
	}

	private void anchorLocation() {
		setX(SWIMMING_LANE_BORDER);
		setY(getDrawing().yForObject(getPortRegistery()));
	}

	@Override
	public int getSwimmingLaneNb() {
		return getDrawing().getSwimmingLaneNb(getPortRegistery());
	}

	@Override
	public void setSwimmingLaneNb(int swlNb) {
		getDrawing().setSwimmingLaneNb(swlNb, getPortRegistery());
	}

	@Override
	public int getSwimmingLaneHeight() {
		return getDrawing().getSwimmingLaneHeight(getPortRegistery());
	}

	@Override
	public void setSwimmingLaneHeight(int height) {
		getDrawing().setSwimmingLaneHeight(height, getPortRegistery());
	}

	private boolean objectIsBeeingDragged = false;

	@Override
	public void notifyObjectWillMove() {
		super.notifyObjectWillMove();
		objectIsBeeingDragged = true;
	}

	@Override
	public void notifyObjectHasMoved() {
		if (objectIsBeeingDragged) {
			getDrawing().reindexObjectForNewVerticalLocation(getPortRegistery(), getY());
			anchorLocation();
			for (GraphicalRepresentation<?> gr : getDrawingGraphicalRepresentation().getContainedGraphicalRepresentations()) {
				if (gr instanceof ShapeGraphicalRepresentation && gr != this) {
					((ShapeGraphicalRepresentation<?>) gr).notifyObjectHasMoved();
				}
			}
		}
		objectIsBeeingDragged = false;
		super.notifyObjectHasMoved();
		anchorLocation();
	}

	@Override
	public FGEArea getLocationConstrainedAreaForChild(AbstractNodeGR node) {
		Vector<FGESegment> lines = new Vector<FGESegment>();
		for (int i = 0; i < getSwimmingLaneNb(); i++) {
			double x1 = SWIMMING_LANE_BORDER - node.getBorder().left;
			double x2 = getWidth() - SWIMMING_LANE_BORDER - node.getWidth() - node.getBorder().left;
			double y = i * getHeight() / getSwimmingLaneNb() + getHeight() / getSwimmingLaneNb() / 2 - node.getHeight() / 2
					- node.getBorder().top;
			lines.add(new FGESegment(x1, y, x2, y));
		}
		return FGEUnionArea.makeUnion(lines);
	}

	@Override
	public void notifyObjectHasResized() {
		for (GraphicalRepresentation gr : getContainedGraphicalRepresentations()) {
			if (gr instanceof AbstractNodeGR) {
				((AbstractNodeGR) gr).resetLocationConstrainedArea();
			}
		}
		super.notifyObjectHasResized();
		updateControlArea();
	}

	@Override
	public List<? extends ControlArea> getControlAreas() {
		return concatenedList;
	}

	private FGEArea lanes;
	private ControlArea lanesArea;
	private ConcatenedList<ControlArea> concatenedList;

	private void updateControlArea() {
		Vector<FGESegment> lines = new Vector<FGESegment>();
		for (int i = 0; i < getSwimmingLaneNb(); i++) {
			double y = i / (double) getSwimmingLaneNb() + 1 / (double) getSwimmingLaneNb() / 2;
			lines.add(new FGESegment(0, y, 1, y));
		}
		lanes = FGEUnionArea.makeUnion(lines);
		lanesArea = new ControlArea<FGEArea>(this, lanes) {
			@Override
			public Cursor getDraggingCursor() {
				return Cursor.getDefaultCursor();
			}

			@Override
			public boolean isDraggable() {
				return false;
			}

			@Override
			public Rectangle paint(FGEGraphics drawingGraphics) {
				Graphics2D oldGraphics = drawingGraphics.cloneGraphics();
				drawingGraphics.setDefaultForeground(ForegroundStyle.makeStyle(Color.LIGHT_GRAY, 0.4f, DashStyle.BIG_DASHES));
				AffineTransform at = GraphicalRepresentation.convertNormalizedCoordinatesAT(PortRegisteryGR.this,
						drawingGraphics.getGraphicalRepresentation());
				getArea().transform(at).paint(drawingGraphics);
				drawingGraphics.releaseClonedGraphics(oldGraphics);
				return null;
			}
		};
		controlsArea = new SWLContainerControls(this);
		concatenedList = new ConcatenedList<ControlArea>();
		concatenedList.addElementList(getShape().getControlPoints());
		concatenedList.addElement(lanesArea);
		concatenedList.addElement(controlsArea);
	}

}
