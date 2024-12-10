
package org.openrtp.namespaces.rts.version02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for component complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="component">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataPorts" type="{http://www.openrtp.org/namespaces/rts}dataport" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ServicePorts" type="{http://www.openrtp.org/namespaces/rts}serviceport" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ConfigurationSets" type="{http://www.openrtp.org/namespaces/rts}configuration_set" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ExecutionContexts" type="{http://www.openrtp.org/namespaces/rts}execution_context" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Participants" type="{http://www.openrtp.org/namespaces/rts}participants" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pathUri" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="instanceName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="activeConfigurationSet" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="compositeType">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="None"/>
 *             &lt;enumeration value="PeriodicECShared"/>
 *             &lt;enumeration value="PeriodicStateShared"/>
 *             &lt;enumeration value="Grouping"/>
 *             &lt;enumeration value="FsmECShared"/>
 *             &lt;enumeration value="FsmStateShared"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="isRequired" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "component", propOrder = {
    "dataPorts",
    "servicePorts",
    "configurationSets",
    "executionContexts",
    "participants"
})
@XmlSeeAlso({
    ComponentExt.class
})
public class Component {

    @XmlElement(name = "DataPorts")
    protected List<Dataport> dataPorts;
    @XmlElement(name = "ServicePorts")
    protected List<Serviceport> servicePorts;
    @XmlElement(name = "ConfigurationSets")
    protected List<ConfigurationSet> configurationSets;
    @XmlElement(name = "ExecutionContexts")
    protected List<ExecutionContext> executionContexts;
    @XmlElement(name = "Participants")
    protected List<Participants> participants;
    @XmlAttribute(name = "id", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String id;
    @XmlAttribute(name = "pathUri", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String pathUri;
    @XmlAttribute(name = "instanceName", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String instanceName;
    @XmlAttribute(name = "activeConfigurationSet", namespace = "http://www.openrtp.org/namespaces/rts")
    protected String activeConfigurationSet;
    @XmlAttribute(name = "compositeType", namespace = "http://www.openrtp.org/namespaces/rts")
    protected String compositeType;
    @XmlAttribute(name = "isRequired", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected boolean isRequired;

    /**
     * Gets the value of the dataPorts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataPorts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataPorts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Dataport }
     * 
     * 
     */
    public List<Dataport> getDataPorts() {
        if (dataPorts == null) {
            dataPorts = new ArrayList<Dataport>();
        }
        return this.dataPorts;
    }

    /**
     * Gets the value of the servicePorts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servicePorts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServicePorts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Serviceport }
     * 
     * 
     */
    public List<Serviceport> getServicePorts() {
        if (servicePorts == null) {
            servicePorts = new ArrayList<Serviceport>();
        }
        return this.servicePorts;
    }

    /**
     * Gets the value of the configurationSets property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configurationSets property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigurationSets().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigurationSet }
     * 
     * 
     */
    public List<ConfigurationSet> getConfigurationSets() {
        if (configurationSets == null) {
            configurationSets = new ArrayList<ConfigurationSet>();
        }
        return this.configurationSets;
    }

    /**
     * Gets the value of the executionContexts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the executionContexts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExecutionContexts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExecutionContext }
     * 
     * 
     */
    public List<ExecutionContext> getExecutionContexts() {
        if (executionContexts == null) {
            executionContexts = new ArrayList<ExecutionContext>();
        }
        return this.executionContexts;
    }

    /**
     * Gets the value of the participants property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participants property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipants().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Participants }
     * 
     * 
     */
    public List<Participants> getParticipants() {
        if (participants == null) {
            participants = new ArrayList<Participants>();
        }
        return this.participants;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the pathUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathUri() {
        return pathUri;
    }

    /**
     * Sets the value of the pathUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathUri(String value) {
        this.pathUri = value;
    }

    /**
     * Gets the value of the instanceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * Sets the value of the instanceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceName(String value) {
        this.instanceName = value;
    }

    /**
     * Gets the value of the activeConfigurationSet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActiveConfigurationSet() {
        return activeConfigurationSet;
    }

    /**
     * Sets the value of the activeConfigurationSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActiveConfigurationSet(String value) {
        this.activeConfigurationSet = value;
    }

    /**
     * Gets the value of the compositeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompositeType() {
        return compositeType;
    }

    /**
     * Sets the value of the compositeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompositeType(String value) {
        this.compositeType = value;
    }

    /**
     * Gets the value of the isRequired property.
     * 
     */
    public boolean isIsRequired() {
        return isRequired;
    }

    /**
     * Sets the value of the isRequired property.
     * 
     */
    public void setIsRequired(boolean value) {
        this.isRequired = value;
    }

}
