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
package org.openflexo.foundation.ie.dm;

import org.openflexo.foundation.ie.cl.ComponentDefinition;
import org.openflexo.foundation.rm.FlexoResource;
import org.openflexo.foundation.rm.RMNotification;

public class ComponentDeleted extends IEDataModification implements RMNotification {

	public ComponentDefinition component;

	public ComponentDeleted(ComponentDefinition component) {
		super(component, null);
		this.component = component;
	}

	@Override
	public boolean forceUpdateWhenUnload() {
		return true;
	}

	@Override
	public boolean isDeepNotification() {
		return true;
	}

	@Override
	public boolean propagateToSynchronizedResource(FlexoResource originResource, FlexoResource targetResource) {
		if ((originResource == component.getComponentLibrary().getFlexoResource()) /*&& targetResource == component.getComponentResource()*/) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean propagateToAlteredResource(FlexoResource originResource, FlexoResource targetResource) {
		if (originResource == component.getComponentResource()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "ComponentDeleteRequest " + component.getComponentName();
	}
}
