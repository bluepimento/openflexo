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
package org.openflexo.fib.model;

import java.util.List;

import org.openflexo.antar.binding.BindingDefinition;
import org.openflexo.antar.binding.BindingDefinition.BindingDefinitionType;
import org.openflexo.antar.binding.GenericArrayTypeImpl;
import org.openflexo.antar.binding.ParameterizedTypeImpl;
import org.openflexo.antar.binding.WilcardTypeImpl;
import org.openflexo.model.annotations.ModelEntity;

@ModelEntity
public interface FIBDropDownColumn extends FIBTableColumn {

	public static enum Parameters implements FIBModelAttribute
	{
		staticList,
		list,
		array
	}

	public static final BindingDefinition LIST = new BindingDefinition("list", new ParameterizedTypeImpl(List.class, new WilcardTypeImpl(
			Object.class)), BindingDefinitionType.GET, false);
	public static final BindingDefinition ARRAY = new BindingDefinition("array",
			new GenericArrayTypeImpl(new WilcardTypeImpl(Object.class)), BindingDefinitionType.GET, false);

	public DataBinding getList();

	public void setList(DataBinding list);

	public DataBinding getArray();

	public void setArray(DataBinding array);

}
