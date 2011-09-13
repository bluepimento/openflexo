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
package org.openflexo.foundation.ontology.shema;

/*
 * FlexoWorkflow.java
 * Project WorkflowEditor
 *
 * Created by benoit on Mar 3, 2004
 */

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.Inspectors;
import org.openflexo.foundation.ontology.dm.SLShemaCreated;
import org.openflexo.foundation.rm.FlexoOEShemaLibraryResource;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.FlexoResource;
import org.openflexo.foundation.rm.FlexoXMLStorageResource;
import org.openflexo.foundation.rm.InvalidFileNameException;
import org.openflexo.foundation.rm.ProjectRestructuration;
import org.openflexo.foundation.rm.SaveResourceException;
import org.openflexo.foundation.rm.XMLStorageResourceData;
import org.openflexo.foundation.utils.FlexoProjectFile;
import org.openflexo.foundation.xml.OEShemaLibraryBuilder;
import org.openflexo.inspector.InspectableObject;
import org.openflexo.xmlcode.XMLMapping;


/**
 * A FlexoComponentLibrary is an object referencing all components used in the
 * project
 *
 * @author benoit,sylvain
 */

public class OEShemaLibrary extends OESLObject implements XMLStorageResourceData, InspectableObject
{

    private static final Logger logger = Logger.getLogger(OEShemaLibrary.class.getPackage().getName());

    // =====================================================================
    // =========================== Instance variables ======================
    // =====================================================================

    private FlexoProject _project;

    private FlexoOEShemaLibraryResource _resource;

    private String name;

    private OEShemaFolder _rootFolder;

    // ========================================================
    // =================== Constructor ========================
    // ========================================================

    /**
     * Create a new FlexoComponentLibrary.
     */
    public OEShemaLibrary(OEShemaLibraryBuilder builder)
    {
        this(builder.getProject());
        builder.shemaLibrary = this;
        _resource = builder.resource;
        initializeDeserialization(builder);
    }

    /**
     * Create a new FlexoComponentLibrary.
     */
    public OEShemaLibrary(FlexoProject project)
    {
        super(project);
        _project = project;
    }

    @Override
	public FlexoOEShemaLibraryResource getFlexoResource()
    {
        return _resource;
    }

    @Override
	public FlexoXMLStorageResource getFlexoXMLFileResource()
    {
        return _resource;
    }

    /**
     * Creates and returns a newly created shema library
     *
     * @return a newly created shema library
     */
    public static OEShemaLibrary createNewShemaLibrary(FlexoProject project)
    {
        OEShemaLibrary newLibrary = new OEShemaLibrary(project);
        if (logger.isLoggable(Level.INFO))
            logger.info("createNewShemaLibrary(), project=" + project + " " + newLibrary);

        File compFile = ProjectRestructuration.getExpectedShemaLibFile(project);
        FlexoProjectFile shemaLibFile = new FlexoProjectFile(compFile, project);
        FlexoOEShemaLibraryResource slRes;
        try {
        	slRes = new FlexoOEShemaLibraryResource(project, newLibrary, shemaLibFile);
        } catch (InvalidFileNameException e2) {
            e2.printStackTrace();
            shemaLibFile = new FlexoProjectFile("ShemaLibrary");
            shemaLibFile.setProject(project);
            try {
            	slRes = new FlexoOEShemaLibraryResource(project, newLibrary, shemaLibFile);
            } catch (InvalidFileNameException e) {
                if (logger.isLoggable(Level.SEVERE))
                    logger.severe("Could not create shema library.");
                e.printStackTrace();
                return null;
            }
        }

        try {
        	slRes.saveResourceData();
            project.registerResource(slRes);
        } catch (Exception e1) {
            // Warns about the exception
            if (logger.isLoggable(Level.WARNING))
                logger.warning("Exception raised: " + e1.getClass().getName() + ". See console for details.");
            e1.printStackTrace();
            System.out.println("C'est la que ca chie");
            System.exit(-1);
        }
        try {
            OEShemaFolder.createNewRootFolder(newLibrary);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newLibrary;
    }

    @Override
    public OEShemaLibrary getShemaLibrary()
    {
        return this;
    }

    @Override
	public void setFlexoResource(FlexoResource resource)
    {
        _resource = (FlexoOEShemaLibraryResource) resource;
    }

    @Override
    public FlexoProject getProject()
    {
        return _project;
    }

    @Override
	public void setProject(FlexoProject aProject)
    {
        _project = aProject;
    }

    @Override
	public String getName()
    {
        return name;
    }

    @Override
	public void setName(String aName)
    {
        name = aName;
    }

    /**
     * Save this object using ResourceManager scheme Additionnaly save all known
     * processes related to this workflow
     *
     * Overrides
     *
     * @see org.openflexo.foundation.rm.FlexoResourceData#save()
     * @see org.openflexo.foundation.rm.FlexoResourceData#save()
     */
    @Override
	public void save() throws SaveResourceException
    {
        _resource.saveResourceData();

    }

    /**
     * Return an enumeration of all folders, by recursively explore the tree
     *
     * @return an Enumeration of FlexoComponentFolder elements
     */
    public Enumeration<OEShemaFolder> allFolders()
    {
        Vector<OEShemaFolder> temp = new Vector<OEShemaFolder>();
        addFolders(temp, getRootFolder());
        return temp.elements();
    }

    /**
     * Return number of folders
     */
    public int allFoldersCount()
    {
        Vector<OEShemaFolder> temp = new Vector<OEShemaFolder>();
        addFolders(temp, getRootFolder());
        return temp.size();
    }

    private void addFolders(Vector<OEShemaFolder> temp, OEShemaFolder folder)
    {
        temp.add(folder);
        for (Enumeration e = folder.getSubFolders().elements(); e.hasMoreElements();) {
            OEShemaFolder currentFolder = (OEShemaFolder) e.nextElement();
            addFolders(temp, currentFolder);
        }
    }

    public OEShemaFolder getFolderWithName(String folderName)
    {
        for (Enumeration e = allFolders(); e.hasMoreElements();) {
            OEShemaFolder folder = ((OEShemaFolder) e.nextElement());

            if (folder.getName().equals(folderName)) {
                return folder;
            }

        }
        if (logger.isLoggable(Level.FINE))
            logger.fine("Could not find folder named " + folderName);
        return null;
    }

    // ==========================================================================
    // ============================= Instance methods
    // ===========================
    // ==========================================================================

    public void setRootFolder(OEShemaFolder rootFolder)
    {
        _rootFolder = rootFolder;
        _rootFolder.setShemaLibrary(this);
    }

    public OEShemaFolder getRootFolder()
    {
        if (_rootFolder == null) {
            if (!isDeserializing()) {
                if (logger.isLoggable(Level.WARNING))
                    logger.warning("No root folder defined for component library");
                setRootFolder(OEShemaFolder.createNewRootFolder(this));
            }
        }
        return _rootFolder;
    }

    public boolean hasRootFolder()
    {
        return _rootFolder != null;
    }

    // =============================================================
    // ===================== Accessors =============================
    // =============================================================

    public Vector<OEShemaDefinition> getAllShemaList()
    {
        Vector<OEShemaDefinition> answer = new Vector<OEShemaDefinition>();
        answer.addAll(getRootFolder().getAllShemas());
        return answer;
    }

    public File getFile()
    {
        return _resource.getResourceFile().getFile();
    }

    // ==========================================================================
    // ========================= XML Serialization ============================
    // ==========================================================================

    @Override
    public XMLMapping getXMLMapping()
    {
        return getProject().getXmlMappings().getShemaLibraryMapping();
    }

    /**
     * @param value
     * @return
     */

    public void delete(OEShemaDefinition def)
    {
        boolean b =_rootFolder.delete(def);
        if (logger.isLoggable(Level.INFO))
            logger.info("Shema removal "+(b?"succeed":"failed"));
    }

    public boolean isValidForANewShemaName(String value)
    {
        if (value == null)
            return false;
        if (_rootFolder != null) {
            return _rootFolder.isValidForANewShemaName(value);
        }
        return true;
    }

    public OEShemaDefinition getShemaNamed(String value)
    {
        if (value == null)
            return null;
        if (_rootFolder != null) {
            return _rootFolder.getShemaNamed(value);
        }
        if (logger.isLoggable(Level.INFO))
            logger.info("Cannot find a component named : " + value);
        return null;
    }

    public Vector<OEShemaDefinition> retrieveAllShemas()
    {
    	Vector<OEShemaDefinition> returned = new Vector<OEShemaDefinition>();
    	if (getRootFolder() != null) appendAllShemas(getRootFolder(),returned);
    	return returned;
    }
    
    private void appendAllShemas(OEShemaFolder folder, Vector<OEShemaDefinition> v)
    {
    	for (OEShemaDefinition s : folder.getShemas()) {
    		v.add(s);
    	}
    	for (OEShemaFolder f : folder.getSubFolders()) {
    		appendAllShemas(f,v);
    	}
    }

	@Override
    public String getFullyQualifiedName()
    {
        return getProject().getFullyQualifiedName()+"."+"ShemaLibrary";
    }

    /**
     * @param definition
     */
    public void handleNewShemaCreated(OEShemaDefinition definition)
    {
        setChanged();
        notifyObservers(new SLShemaCreated(definition));
    }

    /**
     * Overrides getClassNameKey
     *
     * @see org.openflexo.foundation.FlexoModelObject#getClassNameKey()
     */
    @Override
    public String getClassNameKey()
    {
        return "shema_library";
    }

    /**
     * Overrides getInspectorName
     * @see org.openflexo.inspector.InspectableObject#getInspectorName()
     */
    @Override
	public String getInspectorName()
    {
        return Inspectors.OE.OE_SHEMA_LIBRARY_INSPECTOR;
    }

 }