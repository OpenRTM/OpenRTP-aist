
package org.openrtp.namespaces.ros.version01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for target_environment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="target_environment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="libraries" type="{http://www.openrtp.org/namespaces/ros_ext}library" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ros_version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target_environment", namespace = "http://www.openrtp.org/namespaces/ros_ext", propOrder = {
    "libraries"
})
public class TargetEnvironment {

    protected List<Library> libraries;
    @XmlAttribute(name = "ros_version", namespace = "http://www.openrtp.org/namespaces/ros_ext")
    protected String rosVersion;

    /**
     * Gets the value of the libraries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Library }
     * 
     * 
     */
    public List<Library> getLibraries() {
        if (libraries == null) {
            libraries = new ArrayList<Library>();
        }
        return this.libraries;
    }

    /**
     * Gets the value of the rosVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRosVersion() {
        return rosVersion;
    }

    /**
     * Sets the value of the rosVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRosVersion(String value) {
        this.rosVersion = value;
    }

}
