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
package org.openflexo.tm.dao.impl;

import org.openflexo.foundation.sg.implmodel.ImplementationModel;
import org.openflexo.foundation.sg.implmodel.TechnologyModuleDefinition;
import org.openflexo.foundation.sg.implmodel.TechnologyModuleImplementation;
import org.openflexo.foundation.sg.implmodel.exception.TechnologyModuleCompatibilityCheckException;
import org.openflexo.foundation.xml.ImplementationModelBuilder;

/**
 * This class defines properties related to a Dao implementation.
 */
public class DaoImplementation extends TechnologyModuleImplementation {

    public static final String TECHNOLOGY_MODULE_NAME = "Dao";

    // ================ //
    // = Constructors = //
    // ================ //

    /**
      * Build a new Dao implementation for the specified implementation model builder.<br/>
      * This constructor is namely invoked during unserialization.
      *
      * @param builder
      *            the builder that will create this implementation
      * @throws TechnologyModuleCompatibilityCheckException
      */
    public DaoImplementation(ImplementationModelBuilder builder) throws TechnologyModuleCompatibilityCheckException {
        this(builder.implementationModel);
        initializeDeserialization(builder);
    }

    /**
      * Build a new Dao implementation for the specified implementation model.
      *
      * @param implementationModel
      *            the implementation model where to create this Dao implementation
      * @throws TechnologyModuleCompatibilityCheckException
      */
    public DaoImplementation(ImplementationModel implementationModel) throws TechnologyModuleCompatibilityCheckException {
        super(implementationModel);
    }

    // =========== //
    // = Methods = //
    // =========== //

    /**
      * {@inheritDoc}
      */
    @Override
    public TechnologyModuleDefinition getTechnologyModuleDefinition() {
        return TechnologyModuleDefinition.getTechnologyModuleDefinition(TECHNOLOGY_MODULE_NAME);
    }

    /**
      * {@inheritDoc}
      */
    @Override
    public boolean getHasInspector() {
        return true;
    }
}
