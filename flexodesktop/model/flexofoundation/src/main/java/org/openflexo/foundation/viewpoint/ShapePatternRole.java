package org.openflexo.foundation.viewpoint;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.view.ViewShape;
import org.openflexo.localization.FlexoLocalization;

public class ShapePatternRole extends GraphicalElementPatternRole {

	// We dont want to import graphical engine in foundation
	// But you can assert graphical representation is a org.openflexo.fge.ShapeGraphicalRepresentation.
	private Object _graphicalRepresentation;

	private ShapePatternRole parentShapePatternRole;

	@Override
	public PatternRoleType getType() {
		return PatternRoleType.Shape;
	}

	@Override
	public String getPreciseType() {
		return FlexoLocalization.localizedForKey("shape");
	}

	@Override
	public Object getGraphicalRepresentation() {
		return _graphicalRepresentation;
	}

	@Override
	public void setGraphicalRepresentation(Object graphicalRepresentation) {
		_graphicalRepresentation = graphicalRepresentation;
		setChanged();
		notifyObservers(new GraphicalRepresentationChanged(this, graphicalRepresentation));
	}

	public void updateGraphicalRepresentation(Object graphicalRepresentation) {
		if (_graphicalRepresentation != null) {
			((ShapeGraphicalRepresentation) _graphicalRepresentation).setsWith((ShapeGraphicalRepresentation) graphicalRepresentation);
			setChanged();
			notifyObservers(new GraphicalRepresentationModified(this, graphicalRepresentation));
		} else {
			setGraphicalRepresentation(graphicalRepresentation);
		}
	}

	// No notification
	@Override
	public void _setGraphicalRepresentationNoNotification(Object graphicalRepresentation) {
		_graphicalRepresentation = graphicalRepresentation;
	}

	public void tryToFindAGR() {
		if (getGraphicalRepresentation() == null) {
			// Try to find one somewhere
			for (ViewPointPalette palette : getCalc().getPalettes()) {
				for (ViewPointPaletteElement e : palette.getElements()) {
					if (e.getEditionPattern() == getEditionPattern()) {
						setGraphicalRepresentation(e.getGraphicalRepresentation());
					}
				}
			}
		}
	}

	@Override
	public Class<?> getAccessedClass() {
		return ViewShape.class;
	}

	public ShapePatternRole getParentShapePatternRole() {
		return parentShapePatternRole;
	}

	public void setParentShapePatternRole(ShapePatternRole parentShapePatternRole) {
		this.parentShapePatternRole = parentShapePatternRole;
		setChanged();
		notifyObservers();
	}

	public boolean getParentShapeAsDefinedInAction() {
		return getParentShapePatternRole() == null;
	}

	public void setParentShapeAsDefinedInAction(boolean flag) {
		if (!flag && getEditionPattern().getShapePatternRoles().size() > 0) {
			setParentShapePatternRole(getEditionPattern().getShapePatternRoles().get(0));
		} else {
			System.out.println("setParentShapePatternRole with null");
			setParentShapePatternRole(null);
		}
	}

	@Override
	public boolean isIncludedInPrimaryRepresentationRole() {
		if (getParentShapePatternRole() != null) {
			return getParentShapePatternRole().isIncludedInPrimaryRepresentationRole();
		}
		return super.isIncludedInPrimaryRepresentationRole();
	}

}
