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
package cb.petal;

/**
 * Denote that a petal object contains a reference to another object via the property "quidu".
 * 
 * @version $Id: HasQuidu.java,v 1.2 2011/09/12 11:46:47 gpolet Exp $
 * @author <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public interface HasQuidu extends PetalNode {
	public void setQuidu(String quid);

	public String getQuidu();

	public long getQuiduAsLong();

	public void setQuiduAsLong(long quid);

	/**
	 * @return object referenced via "quidu" property
	 */
	public QuidObject getReferencedObject();
}
