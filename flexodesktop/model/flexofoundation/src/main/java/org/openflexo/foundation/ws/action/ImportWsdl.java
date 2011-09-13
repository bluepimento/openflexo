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
package org.openflexo.foundation.ws.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;


public class ImportWsdl extends CreateNewWebService 
{

    static final Logger logger = Logger.getLogger(ImportWsdl.class.getPackage().getName());

    public static FlexoActionType actionType = new FlexoActionType ("import_wsdl",FlexoActionType.importMenu,FlexoActionType.defaultGroup) {

        /**
         * Factory method
         */
        @Override
		public FlexoAction makeNewAction(FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor) 
        {
            return new ImportWsdl(focusedObject, globalSelection, editor);
        }

        @Override
		protected boolean isVisibleForSelection(FlexoModelObject object, Vector globalSelection) 
        {
            return true;
        }

        @Override
		protected boolean isEnabledForSelection(FlexoModelObject object, Vector globalSelection) 
        {
            return true;
         }
                
    };
    
    ImportWsdl (FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor)
    {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
	protected void doAction(Object context) throws FlexoException {
    		super.doAction(context);

    }
 
 
}