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
// Generated on: 2008.02.08 at 10:43:57 AM CET 
//

package org.oasis_open.docs.wsbpel._2_0.process.executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import org.w3c.dom.Element;

/**
 * <p>
 * Java class for tTo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tTo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}documentation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;any/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}query" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="expressionLanguage" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="variable" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}BPELVariableName" />
 *       &lt;attribute name="part" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="property" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="partnerLink" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tTo", propOrder = { "content" })
public class TTo {

	@XmlElementRefs({
			@XmlElementRef(
					name = "documentation",
					namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable",
					type = JAXBElement.class),
			@XmlElementRef(name = "query", namespace = "http://docs.oasis-open.org/wsbpel/2.0/process/executable", type = JAXBElement.class) })
	@XmlMixed
	@XmlAnyElement(lax = true)
	protected List<Object> content;
	@XmlAttribute
	@XmlSchemaType(name = "anyURI")
	protected String expressionLanguage;
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String variable;
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NCName")
	protected String part;
	@XmlAttribute
	protected QName property;
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NCName")
	protected String partnerLink;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Gets the value of the content property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list
	 * will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the content property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getContent().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Element } {@link JAXBElement }{@code <}{@link TQuery }{@code >}
	 * {@link Object } {@link String } {@link JAXBElement }{@code <}{@link TDocumentation }{@code >}
	 * 
	 * 
	 */
	public List<Object> getContent() {
		if (content == null) {
			content = new ArrayList<Object>();
		}
		return this.content;
	}

	/**
	 * Gets the value of the expressionLanguage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExpressionLanguage() {
		return expressionLanguage;
	}

	/**
	 * Sets the value of the expressionLanguage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setExpressionLanguage(String value) {
		this.expressionLanguage = value;
	}

	/**
	 * Gets the value of the variable property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * Sets the value of the variable property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVariable(String value) {
		this.variable = value;
	}

	/**
	 * Gets the value of the part property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPart() {
		return part;
	}

	/**
	 * Sets the value of the part property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPart(String value) {
		this.part = value;
	}

	/**
	 * Gets the value of the property property.
	 * 
	 * @return possible object is {@link QName }
	 * 
	 */
	public QName getProperty() {
		return property;
	}

	/**
	 * Sets the value of the property property.
	 * 
	 * @param value
	 *            allowed object is {@link QName }
	 * 
	 */
	public void setProperty(QName value) {
		this.property = value;
	}

	/**
	 * Gets the value of the partnerLink property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPartnerLink() {
		return partnerLink;
	}

	/**
	 * Sets the value of the partnerLink property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPartnerLink(String value) {
		this.partnerLink = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed property on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute by updating the map directly. Because of this design, there's
	 * no setter.
	 * 
	 * 
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}
