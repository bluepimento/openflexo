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
package org.openflexo.fps.controller.browser;

import javax.swing.ImageIcon;

import org.openflexo.components.browser.BrowserElement;
import org.openflexo.components.browser.BrowserElementType;
import org.openflexo.components.browser.ProjectBrowser;
import org.openflexo.fps.CVSDirectory;
import org.openflexo.fps.CVSFile;
import org.openflexo.icon.FilesIconLibrary;


public class CVSDirectoryElement extends FPSBrowserElement
{
	public CVSDirectoryElement(CVSDirectory directory, ProjectBrowser browser, BrowserElement parent)
	{
		super(directory, BrowserElementType.CVS_DIRECTORY, browser,parent);
	}

	@Override
	protected void buildChildrenVector()
	{
		for (CVSDirectory dir : getDirectory().getDirectories()) {
			addToChilds(dir);
		}
		for (CVSFile file : getDirectory().getFiles()) {
			addToChilds(file);
		}
	}

	@Override
	public String getName()
	{
		return getDirectory().getFileName();
	}

	public CVSDirectory getDirectory()
	{
		return (CVSDirectory)getObject();
	}

	@Override
	public ImageIcon getBaseIcon()
	{
		if (getDirectory().getResourceType() != null) {
			return FilesIconLibrary.smallIconForFileFormat(getDirectory().getResourceType().getFormat());
		} else {
			return super.getBaseIcon();
		}
	}
}