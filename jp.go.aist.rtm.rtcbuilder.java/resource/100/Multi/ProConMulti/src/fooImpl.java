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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import RTC.TimedShort;
import RTC.TimedLong;
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
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
        m_in1_val = new TimedShort();
        initializeParam(m_in1_val);
        m_in1 = new DataRef<TimedShort>(m_in1_val);
        m_in1In = new InPort<TimedShort>("in1", m_in1);
        m_out1_val = new TimedLong();
        initializeParam(m_out1_val);
        m_out1 = new DataRef<TimedLong>(m_out1_val);
        m_out1Out = new OutPort<TimedLong>("out1", m_out1);
        m_MySVProPort = new CorbaPort("MySVPro");
        m_MySVPro2Port = new CorbaPort("MySVPro2");
        m_MyConProPort = new CorbaPort("MyConPro");
        m_MyConPro2Port = new CorbaPort("MyConPro2");
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
        // Set InPort buffers
        addInPort("in1", m_in1In);
        
        // Set OutPort buffer
        addOutPort("out1", m_out1Out);
        
        // Set service provider to Ports
        try {
            m_MySVProPort.registerProvider("myserviceP1", "MyService", m_myserviceP1);
            m_MySVPro2Port.registerProvider("myserviceP2", "MyService2", m_myserviceP2);
        } catch (ServantAlreadyActive e) {
            e.printStackTrace();
        } catch (WrongPolicy e) {
            e.printStackTrace();
        } catch (ObjectNotActive e) {
            e.printStackTrace();
        }
        
        // Set service consumers to Ports
        m_MyConProPort.registerConsumer("myservice0", "MyService", m_myservice0Base);
        m_MyConPro2Port.registerConsumer("myservice2", "DAQService", m_myservice2Base);
        
        // Set CORBA Service Ports
        addPort(m_MySVProPort);
        addPort(m_MySVPro2Port);
        addPort(m_MyConProPort);
        addPort(m_MyConPro2Port);
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
    protected TimedShort m_in1_val;
    protected DataRef<TimedShort> m_in1;
    /*!
     */
    protected InPort<TimedShort> m_in1In;
    
    // </rtc-template>
    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedLong m_out1_val;
    protected DataRef<TimedLong> m_out1;
    /*!
     */
    protected OutPort<TimedLong> m_out1Out;
    
    // </rtc-template>
    // CORBA Port declaration
    // <rtc-template block="corbaport_declare">
    /*!
     */
    protected CorbaPort m_MySVProPort;
    /*!
     */
    protected CorbaPort m_MySVPro2Port;
    /*!
     */
    protected CorbaPort m_MyConProPort;
    /*!
     */
    protected CorbaPort m_MyConPro2Port;
    
    // </rtc-template>
    // Service declaration
    // <rtc-template block="service_declare">
    /*!
     */
    protected MyServiceSVC_impl m_myserviceP1 = new MyServiceSVC_impl();
    /*!
     */
    protected MyService2SVC_impl m_myserviceP2 = new MyService2SVC_impl();
    
    // </rtc-template>
    // Consumer declaration
    // <rtc-template block="consumer_declare">
    protected CorbaConsumer<MyService> m_myservice0Base = new CorbaConsumer<MyService>(MyService.class);
    /*!
     */
    protected MyService m_myservice0;
    protected CorbaConsumer<DAQService> m_myservice2Base = new CorbaConsumer<DAQService>(DAQService.class);
    /*!
     */
    protected DAQService m_myservice2;
    
    // </rtc-template>
    private void initializeParam(Object target) {
        Class<?> targetClass = target.getClass();
        ClassLoader loader = target.getClass().getClassLoader();
        //
        Field[] fields = targetClass.getFields();
        for(Field field : fields) {
            if(field.getType().isPrimitive()) continue;
            
            try {
                if(field.getType().isArray()) {
                    Object arrayValue = null;
                    Class<?> clazz = null;
                    if(field.getType().getComponentType().isPrimitive()) {
                        clazz = field.getType().getComponentType();
                    } else {
                        clazz = loader.loadClass(field.getType().getComponentType().getName());
                    }
                    arrayValue = Array.newInstance(clazz, 0);
                    field.set(target, arrayValue);
                    
                } else {
                    Constructor<?>[] constList = field.getType().getConstructors();
                    if(constList.length==0) {
                        Method[] methodList = field.getType().getMethods();
                        for(Method method : methodList) {
                            if(method.getName().equals("from_int")==false) continue;
                            Object objFld = method.invoke(target, new Object[]{ new Integer(0) });
                            field.set(target, objFld);
                            break;
                        }
                        
                    } else {
                        Class<?> classFld = Class.forName(field.getType().getName(), true, loader);
                        Object objFld = classFld.newInstance();
                        initializeParam(objFld);
                        field.set(target, objFld);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
