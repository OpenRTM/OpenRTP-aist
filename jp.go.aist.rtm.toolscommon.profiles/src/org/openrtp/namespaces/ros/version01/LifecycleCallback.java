
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lifecycle_callback complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lifecycle_callback">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="implemented" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lifecycle_callback", namespace = "http://www.openrtp.org/namespaces/ros")
@XmlSeeAlso({
    LifecycleCallbackDoc.class
})
public class LifecycleCallback {

    @XmlAttribute(name = "implemented", namespace = "http://www.openrtp.org/namespaces/ros", required = true)
    protected boolean implemented;
    protected String implementeds;

    /**
     * Gets the value of the implemented property.
     * 
     */
    public boolean isImplemented() {
        return implemented;
    }

    /**
     * Sets the value of the implemented property.
     * 
     */
    public void setImplemented(boolean value) {
        this.implemented = value;
    }

    public void setImplementedbln(boolean value) {
        this.implemented = value;
    }
    public String getImplemented() {
        if(implementeds==null) implementeds = Boolean.valueOf(implemented).toString();
        return implementeds;
    }
    public void setImplemented(String value) {
    	implementeds = value;
    }
}
