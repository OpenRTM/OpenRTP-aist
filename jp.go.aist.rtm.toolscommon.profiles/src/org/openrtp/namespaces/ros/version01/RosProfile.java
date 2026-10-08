
package org.openrtp.namespaces.ros.version01;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ros_profile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ros_profile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BasicInfo" type="{http://www.openrtp.org/namespaces/ros}basic_info"/>
 *         &lt;element name="LifeCycle" type="{http://www.openrtp.org/namespaces/ros}lifecycle"/>
 *         &lt;element name="Topics" type="{http://www.openrtp.org/namespaces/ros}topic" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Services" type="{http://www.openrtp.org/namespaces/ros}service" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Actions" type="{http://www.openrtp.org/namespaces/ros}action" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Parameters" type="{http://www.openrtp.org/namespaces/ros}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Language" type="{http://www.openrtp.org/namespaces/ros}language" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement(name="ROSProfile")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ros_profile", namespace = "http://www.openrtp.org/namespaces/ros", propOrder = {
    "basicInfo",
    "lifeCycle",
    "topics",
    "services",
    "actions",
    "parameters",
    "language"
})
public class RosProfile {

    @XmlElement(name = "BasicInfo", required = true)
    protected BasicInfo basicInfo;
    @XmlElement(name = "LifeCycle", required = true)
    protected Lifecycle lifeCycle;
    @XmlElement(name = "Topics")
    protected List<Topic> topics;
    @XmlElement(name = "Services")
    protected List<Service> services;
    @XmlElement(name = "Actions")
    protected List<Action> actions;
    @XmlElement(name = "Parameters")
    protected List<Parameter> parameters;
    @XmlElement(name = "Language")
    protected Language language;
    @XmlAttribute(name = "version", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String version;

    /**
     * Gets the value of the basicInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BasicInfo }
     *     
     */
    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    /**
     * Sets the value of the basicInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BasicInfo }
     *     
     */
    public void setBasicInfo(BasicInfo value) {
        this.basicInfo = value;
    }

    /**
     * Gets the value of the lifeCycle property.
     * 
     * @return
     *     possible object is
     *     {@link Lifecycle }
     *     
     */
    public Lifecycle getLifeCycle() {
        return lifeCycle;
    }

    /**
     * Sets the value of the lifeCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Lifecycle }
     *     
     */
    public void setLifeCycle(Lifecycle value) {
        this.lifeCycle = value;
    }

    /**
     * Gets the value of the topics property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topics property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopics().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Topic }
     * 
     * 
     */
    public List<Topic> getTopics() {
        if (topics == null) {
            topics = new ArrayList<Topic>();
        }
        return this.topics;
    }

    /**
     * Gets the value of the services property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the services property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Service }
     * 
     * 
     */
    public List<Service> getServices() {
        if (services == null) {
            services = new ArrayList<Service>();
        }
        return this.services;
    }

    /**
     * Gets the value of the actions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Action }
     * 
     * 
     */
    public List<Action> getActions() {
        if (actions == null) {
            actions = new ArrayList<Action>();
        }
        return this.actions;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     * 
     * 
     */
    public List<Parameter> getParameters() {
        if (parameters == null) {
        	parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link Language }
     *     
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link Language }
     *     
     */
    public void setLanguage(Language value) {
        this.language = value;
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

}
