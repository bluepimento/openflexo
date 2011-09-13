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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				XSD Authors: The child element correlations needs to be a Local Element Declaration, 
 * 				because there is another correlations element defined for the invoke activity.
 * 			
 * 
 * <p>Java class for tReceive complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tReceive">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tActivity">
 *       &lt;sequence>
 *         &lt;element name="correlations" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tCorrelations" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}fromParts" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="partnerLink" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}NCName" />
 *       &lt;attribute name="portType" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}QName" />
 *       &lt;attribute name="operation" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}NCName" />
 *       &lt;attribute name="variable" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}BPELVariableName" />
 *       &lt;attribute name="createInstance" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tBoolean" default="no" />
 *       &lt;attribute name="messageExchange" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}NCName" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tReceive", propOrder = {
    "correlations",
    "fromParts"
})
public class TReceive
    extends TActivity
{

    protected TCorrelations correlations;
    protected TFromParts fromParts;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected String partnerLink;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected String portType;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected String operation;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected String variable;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected TBoolean createInstance;
    @XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
    protected String messageExchange;

    /**
     * Gets the value of the correlations property.
     * 
     * @return
     *     possible object is
     *     {@link TCorrelations }
     *     
     */
    public TCorrelations getCorrelations() {
        return correlations;
    }

    /**
     * Sets the value of the correlations property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCorrelations }
     *     
     */
    public void setCorrelations(TCorrelations value) {
        this.correlations = value;
    }

    /**
     * Gets the value of the fromParts property.
     * 
     * @return
     *     possible object is
     *     {@link TFromParts }
     *     
     */
    public TFromParts getFromParts() {
        return fromParts;
    }

    /**
     * Sets the value of the fromParts property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFromParts }
     *     
     */
    public void setFromParts(TFromParts value) {
        this.fromParts = value;
    }

    /**
     * Gets the value of the partnerLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartnerLink() {
        return partnerLink;
    }

    /**
     * Sets the value of the partnerLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartnerLink(String value) {
        this.partnerLink = value;
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
    }

    /**
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperation(String value) {
        this.operation = value;
    }

    /**
     * Gets the value of the variable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Sets the value of the variable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariable(String value) {
        this.variable = value;
    }

    /**
     * Gets the value of the createInstance property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getCreateInstance() {
        if (createInstance == null) {
            return TBoolean.NO;
        } else {
            return createInstance;
        }
    }

    /**
     * Sets the value of the createInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setCreateInstance(TBoolean value) {
        this.createInstance = value;
    }

    /**
     * Gets the value of the messageExchange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageExchange() {
        return messageExchange;
    }

    /**
     * Sets the value of the messageExchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageExchange(String value) {
        this.messageExchange = value;
    }

}