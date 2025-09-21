// -*- Java -*-
// <rtc-template block="description">
/*!
 * @file  ModuleNameImpl.java
 * @brief ModuleDescription
 * @date  $Date$
 *
 * $Id$
 */
// </rtc-template>
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.util.IntegerHolder;
import RTC.ReturnCode_t;
// <rtc-template block="component_description">
/**
 * ModuleNameImpl
 * <p>
 * ModuleDescription
 *
 */
// </rtc-template>
public class ModuleNameImpl extends DataFlowComponentBase {
  /**
   * constructor
   * @param manager Manager Object
   */
    public ModuleNameImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        // </rtc-template>
    }
    /*!
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
        // </rtc-template>
        bindParameter("int_param0", m_int_param0, "0");
        bindParameter("vector_param", m_vector_param, "1.0,2.0,3.0");
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
    // Configuration variable declaration
    // <rtc-template block="config_declare">
    /*!
     * 
     * - Name:  int_param0
     * - DefaultValue: 0
     */
    protected IntegerHolder m_int_param0 = new IntegerHolder();
    /*!
     * 
     * - Name:  vector_param
     * - DefaultValue: 1.0,2.0,3.0
     */
    protected VectorHolder m_vector_param = new VectorHolder();
    // </rtc-template>
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
    
    // </rtc-template>
    // Service declaration
    // <rtc-template block="service_declare">
    
    // </rtc-template>
    // Consumer declaration
    // <rtc-template block="consumer_declare">
    
    // </rtc-template>
}
