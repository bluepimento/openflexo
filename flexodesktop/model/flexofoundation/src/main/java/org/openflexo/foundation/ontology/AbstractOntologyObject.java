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
package org.openflexo.foundation.ontology;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.XMLStorageResourceData;
import org.openflexo.inspector.InspectableObject;
import org.openflexo.xmlcode.XMLMapping;

public abstract class AbstractOntologyObject extends FlexoModelObject implements InspectableObject {

	private static final Logger logger = Logger.getLogger(AbstractOntologyObject.class.getPackage().getName());

	public AbstractOntologyObject() {
		super();
	}

	@Override
	public XMLMapping getXMLMapping() {
		return null;
	}

	@Override
	public XMLStorageResourceData getXMLResourceData() {
		return null;
	}

	public abstract FlexoOntology getFlexoOntology();

	@Override
	public FlexoProject getProject() {
		return getFlexoOntology().getProject();
	}

	public OntologyLibrary getOntologyLibrary() {
		return getFlexoOntology().getOntologyLibrary();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "_" + getURI();
	}

	public abstract String getDisplayableDescription();
}
