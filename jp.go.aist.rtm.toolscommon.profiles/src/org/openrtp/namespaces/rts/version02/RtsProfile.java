
package org.openrtp.namespaces.rts.version02;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for rts_profile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rts_profile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Components" type="{http://www.openrtp.org/namespaces/rts}component" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Groups" type="{http://www.openrtp.org/namespaces/rts}componentGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DataPortConnectors" type="{http://www.openrtp.org/namespaces/rts}dataport_connector" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ServicePortConnectors" type="{http://www.openrtp.org/namespaces/rts}serviceport_connector" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StartUp" type="{http://www.openrtp.org/namespaces/rts}startup" minOccurs="0"/>
 *         &lt;element name="ShutDown" type="{http://www.openrtp.org/namespaces/rts}shutdown" minOccurs="0"/>
 *         &lt;element name="Activation" type="{http://www.openrtp.org/namespaces/rts}activation" minOccurs="0"/>
 *         &lt;element name="Deactivation" type="{http://www.openrtp.org/namespaces/rts}deactivation" minOccurs="0"/>
 *         &lt;element name="Resetting" type="{http://www.openrtp.org/namespaces/rts}resetting" minOccurs="0"/>
 *         &lt;element name="Initializing" type="{http://www.openrtp.org/namespaces/rts}initialize" minOccurs="0"/>
 *         &lt;element name="Finalizing" type="{http://www.openrtp.org/namespaces/rts}finalize" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="abstract" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="creationDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="updateDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rts_profile", propOrder = {
    "components",
    "groups",
    "dataPortConnectors",
    "servicePortConnectors",
    "startUp",
    "shutDown",
    "activation",
    "deactivation",
    "resetting",
    "initializing",
    "finalizing"
})
public class RtsProfile {

    @XmlElement(name = "Components")
    protected List<Component> components;
    @XmlElement(name = "Groups")
    protected List<ComponentGroup> groups;
    @XmlElement(name = "DataPortConnectors")
    protected List<DataportConnector> dataPortConnectors;
    @XmlElement(name = "ServicePortConnectors")
    protected List<ServiceportConnector> servicePortConnectors;
    @XmlElement(name = "StartUp")
    protected Startup startUp;
    @XmlElement(name = "ShutDown")
    protected Shutdown shutDown;
    @XmlElement(name = "Activation")
    protected Activation activation;
    @XmlElement(name = "Deactivation")
    protected Deactivation deactivation;
    @XmlElement(name = "Resetting")
    protected Resetting resetting;
    @XmlElement(name = "Initializing")
    protected Initialize initializing;
    @XmlElement(name = "Finalizing")
    protected Finalize finalizing;
    @XmlAttribute(name = "id", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String id;
    @XmlAttribute(name = "version", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected String version;
    @XmlAttribute(name = "abstract", namespace = "http://www.openrtp.org/namespaces/rts")
    protected String _abstract;
    @XmlAttribute(name = "creationDate", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    @XmlAttribute(name = "updateDate", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected XMLGregorianCalendar updateDate;

    /**
     * Gets the value of the components property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the components property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Component }
     * 
     * 
     */
    public List<Component> getComponents() {
        if (components == null) {
            components = new ArrayList<Component>();
        }
        return this.components;
    }

    /**
     * Gets the value of the groups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentGroup }
     * 
     * 
     */
    public List<ComponentGroup> getGroups() {
        if (groups == null) {
            groups = new ArrayList<ComponentGroup>();
        }
        return this.groups;
    }

    /**
     * Gets the value of the dataPortConnectors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataPortConnectors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataPortConnectors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataportConnector }
     * 
     * 
     */
    public List<DataportConnector> getDataPortConnectors() {
        if (dataPortConnectors == null) {
            dataPortConnectors = new ArrayList<DataportConnector>();
        }
        return this.dataPortConnectors;
    }

    /**
     * Gets the value of the servicePortConnectors property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servicePortConnectors property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServicePortConnectors().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceportConnector }
     * 
     * 
     */
    public List<ServiceportConnector> getServicePortConnectors() {
        if (servicePortConnectors == null) {
            servicePortConnectors = new ArrayList<ServiceportConnector>();
        }
        return this.servicePortConnectors;
    }

    /**
     * Gets the value of the startUp property.
     * 
     * @return
     *     possible object is
     *     {@link Startup }
     *     
     */
    public Startup getStartUp() {
        return startUp;
    }

    /**
     * Sets the value of the startUp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Startup }
     *     
     */
    public void setStartUp(Startup value) {
        this.startUp = value;
    }

    /**
     * Gets the value of the shutDown property.
     * 
     * @return
     *     possible object is
     *     {@link Shutdown }
     *     
     */
    public Shutdown getShutDown() {
        return shutDown;
    }

    /**
     * Sets the value of the shutDown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Shutdown }
     *     
     */
    public void setShutDown(Shutdown value) {
        this.shutDown = value;
    }

    /**
     * Gets the value of the activation property.
     * 
     * @return
     *     possible object is
     *     {@link Activation }
     *     
     */
    public Activation getActivation() {
        return activation;
    }

    /**
     * Sets the value of the activation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Activation }
     *     
     */
    public void setActivation(Activation value) {
        this.activation = value;
    }

    /**
     * Gets the value of the deactivation property.
     * 
     * @return
     *     possible object is
     *     {@link Deactivation }
     *     
     */
    public Deactivation getDeactivation() {
        return deactivation;
    }

    /**
     * Sets the value of the deactivation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Deactivation }
     *     
     */
    public void setDeactivation(Deactivation value) {
        this.deactivation = value;
    }

    /**
     * Gets the value of the resetting property.
     * 
     * @return
     *     possible object is
     *     {@link Resetting }
     *     
     */
    public Resetting getResetting() {
        return resetting;
    }

    /**
     * Sets the value of the resetting property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resetting }
     *     
     */
    public void setResetting(Resetting value) {
        this.resetting = value;
    }

    /**
     * Gets the value of the initializing property.
     * 
     * @return
     *     possible object is
     *     {@link Initialize }
     *     
     */
    public Initialize getInitializing() {
        return initializing;
    }

    /**
     * Sets the value of the initializing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Initialize }
     *     
     */
    public void setInitializing(Initialize value) {
        this.initializing = value;
    }

    /**
     * Gets the value of the finalizing property.
     * 
     * @return
     *     possible object is
     *     {@link Finalize }
     *     
     */
    public Finalize getFinalizing() {
        return finalizing;
    }

    /**
     * Sets the value of the finalizing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Finalize }
     *     
     */
    public void setFinalizing(Finalize value) {
        this.finalizing = value;
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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the abstract property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Sets the value of the abstract property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbstract(String value) {
        this._abstract = value;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
    }

    /**
     * Gets the value of the updateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

}
