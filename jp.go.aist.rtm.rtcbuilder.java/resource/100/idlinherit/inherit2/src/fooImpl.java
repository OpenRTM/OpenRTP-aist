// -*- Java -*-
// <rtc-template block="description">
/*!
 * @file  fooImpl.java
 * @brief test module
 * @date  $Date$
 *
 * $Id$
 */
// </rtc-template>
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.CorbaConsumer;
import jp.go.aist.rtm.RTC.port.CorbaPort;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import RTC.ReturnCode_t;
// <rtc-template block="component_description">
/**
 * fooImpl
 * <p>
 * test module
 *
 */
// </rtc-template>
public class fooImpl extends DataFlowComponentBase {
  /**
   * constructor
   * @param manager Manager Object
   */
    public fooImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_MyServiceProviderPort = new CorbaPort("MyServiceProvider");
        m_MyServiceRequirePort = new CorbaPort("MyServiceRequire");
        // </rtc-template>
    }
    /**
     *
     * The initialize action (on CREATED-&gt;ALIVE transition)
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onInitialize() {
        // Registration: InPort/OutPort/Service
        // <rtc-template block="registration">
        
        // Set service provider to Ports
        try {
            m_MyServiceProviderPort.registerProvider("MyServiceProvider", "MyServiceChild", m_MyServiceProvider);
            m_MyServiceProviderPort.registerProvider("MyServiceProvider2", "MyServiceWithTypeChild", m_MyServiceProvider2);
        } catch (ServantAlreadyActive e) {
            e.printStackTrace();
        } catch (WrongPolicy e) {
            e.printStackTrace();
        } catch (ObjectNotActive e) {
            e.printStackTrace();
        }
        
        // Set service consumers to Ports
        m_MyServiceRequirePort.registerConsumer("MyServiceRequire", "MyServiceChild", m_MyServiceRequireBase);
        m_MyServiceRequirePort.registerConsumer("MyServiceRequire2", "MyServiceWithTypeChild", m_MyServiceRequire2Base);
        
        // Set CORBA Service Ports
        addPort(m_MyServiceProviderPort);
        addPort(m_MyServiceRequirePort);
        // </rtc-template>
        return super.onInitialize();
    }
    /**
     *
     * The finalize action (on ALIVE-&gt;END transition)
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onFinalize() {
//        return super.onFinalize();
//    }
    /**
     *
     * The startup action when ExecutionContext startup
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStartup(int ec_id) {
//        return super.onStartup(ec_id);
//    }
    /**
     *
     * The shutdown action when ExecutionContext stop
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onShutdown(int ec_id) {
//        return super.onShutdown(ec_id);
//    }
    /**
     *
     * The activated action (Active state entry action)
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onActivated(int ec_id) {
//        return super.onActivated(ec_id);
//    }
    /**
     *
     * The deactivated action (Active state exit action)
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onDeactivated(int ec_id) {
//        return super.onDeactivated(ec_id);
//    }
    /**
     *
     * The execution action that is invoked periodically
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onExecute(int ec_id) {
//        return super.onExecute(ec_id);
//    }
    /**
     *
     * The aborting action when main logic error occurred.
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    public ReturnCode_t onAborting(int ec_id) {
//        return super.onAborting(ec_id);
//    }
    /**
     *
     * The error action in ERROR state
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    public ReturnCode_t onError(int ec_id) {
//        return super.onError(ec_id);
//    }
    /**
     *
     * The reset action that is invoked resetting
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onReset(int ec_id) {
//        return super.onReset(ec_id);
//    }
    /**
     *
     * The state update action that is invoked after onExecute() action
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onStateUpdate(int ec_id) {
//        return super.onStateUpdate(ec_id);
//    }
    /**
     *
     * The action that is invoked when execution context's rate is changed
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onRateChanged(int ec_id) {
//        return super.onRateChanged(ec_id);
//    }
//
    /**
     */
    /**
     */
    // DataInPort declaration
    // <rtc-template block="inport_declare">
    
    // </rtc-template>
    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    
    // </rtc-template>
    // CORBA Port declaration
    // <rtc-template block="corbaport_declare">
    /*!
     */
    protected CorbaPort m_MyServiceProviderPort;
    /*!
     */
    protected CorbaPort m_MyServiceRequirePort;
    
    // </rtc-template>
    // Service declaration
    // <rtc-template block="service_declare">
    /*!
     */
    protected MyServiceChildSVC_impl m_MyServiceProvider = new MyServiceChildSVC_impl();
    /*!
     */
    protected MyServiceWithTypeChildSVC_impl m_MyServiceProvider2 = new MyServiceWithTypeChildSVC_impl();
    
    // </rtc-template>
    // Consumer declaration
    // <rtc-template block="consumer_declare">
    protected CorbaConsumer<MyServiceChild> m_MyServiceRequireBase = new CorbaConsumer<MyServiceChild>(MyServiceChild.class);
    /*!
     */
    protected MyServiceChild m_MyServiceRequire;
    protected CorbaConsumer<MyServiceWithTypeChild> m_MyServiceRequire2Base = new CorbaConsumer<MyServiceWithTypeChild>(MyServiceWithTypeChild.class);
    /*!
     */
    protected MyServiceWithTypeChild m_MyServiceRequire2;
    
    // </rtc-template>
}
