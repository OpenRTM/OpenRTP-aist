
package org.openrtp.namespaces.ros.version01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for language_ext complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="language_ext">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.openrtp.org/namespaces/ros}language">
 *       &lt;sequence>
 *         &lt;element name="targets" type="{http://www.openrtp.org/namespaces/ros_ext}target_environment"/>
 *         &lt;element name="Properties" type="{http://www.openrtp.org/namespaces/ros_ext}property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "language_ext", namespace = "http://www.openrtp.org/namespaces/ros_ext", propOrder = {
    "targets",
    "properties"
})
public class LanguageExt
    extends Language
{

    @XmlElement(required = true)
    protected TargetEnvironment targets;
    @XmlElement(name = "Properties")
    protected List<Property> properties;

    /**
     * Gets the value of the targets property.
     * 
     * @return
     *     possible object is
     *     {@link TargetEnvironment }
     *     
     */
    public TargetEnvironment getTargets() {
        return targets;
    }

    /**
     * Sets the value of the targets property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetEnvironment }
     *     
     */
    public void setTargets(TargetEnvironment value) {
        this.targets = value;
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

}
