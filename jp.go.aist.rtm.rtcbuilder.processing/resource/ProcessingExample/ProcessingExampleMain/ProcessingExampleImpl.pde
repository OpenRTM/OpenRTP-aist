// -*- Java -*-
// <rtc-template block="description">
/*!
 * @file  ProcessingExampleImpl.java
 * @brief ModuleDescription
 * @date  $Date$
 *
 * @author 作成者・連絡先
 *
 * ライセンス、使用条件
 *
 * $Id$
 */
// </rtc-template>

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import RTC.TimedDoubleSeq;
import RTC.TimedPose2D;
import RTC.TimedPose3D;
import RTC.TimedPoint3D;
import jp.go.aist.rtm.RTC.DataFlowComponentBase;
import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.port.InPort;
import jp.go.aist.rtm.RTC.port.OutPort;
import jp.go.aist.rtm.RTC.util.DataRef;
import jp.go.aist.rtm.RTC.port.CorbaPort;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import jp.go.aist.rtm.RTC.util.DoubleHolder;
import jp.go.aist.rtm.RTC.util.IntegerHolder;
import RTC.ReturnCode_t;

// <rtc-template block="component_description">
/**
 * ProcessingExampleImpl
 * <p>
 * ModuleDescription
 *
 * 概要説明
 *
 * 入出力
 *
 * アルゴリズム
 *
 * 参考文献
 *
 */
// </rtc-template>
public class ProcessingExampleImpl extends DataFlowComponentBase {

  /**
   * constructor
   * @param manager Manager Object
   */
    public ProcessingExampleImpl(Manager manager) {  
        super(manager);
        // <rtc-template block="initializer">
        m_in1_val = new TimedDoubleSeq();
        initializeParam(m_in1_val);
        m_in1 = new DataRef<TimedDoubleSeq>(m_in1_val);
        m_in1In = new InPort<TimedDoubleSeq>("in1", m_in1);
        m_in2_val = new TimedPose2D();
        initializeParam(m_in2_val);
        m_in2 = new DataRef<TimedPose2D>(m_in2_val);
        m_in2In = new InPort<TimedPose2D>("in2", m_in2);
        m_out1_val = new TimedPose3D();
        initializeParam(m_out1_val);
        m_out1 = new DataRef<TimedPose3D>(m_out1_val);
        m_out1Out = new OutPort<TimedPose3D>("out1", m_out1);
        m_out2_val = new TimedPoint3D();
        initializeParam(m_out2_val);
        m_out2 = new DataRef<TimedPoint3D>(m_out2_val);
        m_out2Out = new OutPort<TimedPoint3D>("out2", m_out2);
        m_MyServicePort = new CorbaPort("MyService");
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
        // Set InPort buffers
        addInPort("in1", m_in1In);
        addInPort("in2", m_in2In);
        
        // Set OutPort buffer
        addOutPort("out1", m_out1Out);
        addOutPort("out2", m_out2Out);
        
        // Set service provider to Ports
        try {
            m_MyServicePort.registerProvider("myservice", "SimpleService.MyService", m_myservice);
        } catch (ServantAlreadyActive e) {
            e.printStackTrace();
        } catch (WrongPolicy e) {
            e.printStackTrace();
        } catch (ObjectNotActive e) {
            e.printStackTrace();
        }
        
        // Set service consumers to Ports
        
        // Set CORBA Service Ports
        addPort(m_MyServicePort);
        // </rtc-template>
        bindParameter("conf1", m_conf1, "0.0");
        bindParameter("conf2", m_conf2, "5");

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
     * onActivatedの動作概要
     *
     * The activated action (Active state entry action)
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onActivated(int ec_id) {
        return super.onActivated(ec_id);
    }

    /**
     * onDeactivatedの動作概要
     *
     * The deactivated action (Active state exit action)
     *
     * @param ec_id target ExecutionContext Id
     *
     * @return RTC::ReturnCode_t
     * 
     * 
     */
    @Override
    protected ReturnCode_t onDeactivated(int ec_id) {
        return super.onDeactivated(ec_id);
    }

    /**
     * onExecuteの動作概要
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
     * 概要説明
     * - Name: データ名 conf1
     * - DefaultValue: 0.0
     * - Unit: 単位
     * - Range: データ範囲
     * - Constraint: 制約条件
     */
    protected DoubleHolder m_conf1 = new DoubleHolder();
    /*!
     * 
     * - Name:  conf2
     * - DefaultValue: 5
     */
    protected IntegerHolder m_conf2 = new IntegerHolder();
    // </rtc-template>


    /**
     */
    // DataInPort declaration
    // <rtc-template block="inport_declare">
    protected TimedDoubleSeq m_in1_val;
    protected DataRef<TimedDoubleSeq> m_in1;
    /*!
     * 概要説明
     * - Type: データ型
     * - Number: データ数
     * - Semantics: 詳細説明
     * - Unit: 単位
     * - Frequency: 発生頻度、周期
     * - Operation Cycle: 処理頻度、周期
     */
    protected InPort<TimedDoubleSeq> m_in1In;

    protected TimedPose2D m_in2_val;
    protected DataRef<TimedPose2D> m_in2;
    /*!
     */
    protected InPort<TimedPose2D> m_in2In;

    
    // </rtc-template>

    // DataOutPort declaration
    // <rtc-template block="outport_declare">
    protected TimedPose3D m_out1_val;
    protected DataRef<TimedPose3D> m_out1;
    /*!
     * 概要説明
     * - Type: データ型
     * - Number: データ数
     * - Semantics: 詳細説明
     * - Unit: 単位
     * - Frequency: 発生頻度、周期
     * - Operation Cycle: 処理頻度、周期
     */
    protected OutPort<TimedPose3D> m_out1Out;

    protected TimedPoint3D m_out2_val;
    protected DataRef<TimedPoint3D> m_out2;
    /*!
     */
    protected OutPort<TimedPoint3D> m_out2Out;

    
    // </rtc-template>

    // CORBA Port declaration
    // <rtc-template block="corbaport_declare">
    /*!
     * 概要説明
     * Interface: I/F概要説明
     */
    protected CorbaPort m_MyServicePort;
    
    // </rtc-template>

    // Service declaration
    // <rtc-template block="service_declare">
    /*!
     * 概要説明
     * - Argument:      引数
     * - Return Value:  戻り値
     * - Exception:     例外
     */
    protected MyServiceSVC_impl m_myservice = new MyServiceSVC_impl();
    
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
    void setPApplet(PApplet applet) {
      m_applet = applet;
    }
    
    private PApplet m_applet;
}
