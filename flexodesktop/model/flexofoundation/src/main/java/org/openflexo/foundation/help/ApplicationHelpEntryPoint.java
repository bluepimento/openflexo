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
package org.openflexo.foundation.help;

import java.util.List;
import java.util.Map;

import org.openflexo.foundation.rm.FlexoProject;

public interface ApplicationHelpEntryPoint {

	public long getFlexoID();

	public ApplicationHelpEntryPoint getParentHelpEntry();

	public boolean hasDescription();

	public String getDescription();

	public boolean hasSpecificHelp(String key);

	public Map<String, String> getSpecificDescriptions();

	public FlexoProject getProject();

	public String getShortHelpLabel();

	public String getTypedHelpLabel();

	public List<ApplicationHelpEntryPoint> getChildsHelpObjects();
}
