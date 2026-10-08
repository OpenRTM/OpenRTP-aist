
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for service complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="service_role" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Server"/>
 *             &lt;enumeration value="Client"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="service_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="service_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="variable_callback_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", namespace = "http://www.openrtp.org/namespaces/ros")
@XmlSeeAlso({
    ServiceDoc.class
})
public class Service {

    @XmlAttribute(name = "service_role", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String serviceRole;
    @XmlAttribute(name = "service_name", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String serviceName;
    @XmlAttribute(name = "service_type", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String serviceType;
    @XmlAttribute(name = "variable_callback_name", namespace = "http://www.openrtp.org/namespaces/ros")
    protected String variableCallbackName;

    /**
     * Gets the value of the serviceRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceRole() {
        return serviceRole;
    }

    /**
     * Sets the value of the serviceRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceRole(String value) {
        this.serviceRole = value;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the variableCallbackName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariableCallbackName() {
        return variableCallbackName;
    }

    /**
     * Sets the value of the variableCallbackName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariableCallbackName(String value) {
        this.variableCallbackName = value;
    }

}
