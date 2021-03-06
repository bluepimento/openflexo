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
package org.openflexo.fib.editor.view.container;

import java.util.Observable;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import org.openflexo.fib.editor.controller.FIBEditorController;
import org.openflexo.fib.editor.view.FIBEditableView;
import org.openflexo.fib.editor.view.FIBEditableViewDelegate;
import org.openflexo.fib.editor.view.PlaceHolder;
import org.openflexo.fib.model.FIBAddingNotification;
import org.openflexo.fib.model.FIBModelNotification;
import org.openflexo.fib.model.FIBRemovingNotification;
import org.openflexo.fib.model.FIBTab;
import org.openflexo.fib.model.FIBTabPanel;
import org.openflexo.fib.view.container.FIBTabPanelView;
import org.openflexo.logging.FlexoLogger;

public class FIBEditableTabPanelView extends FIBTabPanelView implements FIBEditableView<FIBTabPanel,JTabbedPane> {

	private static final Logger logger = FlexoLogger.getLogger(FIBEditableTabPanelView.class.getPackage().getName());

	private FIBEditableViewDelegate<FIBTabPanel,JTabbedPane> delegate;
	
	private Vector<PlaceHolder> placeholders;
	
	private FIBEditorController editorController;
	
	@Override
	public FIBEditorController getEditorController() 
	{
		return editorController;
	}
	
	public FIBEditableTabPanelView(FIBTabPanel model, FIBEditorController editorController)
	{
		super(model,editorController.getController());
		this.editorController = editorController;
	
		delegate = new FIBEditableViewDelegate<FIBTabPanel,JTabbedPane>(this);
		placeholders = new Vector<PlaceHolder>();
		model.addObserver(this);
	}
	
	
	public void delete() 
	{
		placeholders.clear();
		placeholders = null;
		delegate.delete();
		getComponent().deleteObserver(this);
		super.delete();
	}

	public Vector<PlaceHolder> getPlaceHolders() 
	{
		return placeholders;
	}
	
	public FIBEditableViewDelegate<FIBTabPanel,JTabbedPane> getDelegate()
	{
		return delegate;
	}

	public void update(Observable o, Object dataModification) 
	{
		// Tab added
		if (dataModification instanceof FIBAddingNotification) {
			if (((FIBAddingNotification)dataModification).getAddedValue() instanceof FIBTab) {
				updateLayout();
			}
		}
		// Tab removed
		else if (dataModification instanceof FIBRemovingNotification) {
			if (((FIBRemovingNotification)dataModification).getRemovedValue() instanceof FIBTab) {
				updateLayout();
			}
		}
		if (dataModification instanceof FIBModelNotification) {
			delegate.receivedModelNotifications(o, (FIBModelNotification)dataModification);
		}		
	}

}
