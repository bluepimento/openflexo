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
package org.openflexo.foundation.wkf.dm;

import org.openflexo.foundation.wkf.DeadLine;
import org.openflexo.foundation.wkf.FlexoProcess;

/**
 * Please comment this class
 * 
 * @author sguerin
 * @deprecated
 * 
 */
@Deprecated
public class DeadLineInserted extends WKFDataModification {

	public FlexoProcess parentProcess;

	public DeadLineInserted(DeadLine insertedDeadLine, FlexoProcess parentProcess) {
		super(null, insertedDeadLine);
		this.parentProcess = parentProcess;
	}

}
