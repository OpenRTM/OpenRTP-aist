
package org.openrtp.namespaces.ros.version01;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for topic complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="topic">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="topic_role" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Subscribe"/>
 *             &lt;enumeration value="Publish"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="topic_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="message_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="QoS_reliability_type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Reliable"/>
 *             &lt;enumeration value="Best_Effort"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="QoS_history_type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="KeepLast"/>
 *             &lt;enumeration value="KeepAll"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="QoS_history_depth" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="variable_callback_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "topic", namespace = "http://www.openrtp.org/namespaces/ros")
@XmlSeeAlso({
    TopicDoc.class
})
public class Topic {

    @XmlAttribute(name = "topic_role", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String topicRole;
    @XmlAttribute(name = "topic_name", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String topicName;
    @XmlAttribute(name = "message_type", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String messageType;
    @XmlAttribute(name = "QoS_reliability_type", namespace = "http://www.openrtp.org/namespaces/ros")
    protected String qoSReliabilityType;
    @XmlAttribute(name = "QoS_history_type", namespace = "http://www.openrtp.org/namespaces/ros")
    protected String qoSHistoryType;
    @XmlAttribute(name = "QoS_history_depth", namespace = "http://www.openrtp.org/namespaces/ros")
    protected BigInteger qoSHistoryDepth;
    @XmlAttribute(name = "variable_callback_name", namespace = "http://www.openrtp.org/namespaces/ros")
    protected String variableCallbackName;

    /**
     * Gets the value of the topicRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopicRole() {
        return topicRole;
    }

    /**
     * Sets the value of the topicRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopicRole(String value) {
        this.topicRole = value;
    }

    /**
     * Gets the value of the topicName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * Sets the value of the topicName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopicName(String value) {
        this.topicName = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the qoSReliabilityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQoSReliabilityType() {
        return qoSReliabilityType;
    }

    /**
     * Sets the value of the qoSReliabilityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQoSReliabilityType(String value) {
        this.qoSReliabilityType = value;
    }

    /**
     * Gets the value of the qoSHistoryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQoSHistoryType() {
        return qoSHistoryType;
    }

    /**
     * Sets the value of the qoSHistoryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQoSHistoryType(String value) {
        this.qoSHistoryType = value;
    }

    /**
     * Gets the value of the qoSHistoryDepth property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQoSHistoryDepth() {
        return qoSHistoryDepth;
    }

    /**
     * Sets the value of the qoSHistoryDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQoSHistoryDepth(BigInteger value) {
        this.qoSHistoryDepth = value;
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
