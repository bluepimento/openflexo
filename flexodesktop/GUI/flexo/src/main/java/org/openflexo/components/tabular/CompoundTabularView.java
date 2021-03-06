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
package org.openflexo.components.tabular;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.openflexo.ch.FCH;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.selection.SelectionListener;
import org.openflexo.selection.SelectionManager;
import org.openflexo.view.controller.SelectionManagingController;

/**
 * A compound tabular view represents a logical group of tabular views with a specified layout (for example on a JSplitPane), with
 * master/slave relationships.
 * 
 * @author sguerin
 * 
 */
public abstract class CompoundTabularView<O extends FlexoModelObject> extends JPanel implements SelectionListener {

	static final Logger logger = Logger.getLogger(CompoundTabularView.class.getPackage().getName());

	private SelectionManagingController _controller;

	private O _object;

	private JPanel controlPanel;

	private String title;

	private JComponent _contentPane;

	private Vector<TabularView> _masterTabularViews;
	private Vector<TabularView> _slaveTabularViews;

	/**
	 * Stores controls: key is the JButton and value the FlexoActionType
	 */
	protected Hashtable<JButton, TabularViewAction> _controls;

	private JLabel titleLabel;

	public CompoundTabularView(O object, SelectionManagingController controller, String t) {
		super();
		_controller = controller;
		_object = object;
		this.title = t;
		_controls = new Hashtable<JButton, TabularViewAction>();
		setLayout(new BorderLayout());
		// controller.getSelectionManager().addToSelectionListeners(this);
		_masterTabularViews = new Vector<TabularView>();
		_slaveTabularViews = new Vector<TabularView>();
	}

	public SelectionManagingController getController() {
		return _controller;
	}

	public SelectionManager getSelectionManager() {
		if (getController() != null) {
			return getController().getSelectionManager();
		}
		return null;
	}

	public O getModelObject() {
		return _object;
	}

	protected JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout());
		}
		return controlPanel;
	}

	protected void addAction(TabularViewAction action) {
		JButton newButton = new JButton();
		newButton.setText(action.getLocalizedName(newButton));
		newButton.setIcon(action.getIcon());
		newButton.setDisabledIcon(action.getDisabledIcon());
		newButton.addActionListener(action);
		FCH.setHelpItem(newButton, action.getActionType().getUnlocalizedName());
		getControlPanel().add(newButton);
		_controls.put(newButton, action);
		updateControls();
	}

	protected void finalizeBuilding() {
		add(getTitlePanel(), BorderLayout.NORTH);
		add(getContentPane(), BorderLayout.CENTER);
		add(getControlPanel(), BorderLayout.SOUTH);
		validate();
	}

	protected JPanel getTitlePanel() {
		titleLabel = new JLabel(FlexoLocalization.localizedForKeyWithParams(title, getModelObject()), SwingConstants.CENTER);
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return titlePanel;
	}

	protected void updateTitlePanel() {
		titleLabel.setText(FlexoLocalization.localizedForKeyWithParams(title, getModelObject()));
	}

	protected JComponent getContentPane() {
		if (_contentPane == null) {
			_contentPane = buildContentPane();
		}
		return _contentPane;
	}

	protected JComponent buildContentPane() {
		JLabel niLabel = new JLabel(FlexoLocalization.localizedForKey("not_implemented_yet"), SwingConstants.CENTER);
		JPanel niPanel = new JPanel();
		niPanel.setLayout(new BorderLayout());
		niPanel.add(niLabel, BorderLayout.CENTER);
		niPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return niPanel;
	}

	/**
	 * Returns the list of all master tabular views
	 * 
	 * @return a Vector of TabularView instances
	 */
	public Vector<TabularView> getMasterTabularViews() {
		return _masterTabularViews;
	}

	/**
	 * Returns the list of all slave tabular views
	 * 
	 * @return a Vector of TabularView instances
	 */
	public Vector getSlaveTabularViews() {
		return _slaveTabularViews;
	}

	public void addToMasterTabularView(TabularView aMasterTabularView) {
		_masterTabularViews.add(aMasterTabularView);
	}

	public void addToSlaveTabularView(TabularView aSlaveTabularView, TabularView aMasterTabularView) {
		_slaveTabularViews.add(aSlaveTabularView);
		aMasterTabularView.addToSlaveTabularViews(aSlaveTabularView);
	}

	/**
	 * Adds specified object to selection
	 * 
	 * @param object
	 */
	@Override
	public void fireObjectSelected(FlexoModelObject object) {
		// addToSelection() received
		// logger.info ("addToSelection() "+object+" in "+this);
		// _focusedObject = object;
		for (Enumeration en = _masterTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireObjectSelected(object);
		}
		for (Enumeration en = _slaveTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireObjectSelected(object);
		}
		updateControls();
	}

	/**
	 * Removes specified object from selection
	 * 
	 * @param object
	 */
	@Override
	public void fireObjectDeselected(FlexoModelObject object) {
		// removeFromSelection() received
		for (Enumeration en = _masterTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireObjectDeselected(object);
		}
		for (Enumeration en = _slaveTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireObjectDeselected(object);
		}
		updateControls();
	}

	/**
	 * Clear selection
	 */
	@Override
	public void fireResetSelection() {
		// resetSelection() received
		// _focusedObject = null;
		for (Enumeration en = _masterTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireResetSelection();
		}
		for (Enumeration en = _slaveTabularViews.elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			next.fireResetSelection();
		}
		updateControls();
	}

	/**
	 * Notify that the selection manager is performing a multiple selection
	 */
	@Override
	public void fireBeginMultipleSelection() {
	}

	/**
	 * Notify that the selection manager has finished to perform a multiple selection
	 */
	@Override
	public void fireEndMultipleSelection() {
	}

	public void updateControls() {
		for (Enumeration en = _controls.keys(); en.hasMoreElements();) {
			JButton button = (JButton) en.nextElement();
			Object obj = _controls.get(button);
			if (obj instanceof TabularViewAction) {
				TabularViewAction action = (TabularViewAction) obj;
				FlexoActionType actionType = action.getActionType();
				try {
					button.setEnabled(actionType.isEnabled(action.getFocusedObject(), action.getGlobalSelection(), getController()
							.getEditor()));
				} catch (NullPointerException e) {
					button.setEnabled(false);
				}
			}
		}
	}

	public boolean represents(FlexoModelObject anObject) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("represents() " + this + " obj: " + anObject);
		}
		if (anObject == null) {
			return false;
		}
		for (Enumeration en = getMasterTabularViews().elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			if (next.getModel().indexOf(anObject) > -1) {
				return true;
			}
		}
		for (Enumeration en = getSlaveTabularViews().elements(); en.hasMoreElements();) {
			TabularView next = (TabularView) en.nextElement();
			if (next.getModel().indexOf(anObject) > -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all the objects currently placed in the SelectionManager which are represented (and thus, selected) in this view
	 * 
	 * Return a Vector of FlexoModelObject
	 * 
	 * @return
	 */
	public Vector getViewSelection() {
		// logger.info("View selection, current selection is "+getSelectionManager().getObjectSelection());
		Vector returned = new Vector(getSelectionManager().getSelection());
		for (Enumeration en = getSelectionManager().getSelection().elements(); en.hasMoreElements();) {
			FlexoModelObject next = (FlexoModelObject) en.nextElement();
			if (!represents(next)) {
				// logger.info("exclude "+next+" from view selection");
				returned.remove(next);
			}
		}
		// logger.info("View selection, returned selection is "+returned);
		return returned;

	}

}
