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

import javax.swing.JComponent;

import org.openflexo.fib.model.FIBPanel.Layout;

public class FlowLayoutConstraints extends ComponentConstraints {

	public FlowLayoutConstraints() {
		super();
	}

	public FlowLayoutConstraints(int index) {
		super();
		setIndex(index);
	}

	protected FlowLayoutConstraints(String someConstraints) {
		super(someConstraints);
	}

	FlowLayoutConstraints(ComponentConstraints someConstraints) {
		super(someConstraints);
	}

	@Override
	protected Layout getType() {
		return Layout.flow;
	}

	@Override
	public void performConstrainedAddition(JComponent container, JComponent contained) {
		container.add(contained);
	}

}
