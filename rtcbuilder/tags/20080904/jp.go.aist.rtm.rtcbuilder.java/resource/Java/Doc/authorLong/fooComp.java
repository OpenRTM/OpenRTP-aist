// -*- Java -*-
/*!
 * @file fooComp.java
 * @brief Standalone component
 * @date $Date$
 *
 * @author Noriaki Ando <n-ando@aist.go.jp>3456789412345678951234567896123456789
 * 7123456789812345
 *
 * $Id$
 */

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.ModuleInitProc;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.util.Properties;

/*!
 * @class fooComp
 * @brief Standalone component Class
 *
 */
public class fooComp implements ModuleInitProc {

    public void myModuleInit(Manager mgr) {
      Properties prop = new Properties(foo.component_conf);
      mgr.registerFactory(prop, new foo(), new foo());

      // Create a component
      RTObject_impl comp = mgr.createComponent("foo");
      if( comp==null ) {
    	  System.err.println("Component create failed.");
    	  System.exit(0);
      }
      
      // Example
      // The following procedure is examples how handle RT-Components.
      // These should not be in this function.

//      // Get the component's object reference
//      Manager manager = Manager.instance();
//      RTObject rtobj = null;
//      try {
//          rtobj = RTObjectHelper.narrow(manager.getPOA().servant_to_reference(comp));
//      } catch (ServantNotActive e) {
//          e.printStackTrace();
//      } catch (WrongPolicy e) {
//          e.printStackTrace();
//      }
//
//      // Get the port list of the component
//      PortListHolder portlist = new PortListHolder();
//      portlist.value = rtobj.get_ports();
//
//      // getting port profiles
//      System.out.println( "Number of Ports: " );
//      System.out.println( portlist.value.length );
//      for( int intIdx=0;intIdx<portlist.value.length;++intIdx ) {
//          Port port = portlist.value[intIdx];
//          System.out.println( "Port" + intIdx + " (name): ");
//          System.out.println( port.get_port_profile().name );
//        
//          PortInterfaceProfileListHolder iflist = new PortInterfaceProfileListHolder();
//          iflist.value = port.get_port_profile().interfaces;
//          System.out.println( "---interfaces---" );
//          for( int intIdx2=0;intIdx2<iflist.value.length;++intIdx2 ) {
//              System.out.println( "I/F name: " );
//              System.out.println( iflist.value[intIdx2].instance_name  );
//              System.out.println( "I/F type: " );
//              System.out.println( iflist.value[intIdx2].type_name );
//              if( iflist.value[intIdx2].polarity==PortInterfacePolarity.PROVIDED ) {
//                  System.out.println( "Polarity: PROVIDED" );
//              } else {
//                  System.out.println( "Polarity: REQUIRED" );
//              }
//          }
//          System.out.println( "---properties---" );
//          NVUtil.dump( new NVListHolder(port.get_port_profile().properties) );
//          System.out.println( "----------------" );
//      }
    }

    public static void main(String[] args) {
        // Initialize manager
        final Manager manager = Manager.init(args);

        // Set module initialization proceduer
        // This procedure will be invoked in activateManager() function.
        fooComp init = new fooComp();
        manager.setModuleInitProc(init);

        // Activate manager and register to naming service
        manager.activateManager();

        // run the manager in blocking mode
        // runManager(false) is the default.
        manager.runManager();

        // If you want to run the manager in non-blocking mode, do like this
        // manager.runManager(true);
    }

}
