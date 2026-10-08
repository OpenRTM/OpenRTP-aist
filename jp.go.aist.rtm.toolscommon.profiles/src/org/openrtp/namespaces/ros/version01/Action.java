
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for action complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="action">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="action_role" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="ActionServer"/>
 *             &lt;enumeration value="ActionClient"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="action_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="action_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="callback_base" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action", namespace = "http://www.openrtp.org/namespaces/ros")
@XmlSeeAlso({
    ActionDoc.class
})
public class Action {

    @XmlAttribute(name = "action_role", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String actionRole;
    @XmlAttribute(name = "action_name", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String actionName;
    @XmlAttribute(name = "action_type", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String actionType;
    @XmlAttribute(name = "callback_base", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected String callbackBase;

    /**
     * Gets the value of the actionRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionRole() {
        return actionRole;
    }

    /**
     * Sets the value of the actionRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionRole(String value) {
        this.actionRole = value;
    }

    /**
     * Gets the value of the actionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Sets the value of the actionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionName(String value) {
        this.actionName = value;
    }

    /**
     * Gets the value of the actionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Sets the value of the actionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionType(String value) {
        this.actionType = value;
    }

    /**
     * Gets the value of the callbackBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallbackBase() {
        return callbackBase;
    }

    /**
     * Sets the value of the callbackBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallbackBase(String value) {
        this.callbackBase = value;
    }

}
