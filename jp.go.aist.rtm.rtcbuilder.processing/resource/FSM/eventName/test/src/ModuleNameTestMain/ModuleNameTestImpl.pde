// -*- Java -*-
// <rtc-template block="description">
/*!
 * @file  ModuleNameTestImpl.java
 * @brief ModuleDescription
 * @date  $Date$
 *
 * $Id$
 */
// </rtc-template>
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import RTC.ReturnCode_t;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import RTC.TimedLong;
// <rtc-template block="component_description">
/**
 * @class ModuleNameTestImpl
 * @brief ModuleDescription
 *
 */
// </rtc-template>
public class ModuleNameTestImpl extends DataFlowComponentBase {
  /*!
   * @brief constructor
   * @param manager Maneger Object
   */
    public ModuleNameTestImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_Event01_02_val = new TimedLong();
        initializeParam(m_Event01_02_val);
        m_Event01_02 = new DataRef<TimedLong>(m_Event01_02_val);
        m_Event01_02Out = new OutPort<TimedLong>("Event01_02", m_Event01_02);
    
        m_Event02_Final_val = new TimedLong();
        initializeParam(m_Event02_Final_val);
        m_Event02_Final = new DataRef<TimedLong>(m_Event02_Final_val);
        m_Event02_FinalOut = new OutPort<TimedLong>("Event02_Final", m_Event02_Final);
    
        // </rtc-template>
    }
    /**
     *
     * The initialize action (on CREATED->ALIVE transition)
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onInitialize() {
        // Registration: InPort/OutPort/Service
        // <rtc-template block="registration">
        try {
            addOutPort("Event01_02", m_Event01_02Out);
            addOutPort("Event02_Final", m_Event02_FinalOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // </rtc-template>
        return super.onInitialize();
    }
    /***
     *
     * The finalize action (on ALIVE->END transition)
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
//    @Override
//    protected ReturnCode_t onFinalize() {
//        return super.onFinalize();
//    }
    /***
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
    /***
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
    /***
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
    /***
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
    /***
     *
     * The execution action that is invoked periodically
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onExecute(int ec_id) {
        System.out.println("Please select action!!");
        System.out.println("Commands: ");
        System.out.println("  1   : Event01_02");
        System.out.println("  2   : Event02_Final");
        System.out.print(">> ");
        BufferedReader buff = new BufferedReader(new InputStreamReader( System.in ));
        try {
            String cmd = buff.readLine();
            if(cmd == null) {
                return super.onExecute(ec_id);
            }
            cmd = cmd.trim();
            System.out.print("[command]: "+cmd);
            System.out.println("");
            if(cmd.equals("1")){
                m_Event01_02_val.data = 0;
                m_Event01_02Out.write();
            }
            else if(cmd.equals("2")){
                m_Event02_Final_val.data = 0;
                m_Event02_FinalOut.write();
            }
        }
        catch (Exception ex) {
            System.out.println("Input Error!");
        }
        return super.onExecute(ec_id);
    }
    /***
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
    /***
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
    /***
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
    /***
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
    /***
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
    // DataInPort declaration
    // <rtc-template block="inport_declare">
    
    // </rtc-template>
    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedLong m_Event01_02_val;
    protected DataRef<TimedLong> m_Event01_02;
    protected OutPort<TimedLong> m_Event01_02Out;
    
    protected TimedLong m_Event02_Final_val;
    protected DataRef<TimedLong> m_Event02_Final;
    protected OutPort<TimedLong> m_Event02_FinalOut;
    
    
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
