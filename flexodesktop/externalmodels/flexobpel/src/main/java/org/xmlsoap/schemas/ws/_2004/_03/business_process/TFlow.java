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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.11.06 at 03:26:51 PM CET 
//

package org.xmlsoap.schemas.ws._2004._03.business_process;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tFlow complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tFlow">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tActivity">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}links" minOccurs="0"/>
 *         &lt;group ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}activity" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFlow", propOrder = { "links", "activity" })
public class TFlow extends TActivity {

	protected TLinks links;
	@XmlElements({ @XmlElement(name = "repeatUntil", type = TRepeatUntil.class), @XmlElement(name = "assign", type = TAssign.class),
			@XmlElement(name = "opaqueActivity", type = TOpaqueActivity.class), @XmlElement(name = "scope", type = TScope.class),
			@XmlElement(name = "invoke", type = TInvoke.class), @XmlElement(name = "validate", type = TValidate.class),
			@XmlElement(name = "throw", type = TThrow.class), @XmlElement(name = "while", type = TWhile.class),
			@XmlElement(name = "wait", type = TWait.class), @XmlElement(name = "compensate", type = TCompensate.class),
			@XmlElement(name = "empty", type = TEmpty.class), @XmlElement(name = "exit", type = TExit.class),
			@XmlElement(name = "reply", type = TReply.class), @XmlElement(name = "extensionActivity", type = TExtensionActivity.class),
			@XmlElement(name = "sequence", type = TSequence.class), @XmlElement(name = "compensateScope", type = TCompensateScope.class),
			@XmlElement(name = "forEach", type = TForEach.class), @XmlElement(name = "receive", type = TReceive.class),
			@XmlElement(name = "if", type = TIf.class), @XmlElement(name = "pick", type = TPick.class),
			@XmlElement(name = "flow", type = TFlow.class), @XmlElement(name = "rethrow", type = TRethrow.class) })
	protected List<Object> activity;

	/**
	 * Gets the value of the links property.
	 * 
	 * @return possible object is {@link TLinks }
	 * 
	 */
	public TLinks getLinks() {
		return links;
	}

	/**
	 * Sets the value of the links property.
	 * 
	 * @param value
	 *            allowed object is {@link TLinks }
	 * 
	 */
	public void setLinks(TLinks value) {
		this.links = value;
	}

	/**
	 * Gets the value of the activity property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the activity property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getActivity().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link TRepeatUntil } {@link TAssign } {@link TOpaqueActivity } {@link TScope }
	 * {@link TInvoke } {@link TValidate } {@link TThrow } {@link TWhile } {@link TWait } {@link TCompensate } {@link TEmpty } {@link TExit }
	 * {@link TReply } {@link TExtensionActivity } {@link TSequence } {@link TCompensateScope } {@link TForEach } {@link TReceive } {@link TIf }
	 * {@link TPick } {@link TFlow } {@link TRethrow }
	 * 
	 * 
	 */
	public List<Object> getActivity() {
		if (activity == null) {
			activity = new ArrayList<Object>();
		}
		return this.activity;
	}

}
