//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.24 at 04:33:50 午後 JST 
//


package org.openrtp.namespaces.rts.version02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for preceding complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="preceding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PrecedingComponents" type="{http://www.openrtp.org/namespaces/rts}target_executioncontext" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="timeout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sendingTiming" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "preceding", propOrder = {
    "precedingComponents"
})
public class Preceding {

    @XmlElement(name = "PrecedingComponents", required = true)
    protected List<TargetExecutioncontext> precedingComponents;
    @XmlAttribute(namespace = "http://www.openrtp.org/namespaces/rts")
    protected String timeout;
    @XmlAttribute(namespace = "http://www.openrtp.org/namespaces/rts")
    protected String sendingTiming;

    /**
     * Gets the value of the precedingComponents property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the precedingComponents property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrecedingComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TargetExecutioncontext }
     * 
     * 
     */
    public List<TargetExecutioncontext> getPrecedingComponents() {
        if (precedingComponents == null) {
            precedingComponents = new ArrayList<TargetExecutioncontext>();
        }
        return this.precedingComponents;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the sendingTiming property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendingTiming() {
        return sendingTiming;
    }

    /**
     * Sets the value of the sendingTiming property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendingTiming(String value) {
        this.sendingTiming = value;
    }

}
