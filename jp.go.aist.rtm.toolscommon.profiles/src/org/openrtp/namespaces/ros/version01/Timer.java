
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="timer_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="rate" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="call_back" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timer")
public class Timer {

    @XmlAttribute(name = "timer_name", namespace = "http://www.openrtp.org/namespaces/ros_ext", required = true)
    protected String timerName;
    @XmlAttribute(name = "rate", namespace = "http://www.openrtp.org/namespaces/ros_ext", required = true)
    protected double rate;
    @XmlAttribute(name = "call_back", namespace = "http://www.openrtp.org/namespaces/ros_ext", required = true)
    protected String callBack;
    @XmlAttribute(name = "description", namespace = "http://www.openrtp.org/namespaces/ros_ext")
    protected String description;

    /**
     * Gets the value of the timerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimerName() {
        return timerName;
    }

    /**
     * Sets the value of the timerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimerName(String value) {
        this.timerName = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     */
    public double getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     */
    public void setRate(double value) {
        this.rate = value;
    }

    /**
     * Gets the value of the callBack property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallBack() {
        return callBack;
    }

    /**
     * Sets the value of the callBack property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallBack(String value) {
        this.callBack = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
