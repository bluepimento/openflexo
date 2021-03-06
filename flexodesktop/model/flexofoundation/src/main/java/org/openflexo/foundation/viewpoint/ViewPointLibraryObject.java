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
package org.openflexo.foundation.viewpoint;

import org.openflexo.foundation.TemporaryFlexoModelObject;
import org.openflexo.foundation.ontology.OntologyLibrary;
import org.openflexo.inspector.InspectableObject;
import org.openflexo.xmlcode.XMLMapping;

public abstract class ViewPointLibraryObject extends TemporaryFlexoModelObject implements InspectableObject {

	public abstract ViewPointLibrary getViewPointLibrary();

	public OntologyLibrary getOntologyLibrary() {
		if (getViewPointLibrary() != null) {
			return getViewPointLibrary().getOntologyLibrary();
		}
		return null;
	}

	@Override
	public XMLMapping getXMLMapping() {
		if (getViewPointLibrary() != null) {
			return getViewPointLibrary().get_VIEW_POINT_MODEL();
		}
		return super.getXMLMapping();
	}

}
