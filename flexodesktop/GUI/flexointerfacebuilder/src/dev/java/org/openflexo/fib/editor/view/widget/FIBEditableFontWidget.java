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
package org.openflexo.fib.editor.view.widget;

import java.util.Observable;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fib.editor.controller.FIBEditorController;
import org.openflexo.fib.editor.view.FIBEditableView;
import org.openflexo.fib.editor.view.FIBEditableViewDelegate;
import org.openflexo.fib.editor.view.PlaceHolder;
import org.openflexo.fib.model.FIBFont;
import org.openflexo.fib.model.FIBModelNotification;
import org.openflexo.fib.view.widget.FIBFontWidget;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.swing.FontSelector;

public class FIBEditableFontWidget extends FIBFontWidget implements FIBEditableView<FIBFont,FontSelector> {

	private static final Logger logger = FlexoLogger.getLogger(FIBEditableFontWidget.class.getPackage().getName());

	private final FIBEditableViewDelegate<FIBFont,FontSelector> delegate;
	
	private final FIBEditorController editorController;
	
	@Override
	public FIBEditorController getEditorController() 
	{
		return editorController;
	}
	
	public FIBEditableFontWidget(FIBFont model, FIBEditorController editorController)
	{
		super(model,editorController.getController());
		this.editorController = editorController;
		
		delegate = new FIBEditableViewDelegate<FIBFont,FontSelector>(this);
		model.addObserver(this);
	}
	
	
	@Override
	public void delete() 
	{
		delegate.delete();
		getComponent().deleteObserver(this);
		super.delete();
	}	
	
	public Vector<PlaceHolder> getPlaceHolders() 
	{
		return null;
	}
	
	public FIBEditableViewDelegate<FIBFont,FontSelector> getDelegate()
	{
		return delegate;
	}

	@Override
	public void update(Observable o, Object dataModification) 
	{
		if (dataModification instanceof FIBModelNotification) {
			delegate.receivedModelNotifications(o, (FIBModelNotification)dataModification);
		}		
	}

}
