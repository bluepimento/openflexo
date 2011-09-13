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
package org.openflexo.oe.controller;

import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.openflexo.FlexoCst;
import org.openflexo.components.browser.view.BrowserView.SelectionPolicy;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.ontology.ImportedOntology;
import org.openflexo.foundation.ontology.ProjectOntology;
import org.openflexo.foundation.ontology.shema.OEObject;
import org.openflexo.foundation.ontology.shema.OEShema;
import org.openflexo.foundation.ontology.shema.OEShemaDefinition;
import org.openflexo.foundation.ontology.shema.OEShemaLibrary;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.oe.OECst;
import org.openflexo.oe.shema.OEShemaController;
import org.openflexo.oe.shema.OEShemaModuleView;
import org.openflexo.oe.view.OEBrowserView;
import org.openflexo.utils.FlexoSplitPaneLocationSaver;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;

public class ShemaPerspective extends FlexoPerspective<OEObject>
{

	private final OEController _controller;

	private final ShemaLibraryBrowser _browser;
	private final ShemaBrowser shemaBrowser;
	private final OEBrowserView _browserView;
	private final OEBrowserView shemaBrowserView;


	private final Hashtable<OEShema,OEShemaController> _controllers;
	private final Hashtable<OEShemaController,JSplitPane> _splitPaneForProcess;

	private final JSplitPane splitPane;

	private final JLabel infoLabel;

	private static final JPanel EMPTY_RIGHT_VIEW = new JPanel();
	
	/**
	 * @param controller TODO
	 * @param name
	 */
	public ShemaPerspective(OEController controller)
	{
		super("shema_perspective");
		_controller = controller;
		_controllers = new Hashtable<OEShema, OEShemaController>();
		_splitPaneForProcess = new Hashtable<OEShemaController, JSplitPane>();
		_browser = new ShemaLibraryBrowser(controller);
		_browserView = new OEBrowserView(_browser, _controller, SelectionPolicy.ParticipateToSelection) {
			@Override
			public void treeDoubleClick(FlexoModelObject object) {
				super.treeDoubleClick(object);
				if (object instanceof OEShema) {
		    		focusOnShema((OEShema)object);
				}
			}

   		  /*  public void objectAddedToSelection(ObjectAddedToSelectionEvent event)
		    {
		    	if (event.getAddedObject() instanceof ERDiagram) {
		    		diagramBrowser.deleteBrowserListener(this);
		    		diagramBrowser.setRepresentedDiagram((ERDiagram)event.getAddedObject());
		    		diagramBrowser.update();
		    		diagramBrowser.addBrowserListener(this);
		    	}
		    	super.objectAddedToSelection(event);
		    }			*/
		};
		shemaBrowser = new ShemaBrowser(controller);
		shemaBrowserView = new OEBrowserView(shemaBrowser, controller, SelectionPolicy.ForceSelection);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,_browserView,shemaBrowserView);
		splitPane.setDividerLocation(0.7);
		splitPane.setResizeWeight(0.7);
		infoLabel = new JLabel("Shema perspective");
		infoLabel.setFont(FlexoCst.SMALL_FONT);
	}

	public void focusOnShema(OEShema shema)
	{
		shemaBrowser.deleteBrowserListener(_browserView);
		shemaBrowser.setRepresentedShema(shema);
		shemaBrowser.update();
		shemaBrowser.addBrowserListener(_browserView);
	}

	/**
	 * Overrides getIcon
	 *
	 * @see org.openflexo.view.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon()
	{
		return VEIconLibrary.VE_SP_ACTIVE_ICON;
	}

	/**
	 * Overrides getSelectedIcon
	 *
	 * @see org.openflexo.view.FlexoPerspective#getSelectedIcon()
	 */
	@Override
	public ImageIcon getSelectedIcon()
	{
		return VEIconLibrary.VE_SP_SELECTED_ICON;
	}

	@Override
	public OEObject getDefaultObject(FlexoModelObject proposedObject)
	{
		if (proposedObject instanceof OEShema) {
			return (OEShema)proposedObject;
		}
		return proposedObject.getProject().getShemaLibrary();
	}

	@Override
	public boolean hasModuleViewForObject(FlexoModelObject object)
	{
		return (object instanceof OEObject);
	}


	@Override
	public ModuleView<? extends OEObject> createModuleViewForObject(OEObject object, FlexoController controller)
	{
		if (object instanceof OEShema) {
			return getControllerForShema((OEShema)object).getModuleView();
		}
		if (object instanceof OEShemaDefinition) {
			return getControllerForShema(((OEShemaDefinition)object).getShema()).getModuleView();
		}
		return new EmptyPanel<OEObject>(controller,this,object);
	}

	@Override
	public boolean doesPerspectiveControlLeftView()
	{
		return true;
	}

	@Override
	public JComponent getLeftView()
	{
		return splitPane;
	}

	@Override
	public JComponent getHeader()
	{
		if (getCurrentShemaModuleView() != null) {
			return getCurrentShemaModuleView().getController().getScalePanel();
		}
		return null;
	}

	@Override
	public JComponent getFooter()
	{
		return infoLabel;
	}

	public OEShemaModuleView getCurrentShemaModuleView()
	{
		if ((_controller != null) && (_controller.getCurrentModuleView() instanceof OEShemaModuleView)) {
			return (OEShemaModuleView)_controller.getCurrentModuleView();
		}
		return null;
	}

	public OEShemaController getControllerForShema(OEShema shema)
	{
		OEShemaController returned = _controllers.get(shema);
		if (returned == null) {
			returned = new OEShemaController(_controller,shema);
			_controllers.put(shema, returned);
		}
		return returned;
	}


	public void removeFromControllers(OEShemaController shemaController)
	{
		_controllers.remove(shemaController.getDrawing().getShema());
		_splitPaneForProcess.remove(shemaController);
	}


	@Override
	public boolean isAlwaysVisible()
	{
		return true;
	}

	public String getWindowTitleforObject(FlexoModelObject object)
	{
		if (object == null) {
			return FlexoLocalization.localizedForKey("no_selection");
		}
		if (object instanceof OEShemaLibrary) {
			return FlexoLocalization.localizedForKey("shema_library");
		}
		if (object instanceof OEShemaDefinition) {
			return ((OEShemaDefinition) object).getName();
		}
		if (object instanceof OEShema) {
			return ((OEShema) object).getName();
		}
		if (object instanceof ProjectOntology) {
			return FlexoLocalization.localizedForKey("project_ontology");
		}
		if (object instanceof ImportedOntology) {
			return ((ImportedOntology)object).getName();
		}
		return object.getFullyQualifiedName();
	}

    @Override
    public boolean doesPerspectiveControlRightView()
    {
    	return true;
    }

	@Override
	public JComponent getRightView()
	{
		if (getCurrentShemaModuleView() == null) {
			return EMPTY_RIGHT_VIEW;
		}
		return getSplitPaneWithPalettesAndDocInspectorPanel();
	}

	/**
	 * Return Split pane with Role palette and doc inspector panel
	 * Disconnect doc inspector panel from its actual parent
	 * @return
	 */
	protected JSplitPane getSplitPaneWithPalettesAndDocInspectorPanel()
	{
		JSplitPane splitPaneWithPalettesAndDocInspectorPanel = _splitPaneForProcess.get(getCurrentShemaModuleView().getController());
		if (splitPaneWithPalettesAndDocInspectorPanel == null) {
			splitPaneWithPalettesAndDocInspectorPanel = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT,
					getCurrentShemaModuleView().getController().getPaletteView(),
					_controller.getDisconnectedDocInspectorPanel());
			splitPaneWithPalettesAndDocInspectorPanel.setResizeWeight(0);
			splitPaneWithPalettesAndDocInspectorPanel.setDividerLocation(OECst.PALETTE_DOC_SPLIT_LOCATION);
			_splitPaneForProcess.put(getCurrentShemaModuleView().getController(),splitPaneWithPalettesAndDocInspectorPanel);
		}
		if (splitPaneWithPalettesAndDocInspectorPanel.getBottomComponent() == null) {
			splitPaneWithPalettesAndDocInspectorPanel.setBottomComponent(_controller.getDisconnectedDocInspectorPanel());
		}
		new FlexoSplitPaneLocationSaver(splitPaneWithPalettesAndDocInspectorPanel,"OEPaletteAndDocInspectorPanel");
		return splitPaneWithPalettesAndDocInspectorPanel;
	}


}