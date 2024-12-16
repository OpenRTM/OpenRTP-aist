
package org.openrtp.namespaces.rts.version02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceport_connector complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceport_connector">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourceServicePort" type="{http://www.openrtp.org/namespaces/rts}target_port"/>
 *         &lt;element name="targetServicePort" type="{http://www.openrtp.org/namespaces/rts}target_port"/>
 *       &lt;/sequence>
 *       &lt;attribute name="connectorId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="transMethod" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceport_connector", propOrder = {
    "sourceServicePort",
    "targetServicePort"
})
@XmlSeeAlso({
    ServiceportConnectorExt.class
})
public class ServiceportConnector {

    @XmlElement(required = true)
    protected TargetPort sourceServicePort;
    @XmlElement(required = true)
    protected TargetPort targetServicePort;
    @XmlAttribute(name = "connectorId", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String connectorId;
    @XmlAttribute(name = "name", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String name;
    @XmlAttribute(name = "transMethod", namespace = "http://www.openrtp.org/namespaces/rts")
    protected String transMethod;

    /**
     * Gets the value of the sourceServicePort property.
     * 
     * @return
     *     possible object is
     *     {@link TargetPort }
     *     
     */
    public TargetPort getSourceServicePort() {
        return sourceServicePort;
    }

    /**
     * Sets the value of the sourceServicePort property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetPort }
     *     
     */
    public void setSourceServicePort(TargetPort value) {
        this.sourceServicePort = value;
    }

    /**
     * Gets the value of the targetServicePort property.
     * 
     * @return
     *     possible object is
     *     {@link TargetPort }
     *     
     */
    public TargetPort getTargetServicePort() {
        return targetServicePort;
    }

    /**
     * Sets the value of the targetServicePort property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetPort }
     *     
     */
    public void setTargetServicePort(TargetPort value) {
        this.targetServicePort = value;
    }

    /**
     * Gets the value of the connectorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectorId() {
        return connectorId;
    }

    /**
     * Sets the value of the connectorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectorId(String value) {
        this.connectorId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the transMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransMethod() {
        return transMethod;
    }

    /**
     * Sets the value of the transMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransMethod(String value) {
        this.transMethod = value;
    }

}
