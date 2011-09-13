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

import com.hp.hpl.jena.ontology.SomeValuesFromRestriction;
import com.hp.hpl.jena.rdf.model.Statement;

public class SomeRestrictionStatement extends ObjectRestrictionStatement {

	private static final Logger logger = Logger.getLogger(SomeRestrictionStatement.class.getPackage().getName());

	private OntologyProperty property;
	private OntologyObject object;
	
	public SomeRestrictionStatement(OntologyObject subject, Statement s, SomeValuesFromRestriction r)
	{
		super(subject,s,r);
		property = getOntologyLibrary().getProperty(r.getOnProperty().getURI());
		object = getOntologyLibrary().getOntologyObject(r.getSomeValuesFrom().getURI());
	}

	@Override
	public String getClassNameKey()
	{
		return "some_restriction_statement";
	}

	@Override
	public String getFullyQualifiedName()
	{
		return "SomeRestrictionStatement: "+getStatement();
	}


	@Override
	public OntologyObject getObject() 
	{
		return object;
	}

	@Override
	public OntologyProperty getProperty() 
	{
		return property;
	}

	@Override
	public String toString() 
	{
		return getSubject().getName()+" "+(property==null?"<NOT FOUND:"+restriction.getOnProperty().getURI()+">":property.getName())+" some "+(getObject() != null ? getObject().getName() : "<NOT_FOUND:"+getStatement().getObject()+">");
	}

	@Override
	public String getName() {
		return property.getName()+" some";
	}
}