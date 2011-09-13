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
package org.openflexo.icon;

import javax.swing.ImageIcon;

import org.openflexo.toolbox.ImageIconResource;

/**
 * Utility class containing all icons used in context of CGModule
 * 
 * @author sylvain
 *
 */
public class CGIconLibrary extends GeneratorIconLibrary {

	// Module icons
	public static final ImageIcon CG_ACTIVE_ICON = new ImageIconResource("Icons/CG/CG_A_Small.gif");
	public static final ImageIcon CG_UNACTIVE_ICON = new ImageIconResource("Icons/CG/CG_NA_Small.gif");
	public static final ImageIcon CG_SELECTED_ICON = new ImageIconResource("Icons/CG/CG_S_Small.gif");
	public static final ImageIcon CG_BIG_ACTIVE_ICON = new ImageIconResource("Icons/CG/CG_A.gif");
	public static final ImageIcon CG_BIG_UNACTIVE_ICON = new ImageIconResource("Icons/CG/CG_NA.gif");
	public static final ImageIcon CG_BIG_SELECTED_ICON = new ImageIconResource("Icons/CG/CG_S.gif");

	// Perspective icons
	public static final ImageIcon CG_MRP_ACTIVE_ICON = new ImageIconResource("Icons/CG/MRPerspective_A.gif");
	public static final ImageIcon CG_MRP_SELECTED_ICON = new ImageIconResource("Icons/CG/MRPerspective_S.gif");
	public static final ImageIcon CG_CGP_ACTIVE_ICON = new ImageIconResource("Icons/CG/CGPerspective_A.gif");
	public static final ImageIcon CG_CGP_SELECTED_ICON = new ImageIconResource("Icons/CG/CGPerspective_S.gif");
	public static final ImageIcon CG_VP_ACTIVE_ICON = new ImageIconResource("Icons/CG/VersionningPerspective_A.gif");
	public static final ImageIcon CG_VP_SELECTED_ICON = new ImageIconResource("Icons/CG/VersionningPerspective_S.gif");
	
	// Model icons
	public static final ImageIcon GENERATED_CODE_ICON = new ImageIconResource("Icons/CG/GeneratedCode.gif");
	public static final ImageIcon GENERATED_CODE_REPOSITORY_ICON = new ImageIconResource("Icons/CG/GeneratedCodeRepository.gif");
	public static final ImageIcon SYMBOLIC_FOLDER_ICON = new ImageIconResource("Icons/CG/prj_obj.gif");
	public static final ImageIcon JAVA_SOURCE_FOLDER_ICON = new ImageIconResource("Icons/CG/JavaSourceFolder.gif");
	public static final ImageIcon TARGET_ICON = new ImageIconResource("Icons/CG/Target.gif");
	public static final ImageIcon TARGET_FOLDER_ICON = new ImageIconResource("Icons/CG/TargetFolder.gif");

}