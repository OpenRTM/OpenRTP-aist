
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lifecycle_callback_doc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lifecycle_callback_doc">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.openrtp.org/namespaces/ros}lifecycle_callback">
 *       &lt;sequence>
 *         &lt;element name="Doc" type="{http://www.openrtp.org/namespaces/ros_doc}doc_lifecycle_callback" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lifecycle_callback_doc", namespace = "http://www.openrtp.org/namespaces/ros_doc", propOrder = {
    "doc"
})
@XmlSeeAlso({
    LifecycleCallbackExt.class
})
public class LifecycleCallbackDoc
    extends LifecycleCallback
{

    @XmlElement(name = "Doc")
    protected DocLifecycleCallback doc;

    /**
     * Gets the value of the doc property.
     * 
     * @return
     *     possible object is
     *     {@link DocLifecycleCallback }
     *     
     */
    public DocLifecycleCallback getDoc() {
        return doc;
    }

    /**
     * Sets the value of the doc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocLifecycleCallback }
     *     
     */
    public void setDoc(DocLifecycleCallback value) {
        this.doc = value;
    }

}
