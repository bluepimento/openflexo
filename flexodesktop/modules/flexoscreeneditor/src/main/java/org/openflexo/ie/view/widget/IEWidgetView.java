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
package org.openflexo.ie.view.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.openflexo.ColorCst;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.DeletableObject;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.GraphicalFlexoObserver;
import org.openflexo.foundation.ie.IEObject;
import org.openflexo.foundation.ie.IEWOComponent;
import org.openflexo.foundation.ie.cl.FlexoComponentLibrary;
import org.openflexo.foundation.ie.dm.DisplayNeedsRefresh;
import org.openflexo.foundation.ie.util.FlexoConceptualColor;
import org.openflexo.foundation.ie.widget.IEWidget;
import org.openflexo.foundation.utils.FlexoCSS;
import org.openflexo.ie.view.IESelectable;
import org.openflexo.ie.view.IEWOComponentView;
import org.openflexo.ie.view.Layoutable;
import org.openflexo.ie.view.controller.IEController;
import org.openflexo.utils.DrawUtils;

/**
 * Abstract parent class for all IE widget At this level are managed following features:
 * <ul>
 * <li>Selection and Focus</li>
 * <li>Represented object retrieving</li>
 * <li>Inspection of represented object</li>
 * </ul>
 * 
 * @author bmangez, sguerin
 */
public abstract class IEWidgetView<T extends IEWidget> extends IEInnerDSWidgetView implements
/* InspectableObjectView, */GraphicalFlexoObserver, IESelectable, Layoutable {

	private static final Logger logger = Logger.getLogger(IEWidgetView.class.getPackage().getName());

	public static final String ATTRIB_DESCRIPTION_NAME = "description";

	public static final String BINDING_VALUE_NAME = "value";

	public static final String BINDING_TOOLTIP_NAME = "tooltip";

	protected static final Border EMPTY_BORDER_1 = BorderFactory.createEmptyBorder(1, 1, 1, 1);

	// ==========================================================================
	// ============================= Variables
	// ==================================
	// ==========================================================================

	private boolean _isSelected = false;

	private boolean _isFocused = false;

	private T _model;

	protected IEWOComponentView _componentView;

	// protected boolean holdsNextComputedPreferredSize = false;

	// protected Dimension preferredSize = null;

	// ==========================================================================
	// ============================= Constructor
	// ================================
	// ==========================================================================

	public IEWidgetView(IEController ieController, T model, boolean addDnDSupport, IEWOComponentView componentView) {
		super(ieController, model, addDnDSupport);
		_componentView = componentView;
		_componentView.registerViewForWidget(model, this);
		_model = model;
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Build new " + getClass().getName());
		}
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Add " + getClass().getName() + " to pending views");
		}
		model.addObserver(this);
		updateTooltip();
		setIsSelected(false);
		setIsFocused(false);
		addMouseListener(ieController.getIESelectionManager());
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				getIEController().getIESelectionManager().processMouseMoved(e);
			}
		});
	}

	private String getTooltipFromModel() {
		return getModel().getTooltip();
	}

	private void updateTooltip() {
		if (getTooltipFromModel() != null && getTooltipFromModel().trim().length() > 0) {
			if (getTooltipFromModel().length() <= 100) {
				setToolTipText(getTooltipFromModel());
			} else {
				if (getTooltipFromModel().indexOf(' ', 100) > -1) {
					setToolTipText(getTooltipFromModel().substring(0, getTooltipFromModel().indexOf(' ', 100)) + "...");
				} else {
					setToolTipText(getTooltipFromModel().substring(0, 100) + "...");
				}
			}
		} else {
			setToolTipText(null);
		}
	}

	@Override
	public IEWidgetView findViewForModel(IEObject object) {
		return _componentView.findViewForModel(object);
	}

	public String getCSSName() {
		return getModel().getCSSName();
	}

	public FlexoCSS getFlexoCSS() {
		return getModel().getFlexoCSS();
	}

	public Color getMainColor() {
		return colorFromConceptualColor(FlexoConceptualColor.MAIN_COLOR, getFlexoCSS());
	}

	public Color getTextColor() {
		return colorFromConceptualColor(FlexoConceptualColor.TEXT_COLOR, getFlexoCSS());
	}

	/**
	 * Overrides doLayout
	 * 
	 * @see java.awt.Container#doLayout()
	 */
	@Override
	public void doLayout() {
		_componentView.notifyAllViewsToHoldTheirNextComputedPreferredSize(this);
		if (getParent() instanceof Layoutable) {
			((Layoutable) getParent()).doLayout();
		}
		super.doLayout();
		_componentView.resetAllViewsPreferredSize(this);
	}

	/**
	 * Overrides propagateResize
	 * 
	 * @see org.openflexo.ie.view.Layoutable#propagateResize()
	 */
	@Override
	public void propagateResize() {
		// Does nothing by default
	}

	public void delete() {
		_model.deleteObserver(this);
		if (getParent() != null) {
			getParent().remove(this);
		}
		Component[] comp = getComponents();
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof IEWidgetView) {
				((IEWidgetView) comp[i]).delete();
			}
		}
		_componentView.removeFrowWidgetViews(getModel());
		removeAll();
	}

	public void setDefaultBorder() {
		if (getModel().getIsRootOfPartialComponent() && getParent() != null) {
			((JPanel) getParent()).setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
		} else if (getParent() != null && getParent() instanceof IETDWidgetView) {
			setBorder(null);
		} else {
			setBorder(EMPTY_BORDER_1);
		}
	}

	// ==========================================================================
	// ======================= IESelectable ============================
	// ==========================================================================

	/**
	 * Return boolean indicating if related object is selected
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isSelected() {
		return _isSelected;
	}

	@Override
	public void setIsSelected(boolean b) {
		_isSelected = b;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("setIsSelected=" + b + " dans " + getClass().getName());
		}
		repaint();
	}

	/**
	 * Sets related object to be focused or not
	 */
	@Override
	public void setIsFocused(boolean b) {
		_isFocused = b;
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("setIsFocused=" + b + " dans " + getClass().getName());
		}
		if (b) {
			setBorder(BorderFactory.createLineBorder(ColorCst.BORDER_COLOR_FOR_FOCUSED_WIDGET));
		} else {
			setDefaultBorder();
		}
		repaint();
	}

	/**
	 * Return boolean indicating if related object is focused
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isFocused() {
		return _isFocused;
	}

	public IEWidget getSelectedObject() {
		return _model;
	}

	@Override
	public FlexoModelObject getObject() {
		return getSelectedObject();
	}

	// ==========================================================================
	// ======================= InspectableObjectView
	// ============================
	// ==========================================================================

	@Override
	public DeletableObject getDeletableObject() {
		return _model;
	}

	// ==========================================================================
	// ============================= Accessors
	// ==================================
	// ==========================================================================

	public T getModel() {
		return _model;
	}

	public final IEWidget getContainerModel() {
		return getModel();
	}

	public final IEWOComponent getWOComponent() {
		return _model.getWOComponent();
	}

	protected void switchToModel(T model) {
		_model.deleteObserver(this);
		_model = model;
		model.addObserver(this);
	}

	public IEWidget getIEModel() {
		return getModel();
	}

	public Point getCenter() {
		return new Point(getWidth() / 2, getHeight() / 2);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (_isSelected) {
			paintSelection(g);
		}
	}

	public void paintSelection(Graphics g) {
		if (logger.isLoggable(Level.FINE)) {
			logger.finer("Drawing selection");
		}
		Graphics2D g2 = (Graphics2D) g;
		DrawUtils.turnOnAntiAlising(g2);
		DrawUtils.setRenderQuality(g2);
		DrawUtils.setColorRenderQuality(g2);
		g2.setColor(Color.BLUE);
		Dimension panelDim = getSize();
		g2.fillRect(0, 0, 5, 5);
		g2.fillRect(panelDim.width - 5, 0, 5, 5);
		g2.fillRect(0, panelDim.height - 5, 5, 5);
		g2.fillRect(panelDim.width - 5, panelDim.height - 5, 5, 5);
	}

	@Override
	public String toString() {
		return getClass().getName() + "/" + hashCode() + " view for model " + getModel();
	}

	public FlexoComponentLibrary getFlexoComponentLibrary() {
		return getIEController().getProject().getFlexoComponentLibrary();
	}

	public void updateConstraints() {
		Container c = getParent();
		IESequenceTRWidgetView tr = null;
		while (c != null) {
			if (c instanceof IESequenceTRWidgetView) {
				tr = (IESequenceTRWidgetView) c;
			}
			c = c.getParent();
		}
		if (tr != null) {
			(tr).updateConstraints();
			tr.doLayout();
			tr.repaint();
		}
	}

	/**
	 * Overrides update
	 * 
	 * @see org.openflexo.foundation.FlexoObserver#update(org.openflexo.foundation.FlexoObservable,
	 *      org.openflexo.foundation.DataModification)
	 */
	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		if (observable == getModel() && dataModification instanceof DisplayNeedsRefresh) {
			doLayout();
			repaint();
		} else if (observable == getModel() && dataModification.propertyName() != null && dataModification.propertyName().equals("tooltip")) {
			updateTooltip();
		}
	}

	@Override
	public boolean getHoldsNextComputedPreferredSize() {
		return _componentView.holdsNextComputedPreferredSize;
		// return holdsNextComputedPreferredSize;
	}

	@Override
	public void setHoldsNextComputedPreferredSize() {
		// holdsNextComputedPreferredSize = true;
	}

	@Override
	public void resetPreferredSize() {
		// preferredSize = null;
		// holdsNextComputedPreferredSize = false;
	}

	public void storePrefSize(Dimension value) {
		_componentView.storePrefSize(this, value);
	}

	public Dimension storedPrefSize() {
		return _componentView.storedPrefSize(this);
	}
}
