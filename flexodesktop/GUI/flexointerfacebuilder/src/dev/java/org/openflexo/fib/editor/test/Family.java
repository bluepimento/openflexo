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
package org.openflexo.fib.editor.test;

import java.util.Vector;

public class Family {

	public Person father;
	public Person mother;
	
	public Person biggestChild;
	public Person biggestParent;

	public Person[] parents;
	
	private Vector<Person> children;
	
	public enum Gender
	{
		Male,
		Female
	}
	
	public Family()
	{
		father = new Person("Robert","Smith",39,Gender.Male);
		mother = new Person("Mary","Smith",37,Gender.Female);
		parents = new Person[2];
		parents[0] = father;
		parents[1] = mother;
		
		biggestChild = null;

		children = new Vector<Person>();
		children.add(new Person("John","Smith",9,Gender.Male));
		children.add(new Person("Suzy","Smith",3,Gender.Female));
		

	}
	
	public Vector<Person> getChildren()
	{
		return children;
	}
	
	public Person createChild()
	{
		System.out.println("OK, je cree un nouvel enfant !!!");
		Person newChild = new Person("John Jr","Smith",0,Gender.Male);
		children.add(newChild);
		return newChild;
	}
	
	public Person deleteChild(Person childToDelete)
	{
		System.out.println("OK, je supprime cet enfant: "+childToDelete);
		children.remove(childToDelete);
		return childToDelete;
	}
	
	@Override
	public String toString()
	{
		return super.toString()+" children="+Integer.toHexString(children.hashCode())+" : "+children;
	}
		
	public static class Person
	{
		public Person(String firstName, String lastName, int age, Gender gender)
		{
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
			this.gender = gender;
		}
		
		public String firstName;
		public String lastName;
		public int age;
		public Gender gender;
		
		@Override
		public String toString()
		{
			return firstName+" "+lastName+" aged "+age+" ("+gender+")";
		}
	}

}
