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
package org.openflexo.foundation.ie;

/**
 * Represents a WOComponent displaying a whole page
 * 
 * @author bmangez
 */

import java.util.logging.Logger;

import org.openflexo.foundation.DataFlexoObserver;
import org.openflexo.foundation.ie.cl.ComponentDefinition;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.xml.FlexoComponentBuilder;

public abstract class IEPartialComponent extends IEWOComponent implements DataFlexoObserver {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(IEPartialComponent.class.getPackage().getName());

	public IEPartialComponent(ComponentDefinition model, FlexoProject prj) {
		super(model, prj);
	}

	public IEPartialComponent(FlexoComponentBuilder builder) {
		super(builder);
	}

}
