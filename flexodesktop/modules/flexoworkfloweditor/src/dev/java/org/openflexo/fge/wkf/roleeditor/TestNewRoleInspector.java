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
package org.openflexo.fge.wkf.roleeditor;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.logging.Logger;

import org.openflexo.inspector.InspectorController;
import org.openflexo.inspector.InspectorWindow;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.toolbox.FileResource;

public class TestNewRoleInspector extends InspectorController {

	private static final Logger logger = FlexoLogger.getLogger(TestNewRoleInspector.class.getPackage().getName());

	private InspectorWindow window;
	
	public TestNewRoleInspector()
	{
		super(null,null);
		logger.info("Load WKF inspectors...");
		try {
			importInspectorFile(new FileResource("DrawingEditorInspectors/GraphicalRepresentation.inspector"));
			importInspectorFile(new FileResource("DrawingEditorInspectors/ShapeGraphicalRepresentation.inspector"));
			importInspectorFile(new FileResource("DrawingEditorInspectors/DrawingGraphicalRepresentation.inspector"));
			importInspectorFile(new FileResource("DrawingEditorInspectors/ConnectorGraphicalRepresentation.inspector"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			updateSuperInspectors();
		}
		
		window = createInspectorWindow(null);
	}

	public InspectorWindow getWindow()
	{
		return window;
	}
	
	@Override
	public void update(Observable observable, Object selection)
	{
		super.update(observable, selection);
	}
	
}
