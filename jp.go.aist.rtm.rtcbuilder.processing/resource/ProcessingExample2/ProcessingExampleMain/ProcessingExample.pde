// -*- Java -*-
// <rtc-template block="description">
/*!
 * @file ProcessingExample.java
 * @date $Date$
 *
 * @author 作成者・連絡先
 *
 * ライセンス、使用条件
 *
 * $Id$
 */
// </rtc-template>

import jp.go.aist.rtm.RTC.Manager;
import jp.go.aist.rtm.RTC.RTObject_impl;
import jp.go.aist.rtm.RTC.RtcDeleteFunc;
import jp.go.aist.rtm.RTC.RtcNewFunc;
import jp.go.aist.rtm.RTC.RegisterModuleFunc;
import jp.go.aist.rtm.RTC.util.Properties;

//  Module specification
//  <rtc-template block="module_spec">
public static String processingexample_conf[] = {
        "implementation_id", "ProcessingExample",
        "type_name",         "ProcessingExample",
        "description",       "ModuleDescription",
        "version",           "1.0.0",
        "vendor",            "VenderName",
        "category",          "Category",
        "activity_type",     "STATIC",
        "max_instance",      "1",
        "language",          "Java",
        "lang_type",         "compile",
        // Configuration variables
        "conf.default.conf1", "0.0",
        "conf.default.conf2", "5",

        // Widget
        "conf.__widget__.conf1", "slider.0.1",
        "conf.__widget__.conf2", "text",
        // Constraints
        "conf.__constraints__.conf1", "-1.0<x<1.0",

        "conf.__type__.conf1", "double",
        "conf.__type__.conf2", "int",

	    ""
};
//  </rtc-template>

/**
 * ProcessingExample
 * <p> 
 * ModuleDescription
 */
public class ProcessingExample implements RtcNewFunc, RtcDeleteFunc, RegisterModuleFunc {

    public RTObject_impl createRtc(Manager mgr) {
        return new ProcessingExampleImpl(mgr);
    }

    public void deleteRtc(RTObject_impl rtcBase) {
        rtcBase = null;
    }
    public void registerModule() {
        Properties prop = new Properties(processingexample_conf);
        final Manager manager = Manager.instance();
        manager.registerFactory(prop, new ProcessingExample(), new ProcessingExample());
    }
}
