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
package org.openflexo.sgmodule.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.FlexoObserver;
import org.openflexo.foundation.action.FlexoActionSource;
import org.openflexo.foundation.cg.GeneratedOutput;
import org.openflexo.foundation.sg.CreateSourceRepository;
import org.openflexo.foundation.sg.implmodel.CreateImplementationModel;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.sgmodule.controller.SGController;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.ModuleView;
import org.openflexo.view.listener.FlexoActionButton;

/**
 * @author sylvain
 */
public class GeneratedSourcesModuleView extends JPanel implements ModuleView<GeneratedOutput>, FlexoObserver, FlexoActionSource {
	private GeneratedOutput _gc;
	private SGController _controller;

	private JComponent component;
	private JPanel panel;

	public GeneratedSourcesModuleView(GeneratedOutput gc, SGController controller) {
		super(new BorderLayout());
		_gc = gc;
		_controller = controller;
		_gc.addObserver(this);
		panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 50));
		add(panel);
		validate();
		updateView();
	}

	private void updateView() {
		if (component != null) {
			panel.removeAll();
		}
		if (_gc.getGeneratedRepositories().size() == 0) {
			panel.add(component = new FlexoActionButton(CreateSourceRepository.actionType, this, _controller.getEditor()));
			panel.add(component = new FlexoActionButton(CreateImplementationModel.actionType, this, _controller.getEditor()));
		} else {
			panel.add(component = new JLabel(FlexoLocalization.localizedForKey("please_select_a_repository"), SwingConstants.CENTER));
		}
		panel.validate();
		panel.repaint();
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		updateView();
	}

	@Override
	public void deleteModuleView() {
		_controller.removeModuleView(this);
		component = null;
		panel = null;
	}

	@Override
	public FlexoPerspective<FlexoModelObject> getPerspective() {
		return _controller.CODE_GENERATION_PERSPECTIVE;
	}

	@Override
	public GeneratedOutput getRepresentedObject() {
		return _gc;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
	}

	/**
	 * Returns flag indicating if this view is itself responsible for scroll management When not, Flexo will manage it's own scrollbar for
	 * you
	 * 
	 * @return
	 */
	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	/**
	 * Overrides getEditor
	 * 
	 * @see org.openflexo.foundation.action.FlexoActionSource#getEditor()
	 */
	@Override
	public FlexoEditor getEditor() {
		return _controller.getEditor();
	}

	/**
	 * Overrides getFocusedObject
	 * 
	 * @see org.openflexo.foundation.action.FlexoActionSource#getFocusedObject()
	 */
	@Override
	public GeneratedOutput getFocusedObject() {
		return _gc;
	}

	/**
	 * Overrides getGlobalSelection
	 * 
	 * @see org.openflexo.foundation.action.FlexoActionSource#getGlobalSelection()
	 */
	@Override
	public Vector getGlobalSelection() {
		return null;
	}

}
