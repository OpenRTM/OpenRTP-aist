#!/usr/bin/env python
# -*- coding: utf-8 -*-
# -*- Python -*-
# <rtc-template block="description">
"""
 @file foo.py
 @brief MDesc
 @date $Date$
 @author Noriaki Ando
 <n-ando@aist.go.jp>3456789412345678951234567896123456789712345678981234
 5
"""
# </rtc-template>
import sys
import time
sys.path.append(".")
# Import RTM module
import RTC
import OpenRTM_aist
import MyService_idl
import DAQService_idl
# Import Service implementation class
# <rtc-template block="service_impl">
from MyService_idl_example import *
# </rtc-template>
# Import Service stub modules
# <rtc-template block="consumer_import">
import _GlobalIDL, _GlobalIDL__POA
# </rtc-template>
# This module's spesification
# <rtc-template block="module_spec">
foo_spec = ["implementation_id", "foo", 
         "type_name",         "foo", 
         "description",       "MDesc", 
         "version",           "1.0.1", 
         "vendor",            "TA", 
         "category",          "Manip", 
         "activity_type",     "STATIC2", 
         "max_instance",      "5", 
         "language",          "Python", 
         "lang_type",         "SCRIPT",
         ""]
# </rtc-template>
# <rtc-template block="component_description">
##
# @class foo
# @brief MDesc
# 
# 
# </rtc-template>
class foo(OpenRTM_aist.DataFlowComponentBase):
	
    ##
    # @brief constructor
    # @param manager Maneger Object
    # 
    def __init__(self, manager):
        OpenRTM_aist.DataFlowComponentBase.__init__(self, manager)
        self._d_InP1 = OpenRTM_aist.instantiateDataType(RTC.TimedShort)
        """
        """
        self._InP1In = OpenRTM_aist.InPort("InP1", self._d_InP1)
        self._d_InP2 = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        """
        self._InP2In = OpenRTM_aist.InPort("InP2", self._d_InP2)
        self._d_OutP1 = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        """
        self._OutP1Out = OpenRTM_aist.OutPort("OutP1", self._d_OutP1)
        self._d_OutP2 = OpenRTM_aist.instantiateDataType(RTC.TimedFloat)
        """
        """
        self._OutP2Out = OpenRTM_aist.OutPort("OutP2", self._d_OutP2)
        """
        """
        self._svPortPort = OpenRTM_aist.CorbaPort("svPort")
        """
        """
        self._cmPortPort = OpenRTM_aist.CorbaPort("cmPort")
        """
        """
        self._acc = MyService_i()
		
        """
        """
        self._rate = OpenRTM_aist.CorbaConsumer(interfaceType=_GlobalIDL.DAQService)
        # initialize of configuration-data.
        # <rtc-template block="init_conf_param">
		
        # </rtc-template>
		 
    ##
    #
    # The initialize action (on CREATED->ALIVE transition)
    # 
    # @return RTC::ReturnCode_t
    # 
    #
    def onInitialize(self):
        # Bind variables and configuration variable
		
        # Set InPort buffers
        self.addInPort("InP1",self._InP1In)
        self.addInPort("InP2",self._InP2In)
		
        # Set OutPort buffers
        self.addOutPort("OutP1",self._OutP1Out)
        self.addOutPort("OutP2",self._OutP2Out)
		
        # Set service provider to Ports
        self._svPortPort.registerProvider("acc", "MyService", self._acc)
		
        # Set service consumers to Ports
        self._cmPortPort.registerConsumer("rate", "DAQService", self._rate)
		
        # Set CORBA Service Ports
        self.addPort(self._svPortPort)
        self.addPort(self._cmPortPort)
		
        return RTC.RTC_OK
	
    ###
    ## 
    ## The finalize action (on ALIVE->END transition)
    ## 
    ## @return RTC::ReturnCode_t
    ##
    ## 
    #def onFinalize(self):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The startup action when ExecutionContext startup
    ## 
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onStartup(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The shutdown action when ExecutionContext stop
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onShutdown(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The activated action (Active state entry action)
    ##
    ## @param ec_id target ExecutionContext Id
    ## 
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onActivated(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The deactivated action (Active state exit action)
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onDeactivated(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The execution action that is invoked periodically
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onExecute(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The aborting action when main logic error occurred.
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onAborting(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The error action in ERROR state
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onError(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The reset action that is invoked resetting
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onReset(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The state update action that is invoked after onExecute() action
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onStateUpdate(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ##
    ## The action that is invoked when execution context's rate is changed
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ##
    #def onRateChanged(self, ec_id):
    #
    #    return RTC.RTC_OK
	
def fooInit(manager):
    profile = OpenRTM_aist.Properties(defaults_str=foo_spec)
    manager.registerFactory(profile,
                            foo,
                            OpenRTM_aist.Delete)
def MyModuleInit(manager):
    fooInit(manager)
    # create instance_name option for createComponent()
    instance_name = [i for i in sys.argv if "--instance_name=" in i]
    if instance_name:
        args = instance_name[0].replace("--", "?")
    else:
        args = ""
  
    # Create a component
    comp = manager.createComponent("foo" + args)
def main():
    # remove --instance_name= option
    argv = [i for i in sys.argv if not "--instance_name=" in i]
    # Initialize manager
    mgr = OpenRTM_aist.Manager.init(sys.argv)
    mgr.setModuleInitProc(MyModuleInit)
    mgr.activateManager()
    mgr.runManager()
if __name__ == "__main__":
    main()
