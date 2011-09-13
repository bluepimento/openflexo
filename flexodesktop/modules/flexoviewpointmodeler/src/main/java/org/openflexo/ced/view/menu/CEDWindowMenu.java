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
package org.openflexo.ced.view.menu;

/*
 * Created on <date> by <yourname>
 *
 * Flexo Application Suite
 * (c) Denali 2003-2006
 */
import java.util.logging.Logger;

import org.openflexo.ced.controller.CEDController;
import org.openflexo.module.Module;
import org.openflexo.view.menu.WindowMenu;


/**
 * 'Window' menu for this Module
 * 
 * @author yourname
 */
public class CEDWindowMenu extends WindowMenu
{

    private static final Logger logger = Logger.getLogger(CEDWindowMenu.class.getPackage().getName());

    // ==========================================================================
    // ============================= Instance Variables
    // =========================
    // ==========================================================================

      protected CEDController _xxxController;

    // ==========================================================================
    // ============================= Constructor
    // ================================
    // ==========================================================================

    public CEDWindowMenu(CEDController controller)
    {
        super(controller,Module.XXX_MODULE);
        _xxxController = controller;
        // Put your actions here
    }

    public CEDController getXXXController()
    {
        return _xxxController;
    }
}