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

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class DomainStatement extends OntologyStatement {

	private static final Logger logger = Logger.getLogger(DomainStatement.class.getPackage().getName());

	public static final String DOMAIN_URI = "http://www.w3.org/2000/01/rdf-schema#domain";

	private OntologyObject domain;

	public DomainStatement(OntologyObject subject, Statement s) {
		super(subject, s);
		if (s.getObject() instanceof Resource) {
			domain = getOntologyLibrary().getOntologyObject(((Resource) s.getObject()).getURI());
		} else {
			logger.warning("DomainStatement: object is not a Resource !");
		}
	}

	@Override
	public String getClassNameKey() {
		return "domain_statement";
	}

	@Override
	public String getFullyQualifiedName() {
		return "DomainStatement: " + getStatement();
	}

	public OntologyObject getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return getSubject().getName() + " has domain "
				+ (getDomain() != null ? getDomain().getName() : "<NOT_FOUND:" + getStatement().getObject() + ">");
	}

}
