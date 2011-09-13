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
package org.openflexo.foundation.ontology.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.InvalidParametersException;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.ontology.EditionPatternInstance;
import org.openflexo.foundation.ontology.calc.AddConnector;
import org.openflexo.foundation.ontology.calc.EditionScheme;
import org.openflexo.foundation.ontology.calc.LinkScheme;
import org.openflexo.foundation.ontology.shema.OEConnector;
import org.openflexo.foundation.ontology.shema.OEShape;
import org.openflexo.foundation.ontology.shema.OEShema;
import org.openflexo.foundation.ontology.shema.OEShemaObject;
import org.openflexo.foundation.rm.DuplicateResourceException;


public class LinkSchemeAction extends EditionSchemeAction<LinkSchemeAction> 
{

	private static final Logger logger = Logger.getLogger(LinkSchemeAction.class.getPackage().getName());

	public static FlexoActionType<LinkSchemeAction,FlexoModelObject,FlexoModelObject> actionType 
	= new FlexoActionType<LinkSchemeAction,FlexoModelObject,FlexoModelObject> (
			"link_palette_connector",
			FlexoActionType.newMenu,
			FlexoActionType.defaultGroup,
			FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public LinkSchemeAction makeNewAction(FlexoModelObject focusedObject, Vector<FlexoModelObject> globalSelection, FlexoEditor editor) 
		{
			return new LinkSchemeAction(focusedObject, globalSelection, editor);
		}

		@Override
		protected boolean isVisibleForSelection(FlexoModelObject object, Vector<FlexoModelObject> globalSelection) 
		{
			return false;
		}

		@Override
		protected boolean isEnabledForSelection(FlexoModelObject object, Vector<FlexoModelObject> globalSelection) 
		{
			return (object instanceof OEShemaObject);
		}

	};

	private OEShape _fromShape;
	private OEShape _toShape;
	private OEConnector _newConnector;

	private LinkScheme _linkScheme;
	
	private Object _graphicalRepresentation;

	public boolean escapeParameterRetrievingWhenValid = false;

	LinkSchemeAction (FlexoModelObject focusedObject, Vector<FlexoModelObject> globalSelection, FlexoEditor editor)
	{
		super(actionType, focusedObject, globalSelection, editor);
	}

	//private Hashtable<EditionAction,FlexoModelObject> createdObjects;

	public LinkScheme getLinkScheme() 
	{
		return _linkScheme;
	}

	public void setLinkScheme(LinkScheme linkScheme) 
	{
		_linkScheme = linkScheme;
	}
	
	private EditionPatternInstance editionPatternInstance;
	
	@Override
	protected void doAction(Object context) throws DuplicateResourceException,NotImplementedException,InvalidParametersException
	{
		logger.info ("Link palette connector");  	

		getEditionPattern().getCalc().getCalcOntology().loadWhenUnloaded();

		editionPatternInstance = getProject().makeNewEditionPatternInstance(getEditionPattern());
			
		applyEditionActions();

	}

	@Override
	public EditionScheme getEditionScheme()
	{
		return getLinkScheme();
	}

	@Override
	public Object getOverridenGraphicalRepresentation() 
	{
		return _graphicalRepresentation;
	}

	public void setOverridenGraphicalRepresentation(Object graphicalRepresentation) 
	{
		_graphicalRepresentation = graphicalRepresentation;
	}

	public OEConnector getNewConnector()
	{
		return _newConnector;
	}

	@Override
	public EditionPatternInstance getEditionPatternInstance()
	{
		return editionPatternInstance;
	}


	public OEShape getFromShape() {
		return _fromShape;
	}

	public void setFromShape(OEShape fromShape) {
		_fromShape = fromShape;
	}

	public OEShape getToShape() {
		return _toShape;
	}

	public void setToShape(OEShape toShape) {
		_toShape = toShape;
	}

	@Override
	protected OEShema retrieveOEShema()
	{
		if (getFromShape() != null) return getFromShape().getShema();
		if (getToShape() != null) return getToShape().getShema();
		if (getFocusedObject() instanceof OEShemaObject) return ((OEShemaObject)getFocusedObject()).getShema();
		return null;
	}
	
	@Override
	protected OEConnector performAddConnector(AddConnector action)
	{
		_newConnector = super.performAddConnector(action);
		return _newConnector;
	}


}