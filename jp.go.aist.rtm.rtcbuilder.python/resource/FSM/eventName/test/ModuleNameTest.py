#!/usr/bin/env python
# -*- coding: utf-8 -*-
# -*- Python -*-
# <rtc-template block="description">
"""
 @file ModuleNameTest.py
 @brief ModuleDescription
 @date $Date$
"""
# </rtc-template>
from __future__ import print_function
import sys
import time
sys.path.append(".")
# Import RTM module
import RTC
import OpenRTM_aist
# Import Service implementation class
# <rtc-template block="service_impl">
import ModuleName
# </rtc-template>
# Import Service stub modules
# <rtc-template block="consumer_import">
# </rtc-template>
# This module's spesification
# <rtc-template block="module_spec">
modulenametest_spec = ["implementation_id", "ModuleNameTest", 
         "type_name",         "ModuleNameTest", 
         "description",       "ModuleDescription", 
         "version",           "1.0.0", 
         "vendor",            "VenderName", 
         "category",          "Category", 
         "activity_type",     "STATIC", 
         "max_instance",      "1", 
         "language",          "Python", 
         "lang_type",         "SCRIPT",
         ""]
# </rtc-template>
# <rtc-template block="component_description">
##
# @class ModuleNameTest
# @brief ModuleDescription
# 
# 
# </rtc-template>
class ModuleNameTest(OpenRTM_aist.DataFlowComponentBase):
    
    ##
    # @brief constructor
    # @param manager Maneger Object
    # 
    def __init__(self, manager):
        OpenRTM_aist.DataFlowComponentBase.__init__(self, manager)
        
        self._d_Event01_02 = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        """
        self._Event01_02Out = OpenRTM_aist.OutPort("Event01_02", self._d_Event01_02)
        self._d_Event02_Final = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        """
        self._Event02_FinalOut = OpenRTM_aist.OutPort("Event02_Final", self._d_Event02_Final)
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
        
        # Set OutPort buffers
        self.addOutPort("Event01_02", self._Event01_02Out)
        self.addOutPort("Event02_Final", self._Event02_FinalOut)
        
        # Set service provider to Ports
        
        # Set service consumers to Ports
        
        # Set CORBA Service Ports
        
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
    
    def runTest(self):
        return True
def RunTest():
    manager = OpenRTM_aist.Manager.instance()
    comp = manager.getComponent("ModuleNameTest0")
    if comp is None:
        print('Component get failed.', file=sys.stderr)
        return False
    return comp.runTest()
def ModuleNameTestInit(manager):
    profile = OpenRTM_aist.Properties(defaults_str=modulenametest_spec)
    manager.registerFactory(profile,
                            ModuleNameTest,
                            OpenRTM_aist.Delete)
def MyModuleInit(manager):
    ModuleNameTestInit(manager)
    ModuleName.ModuleNameInit(manager)
    # Create a component
    comp = manager.createComponent("ModuleNameTest")
def main():
    mgr = OpenRTM_aist.Manager.init(sys.argv)
    mgr.setModuleInitProc(MyModuleInit)
    mgr.activateManager()
    mgr.runManager(True)
    ret = RunTest()
    mgr.shutdown()
    if ret:
        sys.exit(0)
    else:
        sys.exit(1)
if __name__ == "__main__":
    main()
