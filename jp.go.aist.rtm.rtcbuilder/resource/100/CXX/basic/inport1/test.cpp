// -*- C++ -*-
// <rtc-template block="description">
/*!
 * @file  test.cpp
 * @brief MDesc
 * @date $Date$
 *
 * $Id$
 */
// </rtc-template>
#include "test.h"
// Module specification
// <rtc-template block="module_spec">
static const char* const test_spec[] =
  {
    "implementation_id", "test",
    "type_name",         "test",
    "description",       "MDesc",
    "version",           "1.0.1",
    "vendor",            "TA",
    "category",          "Manip",
    "activity_type",     "PERIODIC2",
    "kind",              "DataFlowComponent",
    "max_instance",      "5",
    "language",          "C++",
    "lang_type",         "compile",
    ""
  };
// </rtc-template>
/*!
 * @brief constructor
 * @param manager Maneger Object
 */
test::test(RTC::Manager* manager)
    // <rtc-template block="initializer">
  : RTC::DataFlowComponentBase(manager),
    m_InP1In("InP1", m_InP1)
    // </rtc-template>
{
}
/*!
 * @brief destructor
 */
test::~test()
{
}
RTC::ReturnCode_t test::onInitialize()
{
  // Registration: InPort/OutPort/Service
  // <rtc-template block="registration">
  // Set InPort buffers
  addInPort("InP1", m_InP1In);
  // Set OutPort buffer
  // Set service provider to Ports
  // Set service consumers to Ports
  // Set CORBA Service Ports
  // </rtc-template>
  // <rtc-template block="bind_config">
  // </rtc-template>
  return RTC::RTC_OK;
}
/*
RTC::ReturnCode_t test::onFinalize()
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onStartup(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onShutdown(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onActivated(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onDeactivated(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onExecute(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onAborting(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onError(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onReset(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onStateUpdate(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
/*
RTC::ReturnCode_t test::onRateChanged(RTC::UniqueId ec_id)
{
  return RTC::RTC_OK;
}
*/
extern "C"
{
  void testInit(RTC::Manager* manager)
  {
    coil::Properties profile(test_spec);
    manager->registerFactory(profile,
                             RTC::Create<test>,
                             RTC::Delete<test>);
  }
};
