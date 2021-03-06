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

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.ie.IEObject;
import org.openflexo.foundation.ie.TabComponentInstance;
import org.openflexo.foundation.ie.cl.TabComponentDefinition;
import org.openflexo.foundation.ie.dm.TopComponentInserted;
import org.openflexo.foundation.ie.widget.ContentSizeChanged;
import org.openflexo.foundation.ie.widget.IETabWidget;
import org.openflexo.ie.view.IEWOComponentView;
import org.openflexo.ie.view.controller.IEController;

/**
 * @author bmangez
 * 
 */
public class IETabWidgetView extends IEReusableWidgetView<IETabWidget, TabComponentInstance, TabComponentDefinition> {

	private static final Logger logger = Logger.getLogger(IEWidgetView.class.getPackage().getName());

	private boolean tabVisibility = false;

	/**
	 * @param model
	 */
	public IETabWidgetView(IEController ieController, IETabWidget model, IEWOComponentView view) {
		super(ieController, model, false, view);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("new IETabWidgetView:" + model.toString() + " " + model.getTabKeyForGenerator());
		}
	}

	/**
	 * Overrides delete
	 * 
	 * @see org.openflexo.ie.view.widget.IEWidgetView#delete()
	 */
	@Override
	public void delete() {
		getModel().getTabComponent().getRootSequence().deleteObserver(this);
		super.delete();
	}

	/**
	 * Overrides doLayout
	 * 
	 * @see org.openflexo.ie.view.widget.IEWidgetView#doLayout()
	 */
	@Override
	public void doLayout() {
		if (!getTabVisibility()) {
			return;
		}
		super.doLayout();
	}

	/**
	 * Overrides propagateResize
	 * 
	 * @see org.openflexo.ie.view.widget.IEWidgetView#propagateResize()
	 */
	@Override
	public void propagateResize() {
		if (!getTabVisibility()) {
			return;
		}
		super.propagateResize();
	}

	@Override
	public Dimension getPreferredSize() {
		if (getHoldsNextComputedPreferredSize()) {
			Dimension storedSize = storedPrefSize();
			if (storedSize != null) {
				return storedSize;
			}
		}
		if (!getTabVisibility()) {
			return new Dimension(0, 0);
		}
		Dimension d = getReusableWidgetComponentView().getPreferredSize();
		if (getHoldsNextComputedPreferredSize()) {
			storePrefSize(d);
		}
		return d;
	}

	public IETabWidget getTabWidget() {
		return getModel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(FlexoObservable arg0, DataModification modif) {
		if (!getTabVisibility()) {
			return;
		}
		if (modif.modificationType() == DataModification.ATTRIBUTE && modif.propertyName().equals("title") && arg0 == getTabWidget()) {
			setName(getTabWidget().getTitle());
			if (getParent() != null) {
				((JTabbedPane) getParent()).setTitleAt(getTabWidget().getRootParent() == getTabWidget().getParent() ? getTabWidget()
						.getIndex() : getTabWidget().getParent().getIndex(), getTabWidget().getTitle());
			}
		} else if (modif instanceof ContentSizeChanged) {
			getReusableWidgetComponentView().validate();
			getReusableWidgetComponentView().doLayout();
			getReusableWidgetComponentView().repaint();
			doLayout();
			repaint();
			if (getParent() != null) {
				getParent().validate();
				getParent().doLayout();
				getParent().repaint();
			}
		} else if (modif instanceof TopComponentInserted) {
			((IEObject) (((TopComponentInserted) modif).newValue())).addObserver(this);
			getReusableWidgetComponentView().validate();
			getReusableWidgetComponentView().doLayout();
			getReusableWidgetComponentView().repaint();
			doLayout();
			repaint();
			if (getParent() != null) {
				getParent().validate();
				getParent().doLayout();
				getParent().repaint();
			}
		} else {
			super.update(arg0, modif);
		}
	}

	public boolean getTabVisibility() {
		return tabVisibility;
	}

	public void setTabVisibility(boolean isVisible) {
		this.tabVisibility = isVisible;
		if (isVisible) {
			validate();
			doLayout();
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Building tab: " + getTabWidget().getTitle());
			}
			getModel().getTabComponent().getRootSequence().addObserver(this);
		}
		if (!isVisible) {
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Hiding tab: " + getTabWidget().getTitle());
			}
		}
	}

}
