
package org.openrtp.namespaces.rts.version02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataport_connector complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataport_connector">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sourceDataPort" type="{http://www.openrtp.org/namespaces/rts}target_port"/>
 *         &lt;element name="targetDataPort" type="{http://www.openrtp.org/namespaces/rts}target_port"/>
 *       &lt;/sequence>
 *       &lt;attribute name="connectorId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="interfaceType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="dataflowType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="subscriptionType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pushInterval" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataport_connector", propOrder = {
    "sourceDataPort",
    "targetDataPort"
})
@XmlSeeAlso({
    DataportConnectorExt.class
})
public class DataportConnector {

    @XmlElement(required = true)
    protected TargetPort sourceDataPort;
    @XmlElement(required = true)
    protected TargetPort targetDataPort;
    @XmlAttribute(name = "connectorId", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String connectorId;
    @XmlAttribute(name = "name", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String name;
    @XmlAttribute(name = "dataType", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String dataType;
    @XmlAttribute(name = "interfaceType", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String interfaceType;
    @XmlAttribute(name = "dataflowType", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String dataflowType;
    @XmlAttribute(name = "subscriptionType", namespace = "http://www.openrtp.org/namespaces/rts")
    protected String subscriptionType;
    @XmlAttribute(name = "pushInterval", namespace = "http://www.openrtp.org/namespaces/rts")
    protected Double pushInterval;

    /**
     * Gets the value of the sourceDataPort property.
     * 
     * @return
     *     possible object is
     *     {@link TargetPort }
     *     
     */
    public TargetPort getSourceDataPort() {
        return sourceDataPort;
    }

    /**
     * Sets the value of the sourceDataPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetPort }
     *     
     */
    public void setSourceDataPort(TargetPort value) {
        this.sourceDataPort = value;
    }

    /**
     * Gets the value of the targetDataPort property.
     * 
     * @return
     *     possible object is
     *     {@link TargetPort }
     *     
     */
    public TargetPort getTargetDataPort() {
        return targetDataPort;
    }

    /**
     * Sets the value of the targetDataPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetPort }
     *     
     */
    public void setTargetDataPort(TargetPort value) {
        this.targetDataPort = value;
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
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the interfaceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfaceType() {
        return interfaceType;
    }

    /**
     * Sets the value of the interfaceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfaceType(String value) {
        this.interfaceType = value;
    }

    /**
     * Gets the value of the dataflowType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataflowType() {
        return dataflowType;
    }

    /**
     * Sets the value of the dataflowType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataflowType(String value) {
        this.dataflowType = value;
    }

    /**
     * Gets the value of the subscriptionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * Sets the value of the subscriptionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubscriptionType(String value) {
        this.subscriptionType = value;
    }

    /**
     * Gets the value of the pushInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getPushInterval() {
        return pushInterval;
    }

    /**
     * Sets the value of the pushInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setPushInterval(Double value) {
        this.pushInterval = value;
    }

}
