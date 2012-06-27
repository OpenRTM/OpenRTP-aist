//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.24 at 04:33:50 �ߌ� JST 
//


package org.openrtp.namespaces.rts.version02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rts_profile_ext complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rts_profile_ext">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.openrtp.org/namespaces/rts}rts_profile">
 *       &lt;sequence>
 *         &lt;element name="VersionUpLogs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Properties" type="{http://www.openrtp.org/namespaces/rts_ext}property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="RtsProfile")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rts_profile_ext", namespace = "http://www.openrtp.org/namespaces/rts_ext", propOrder = {
    "versionUpLogs",
    "properties"
})
public class RtsProfileExt
    extends RtsProfile
{

    @XmlElement(name = "VersionUpLogs", namespace = "http://www.openrtp.org/namespaces/rts_ext")
    protected List<String> versionUpLogs;
    @XmlElement(name = "Properties", namespace = "http://www.openrtp.org/namespaces/rts_ext")
    protected List<Property> properties;
    @XmlAttribute(namespace = "http://www.openrtp.org/namespaces/rts_ext")
    protected String comment;

    /**
     * Gets the value of the versionUpLogs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the versionUpLogs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVersionUpLogs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getVersionUpLogs() {
        if (versionUpLogs == null) {
            versionUpLogs = new ArrayList<String>();
        }
        return this.versionUpLogs;
    }

    /**
     * Gets the value of the properties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}