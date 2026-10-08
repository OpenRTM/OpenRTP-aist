
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lifecycle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lifecycle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OnConfigure" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *         &lt;element name="OnActivate" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *         &lt;element name="OnDeactivate" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *         &lt;element name="OnCleanup" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *         &lt;element name="OnShutdown" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *         &lt;element name="OnError" type="{http://www.openrtp.org/namespaces/ros}lifecycle_callback" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lifecycle", namespace = "http://www.openrtp.org/namespaces/ros", propOrder = {
    "onConfigure",
    "onActivate",
    "onDeactivate",
    "onCleanup",
    "onShutdown",
    "onError"
})
@XmlSeeAlso({
    LifeCycleExt.class
})
public class Lifecycle {

    @XmlElement(name = "OnConfigure")
    protected LifecycleCallback onConfigure;
    @XmlElement(name = "OnActivate")
    protected LifecycleCallback onActivate;
    @XmlElement(name = "OnDeactivate")
    protected LifecycleCallback onDeactivate;
    @XmlElement(name = "OnCleanup")
    protected LifecycleCallback onCleanup;
    @XmlElement(name = "OnShutdown")
    protected LifecycleCallback onShutdown;
    @XmlElement(name = "OnError")
    protected LifecycleCallback onError;

    /**
     * Gets the value of the onConfigure property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnConfigure() {
        return onConfigure;
    }

    /**
     * Sets the value of the onConfigure property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnConfigure(LifecycleCallback value) {
        this.onConfigure = value;
    }

    /**
     * Gets the value of the onActivate property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnActivate() {
        return onActivate;
    }

    /**
     * Sets the value of the onActivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnActivate(LifecycleCallback value) {
        this.onActivate = value;
    }

    /**
     * Gets the value of the onDeactivate property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnDeactivate() {
        return onDeactivate;
    }

    /**
     * Sets the value of the onDeactivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnDeactivate(LifecycleCallback value) {
        this.onDeactivate = value;
    }

    /**
     * Gets the value of the onCleanup property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnCleanup() {
        return onCleanup;
    }

    /**
     * Sets the value of the onCleanup property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnCleanup(LifecycleCallback value) {
        this.onCleanup = value;
    }

    /**
     * Gets the value of the onShutdown property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnShutdown() {
        return onShutdown;
    }

    /**
     * Sets the value of the onShutdown property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnShutdown(LifecycleCallback value) {
        this.onShutdown = value;
    }

    /**
     * Gets the value of the onError property.
     * 
     * @return
     *     possible object is
     *     {@link LifecycleCallback }
     *     
     */
    public LifecycleCallback getOnError() {
        return onError;
    }

    /**
     * Sets the value of the onError property.
     * 
     * @param value
     *     allowed object is
     *     {@link LifecycleCallback }
     *     
     */
    public void setOnError(LifecycleCallback value) {
        this.onError = value;
    }

}
