
package org.openrtp.namespaces.ros.version01;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openrtp.namespaces.ros.version01 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ROSProfile_QNAME = new QName("http://www.openrtp.org/namespaces/ros", "ROSProfile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openrtp.namespaces.ros.version01
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Timer }
     * 
     */
    public Timer createTimer() {
        return new Timer();
    }

    /**
     * Create an instance of {@link BasicInfoExt }
     * 
     */
    public BasicInfoExt createBasicInfoExt() {
        return new BasicInfoExt();
    }

    /**
     * Create an instance of {@link LifeCycleExt }
     * 
     */
    public LifeCycleExt createLifeCycleExt() {
        return new LifeCycleExt();
    }

    /**
     * Create an instance of {@link LifecycleCallbackExt }
     * 
     */
    public LifecycleCallbackExt createLifecycleCallbackExt() {
        return new LifecycleCallbackExt();
    }

    /**
     * Create an instance of {@link ServiceExt }
     * 
     */
    public ServiceExt createServiceExt() {
        return new ServiceExt();
    }

    /**
     * Create an instance of {@link Library }
     * 
     */
    public Library createLibrary() {
        return new Library();
    }

    /**
     * Create an instance of {@link TargetEnvironment }
     * 
     */
    public TargetEnvironment createTargetEnvironment() {
        return new TargetEnvironment();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link TopicExt }
     * 
     */
    public TopicExt createTopicExt() {
        return new TopicExt();
    }

    /**
     * Create an instance of {@link ParameterExt }
     * 
     */
    public ParameterExt createParameterExt() {
        return new ParameterExt();
    }

    /**
     * Create an instance of {@link ActionExt }
     * 
     */
    public ActionExt createActionExt() {
        return new ActionExt();
    }

    /**
     * Create an instance of {@link LanguageExt }
     * 
     */
    public LanguageExt createLanguageExt() {
        return new LanguageExt();
    }

    /**
     * Create an instance of {@link RosProfile }
     * 
     */
    public RosProfile createRosProfile() {
        return new RosProfile();
    }

    /**
     * Create an instance of {@link Lifecycle }
     * 
     */
    public Lifecycle createLifecycle() {
        return new Lifecycle();
    }

    /**
     * Create an instance of {@link BasicInfo }
     * 
     */
    public BasicInfo createBasicInfo() {
        return new BasicInfo();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link LifecycleCallback }
     * 
     */
    public LifecycleCallback createLifecycleCallback() {
        return new LifecycleCallback();
    }

    /**
     * Create an instance of {@link Topic }
     * 
     */
    public Topic createTopic() {
        return new Topic();
    }

    /**
     * Create an instance of {@link Action }
     * 
     */
    public Action createAction() {
        return new Action();
    }

    /**
     * Create an instance of {@link Language }
     * 
     */
    public Language createLanguage() {
        return new Language();
    }

    /**
     * Create an instance of {@link DocTopic }
     * 
     */
    public DocTopic createDocTopic() {
        return new DocTopic();
    }

    /**
     * Create an instance of {@link DocBasic }
     * 
     */
    public DocBasic createDocBasic() {
        return new DocBasic();
    }

    /**
     * Create an instance of {@link DocLifecycleCallback }
     * 
     */
    public DocLifecycleCallback createDocLifecycleCallback() {
        return new DocLifecycleCallback();
    }

    /**
     * Create an instance of {@link TopicDoc }
     * 
     */
    public TopicDoc createTopicDoc() {
        return new TopicDoc();
    }

    /**
     * Create an instance of {@link DocParameter }
     * 
     */
    public DocParameter createDocParameter() {
        return new DocParameter();
    }

    /**
     * Create an instance of {@link ParameterDoc }
     * 
     */
    public ParameterDoc createParameterDoc() {
        return new ParameterDoc();
    }

    /**
     * Create an instance of {@link DocService }
     * 
     */
    public DocService createDocService() {
        return new DocService();
    }

    /**
     * Create an instance of {@link ActionDoc }
     * 
     */
    public ActionDoc createActionDoc() {
        return new ActionDoc();
    }

    /**
     * Create an instance of {@link ServiceDoc }
     * 
     */
    public ServiceDoc createServiceDoc() {
        return new ServiceDoc();
    }

    /**
     * Create an instance of {@link BasicInfoDoc }
     * 
     */
    public BasicInfoDoc createBasicInfoDoc() {
        return new BasicInfoDoc();
    }

    /**
     * Create an instance of {@link LifecycleCallbackDoc }
     * 
     */
    public LifecycleCallbackDoc createLifecycleCallbackDoc() {
        return new LifecycleCallbackDoc();
    }

    /**
     * Create an instance of {@link DocAction }
     * 
     */
    public DocAction createDocAction() {
        return new DocAction();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RosProfile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.openrtp.org/namespaces/ros", name = "ROSProfile")
    public JAXBElement<RosProfile> createROSProfile(RosProfile value) {
        return new JAXBElement<RosProfile>(_ROSProfile_QNAME, RosProfile.class, null, value);
    }

}
