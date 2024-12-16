
package org.openrtp.namespaces.rts.version02;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for condition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="condition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="WaitTime" type="{http://www.openrtp.org/namespaces/rts}waittime"/>
 *           &lt;element name="Preceding" type="{http://www.openrtp.org/namespaces/rts}preceding"/>
 *         &lt;/choice>
 *         &lt;element name="TargetComponent" type="{http://www.openrtp.org/namespaces/rts}target_executioncontext"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sequence" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "condition", propOrder = {
    "waitTime",
    "preceding",
    "targetComponent"
})
@XmlSeeAlso({
    ConditionExt.class
})
public class Condition {

    @XmlElement(name = "WaitTime")
    protected Waittime waitTime;
    @XmlElement(name = "Preceding")
    protected Preceding preceding;
    @XmlElement(name = "TargetComponent", required = true)
    protected TargetExecutioncontext targetComponent;
    @XmlAttribute(name = "sequence", namespace = "http://www.openrtp.org/namespaces/rts", required = true)
    protected BigInteger sequence;

    /**
     * Gets the value of the waitTime property.
     * 
     * @return
     *     possible object is
     *     {@link Waittime }
     *     
     */
    public Waittime getWaitTime() {
        return waitTime;
    }

    /**
     * Sets the value of the waitTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Waittime }
     *     
     */
    public void setWaitTime(Waittime value) {
        this.waitTime = value;
    }

    /**
     * Gets the value of the preceding property.
     * 
     * @return
     *     possible object is
     *     {@link Preceding }
     *     
     */
    public Preceding getPreceding() {
        return preceding;
    }

    /**
     * Sets the value of the preceding property.
     * 
     * @param value
     *     allowed object is
     *     {@link Preceding }
     *     
     */
    public void setPreceding(Preceding value) {
        this.preceding = value;
    }

    /**
     * Gets the value of the targetComponent property.
     * 
     * @return
     *     possible object is
     *     {@link TargetExecutioncontext }
     *     
     */
    public TargetExecutioncontext getTargetComponent() {
        return targetComponent;
    }

    /**
     * Sets the value of the targetComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetExecutioncontext }
     *     
     */
    public void setTargetComponent(TargetExecutioncontext value) {
        this.targetComponent = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSequence(BigInteger value) {
        this.sequence = value;
    }

}
