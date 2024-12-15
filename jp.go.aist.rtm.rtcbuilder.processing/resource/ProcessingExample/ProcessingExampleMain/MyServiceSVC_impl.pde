// -*-Java-*-
/*!
 * @file  MyServiceSVC_impl.java
 * @brief Service implementation code of MyService.idl
 *
 * @author 作成者・連絡先
 *
 * ライセンス、使用条件
 *
 */
import SimpleService.MyServicePOA;
/**
 * MyServiceSVC_impl
 * <p>
 * Example class implementing IDL interface SimpleService::MyService
 */
public class MyServiceSVC_impl extends MyServicePOA{
    
    public MyServiceSVC_impl() {
        // Please add extra constructor code here.
    }

    /*
     * Methods corresponding to IDL attributes and operations
     */
    public String echo(String msg) {
        // Please insert your code here and remove the following warning pragma
        // TODO "Code missing in function <String echo(String msg)>"
        return null;
    }

    public String[] get_echo_history() {
        // Please insert your code here and remove the following warning pragma
        // TODO "Code missing in function <EchoList get_echo_history()>"
        return null;
    }

    public void set_value(float value) {
        // Please insert your code here and remove the following warning pragma
        // TODO "Code missing in function <void set_value(float value)>"
    }

    public float get_value() {
        // Please insert your code here and remove the following warning pragma
        // TODO "Code missing in function <float get_value()>"
        return 0;
    }

    public float[] get_value_history() {
        // Please insert your code here and remove the following warning pragma
        // TODO "Code missing in function <ValueList get_value_history()>"
        return null;
    }

//  End of example implementational code
}
