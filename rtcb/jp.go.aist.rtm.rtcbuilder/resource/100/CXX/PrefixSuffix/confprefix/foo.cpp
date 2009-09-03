// -*- C++ -*-
/*!
 * @file  foo.cpp
 * @brief MDesc
 * @date $Date$
 *
 * $Id$
 */

#include "foo.h"

// Module specification
// <rtc-template block="module_spec">
static const char* foo_spec[] =
  {
    "implementation_id", "foo",
    "type_name",         "foo",
    "description",       "MDesc",
    "version",           "1.0.1",
    "vendor",            "TA",
    "category",          "Manip",
    "activity_type",     "PERIODIC2",
    "kind",              "DataFlowComponent",
    "max_instance",      "5",
    "language",          "C++",
    "lang_type",         "compile",
    // Configuration variables
    "conf.default.int_param0", "0",
    ""
  };
// </rtc-template>

/*!
 * @brief constructor
 * @param manager Maneger Object
 */
foo::foo(RTC::Manager* manager)
    // <rtc-template block="initializer">
  : RTC::DataFlowComponentBase(manager),
    p_dtInP1Inds_s("InP1", p_dtInP1ds_s),
    p_dtInP2Inds_s("InP2", p_dtInP2ds_s),
    p_dtOutP1Outds_s("OutP1", p_dtOutP1ds_s),
    p_dtOutP2Outds_s("OutP2", p_dtOutP2ds_s),
    p_spsvPortPortss_s("svPort"),
    p_spcmPortPortss_s("cmPort")

    // </rtc-template>
{
  // Registration: InPort/OutPort/Service
  // <rtc-template block="registration">
  // Set InPort buffers
  registerInPort("InP1", p_dtInP1Inds_s);
  registerInPort("InP2", p_dtInP2Inds_s);
  
  // Set OutPort buffer
  registerOutPort("OutP1", p_dtOutP1Outds_s);
  registerOutPort("OutP2", p_dtOutP2Outds_s);
  
  // Set service provider to Ports
  p_spsvPortPortss_s.registerProvider("acc", "MyService", p_sipaccsis_s);
  
  // Set service consumers to Ports
  p_spcmPortPortss_s.registerConsumer("rate", "DAQService", p_sipratesis_s);
  
  // Set CORBA Service Ports
  registerPort(p_spsvPortPortss_s);
  registerPort(p_spcmPortPortss_s);
  
  // </rtc-template>

}

/*!
 * @brief destructor
 */
foo::~foo()
{
}



RTC::ReturnCode_t foo::onInitialize()
{
  // <rtc-template block="bind_config">
  // Bind variables and configuration variable
  bindParameter("int_param0", p_cpint_param0_s, "0");
  
  // </rtc-template>
  return RTC::RTC_OK;
}

/*
RTC::ReturnCode_t foo::onFinalize()
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onStartup(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onShutdown(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onActivated(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onDeactivated(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onExecute(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onAborting(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onError(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onReset(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onStateUpdate(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/

/*
RTC::ReturnCode_t foo::onRateChanged(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/



extern "C"
{
 
  void fooInit(RTC::Manager* manager)
  {
    coil::Properties profile(foo_spec);
    manager->registerFactory(profile,
                             RTC::Create<foo>,
                             RTC::Delete<foo>);
  }
  
};


