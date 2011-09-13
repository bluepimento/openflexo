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
package org.openflexo.components.browser.ontology;

import org.openflexo.components.browser.BrowserElement;
import org.openflexo.components.browser.BrowserElementType;
import org.openflexo.components.browser.ProjectBrowser;
import org.openflexo.foundation.ontology.calc.CalcPalette;
import org.openflexo.foundation.ontology.calc.CalcPaletteElement;


/**
 * Browser element representing the calc palette
 *
 * @author sguerin
 *
 */
public class CalcPaletteBrowserElement extends BrowserElement
{

    protected CalcPaletteBrowserElement(CalcPalette palette, ProjectBrowser browser, BrowserElement parent)
    {
        super(palette, BrowserElementType.ONTOLOGY_CALC_PALETTE, browser, parent);
    }

    @Override
	protected void buildChildrenVector()
    {
    	for (CalcPaletteElement element : getPalette().getElements()) {
    		addToChilds(element);
    	}
       }

    @Override
	public String getName()
    {
        return getPalette().getName()+".palette";
    }


    protected CalcPalette getPalette()
    {
        return (CalcPalette) getObject();
    }

}