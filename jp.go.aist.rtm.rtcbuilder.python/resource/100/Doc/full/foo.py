#!/usr/bin/env python
# -*- coding: utf-8 -*-
# -*- Python -*-
# <rtc-template block="description">
"""
 @file foo.py
 @brief MDesc
 @date $Date$
 @author Noriaki Ando <n-ando@aist.go.jp>
 Copyright (C) 2006-2008 ライセンス
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
         "conf.default.int_param0", "0",
         "conf.default.int_param1", "1",
         "conf.default.double_param0", "0.11",
         "conf.default.str_param0", "hoge",
         "conf.default.str_param1", "dara",
         "conf.__type__.int_param0", "int",
         "conf.__type__.int_param1", "int",
         "conf.__type__.double_param0", "double",
         "conf.__type__.str_param0", "String",
         "conf.__type__.str_param1", "String",
         ""]
# </rtc-template>
# <rtc-template block="component_description">
##
# @class foo
# @brief MDesc
# 
# 本コンポーネントの概要説明
# 
# 本コンポーネントの入出力
# 
# 本コンポーネントのアルゴリズムなど
# 
# 参考文献の情報
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
        InPort1の概要
         - Type: InPort1のデータの型
         - Number: InPort1のデータの数
         - Semantics: InPort1のデータの意味
         - Unit: InPort1のデータの単位
         - Frequency: InPort1のデータの発生頻度
         - Operation Cycle: InPort1のデータの処理周期
        """
        self._InP1In = OpenRTM_aist.InPort("InP1", self._d_InP1)
        self._d_InP2 = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        InPort2の概要
         - Type: InPort2のデータの型
         - Number: InPort2のデータの数
         - Semantics: InPort2のデータの意味
         - Unit: InPort2のデータの単位
         - Frequency: InPort2のデータの発生頻度
         - Operation Cycle: InPort2のデータの処理周期
        """
        self._InP2In = OpenRTM_aist.InPort("InP2", self._d_InP2)
        self._d_OutP1 = OpenRTM_aist.instantiateDataType(RTC.TimedLong)
        """
        OutPort1の概要
         - Type: OutPort1のデータの型
         - Number: OutPort1のデータの数
         - Semantics: OutPort1のデータの意味
         - Unit: OutPort1のデータの単位
         - Frequency: OutPort1のデータの発生頻度
         - Operation Cycle: OutPort1のデータの処理周期
        """
        self._OutP1Out = OpenRTM_aist.OutPort("OutP1", self._d_OutP1)
        self._d_OutP2 = OpenRTM_aist.instantiateDataType(RTC.TimedFloat)
        """
        OutPort2の概要
         - Type: OutPort2のデータの型
         - Number: OutPort2のデータの数
         - Semantics: OutPort2のデータの意味
         - Unit: OutPort2のデータの単位
         - Frequency: OutPort2のデータの発生頻度
         - Operation Cycle: OutPort2のデータの処理周期
        """
        self._OutP2Out = OpenRTM_aist.OutPort("OutP2", self._d_OutP2)
        """
        ServicePort1の概要
        Interface: ServicePort1のインターフェースの概要
        """
        self._svPortPort = OpenRTM_aist.CorbaPort("svPort")
        """
        ServicePort2の概要
        Interface: ServicePort2のインターフェースの概要
        """
        self._cmPortPort = OpenRTM_aist.CorbaPort("cmPort")
        """
        ServiceIF1の概要説明
         - Argument:      ServiceIF1の引数
         - Return Value:  ServiceIF1の返値
         - Exception:     ServiceIF1の例外
         - PreCondition:  ServiceIF1の事前条件
         - PostCondition: ServiceIF1の事後条件
        """
        self._acc = MyService_i()
		
        """
        ServiceIF2の概要説明
         - Argument:      ServiceIF2の引数
         - Return Value:  ServiceIF2の返値
         - Exception:     ServiceIF2の例外
         - PreCondition:  ServiceIF2の事前条件
         - PostCondition: ServiceIF2の事後条件
        """
        self._rate = OpenRTM_aist.CorbaConsumer(interfaceType=_GlobalIDL.DAQService)
        # initialize of configuration-data.
        # <rtc-template block="init_conf_param">
        """
        Config1の概要
         - Name: Config1の名前 int_param0
         - DefaultValue: 0
         - Unit: Config1の単位
         - Range: Config1の範囲
         - Constraint: Config1の制約条件
        """
        self._int_param0 = [0]
        """
        Config2の概要
         - Name: Config2の名前 int_param1
         - DefaultValue: 1
         - Unit: Config2の単位
         - Range: Config2の範囲
         - Constraint: Config2の制約条件
        """
        self._int_param1 = [1]
        """
        Config3の概要
         - Name: Config3の名前 double_param0
         - DefaultValue: 0.11
         - Unit: Config3の単位
         - Range: Config3の範囲
         - Constraint: Config3の制約条件
        """
        self._double_param0 = [0.11]
        """
        Config4の概要
         - Name: Config4の名前 str_param0
         - DefaultValue: hoge
         - Unit: Config4の単位
         - Range: Config4の範囲
         - Constraint: Config4の制約条件
        """
        self._str_param0 = ['hoge']
        """
        Config5の概要
         - Name: Config5の名前 str_param1
         - DefaultValue: dara
         - Unit: Config5の単位
         - Range: Config5の範囲
         - Constraint: Config5の制約条件
        """
        self._str_param1 = ['dara']
		
        # </rtc-template>
		 
    ##
    # on_initialize概要説明
    #
    # The initialize action (on CREATED->ALIVE transition)
    # 
    # @return RTC::ReturnCode_t
    # 
    # @pre on_initialize事前条件
    # @post on_initialize事後条件
    #
    def onInitialize(self):
        # Bind variables and configuration variable
        self.bindParameter("int_param0", self._int_param0, "0")
        self.bindParameter("int_param1", self._int_param1, "1")
        self.bindParameter("double_param0", self._double_param0, "0.11")
        self.bindParameter("str_param0", self._str_param0, "hoge")
        self.bindParameter("str_param1", self._str_param1, "dara")
		
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
    ## on_finalize概要説明
    ## 
    ## The finalize action (on ALIVE->END transition)
    ## 
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_finalize事前条件
    ## @post on_finalize事後条件
    ## 
    #def onFinalize(self):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_startup概要説明
    ##
    ## The startup action when ExecutionContext startup
    ## 
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_startup事前条件
    ## @post on_startup事後条件
    ##
    #def onStartup(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_shutdown概要説明
    ##
    ## The shutdown action when ExecutionContext stop
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_shutdown事前条件
    ## @post on_shutdown事後条件
    ##
    #def onShutdown(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_activated概要説明
    ##
    ## The activated action (Active state entry action)
    ##
    ## @param ec_id target ExecutionContext Id
    ## 
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_activated事前条件
    ## @post on_activated事後条件
    ##
    #def onActivated(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_deactivated概要説明
    ##
    ## The deactivated action (Active state exit action)
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_deactivated事前条件
    ## @post on_deactivated事後条件
    ##
    #def onDeactivated(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_execute概要説明
    ##
    ## The execution action that is invoked periodically
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_execute事前条件
    ## @post on_execute事後条件
    ##
    #def onExecute(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_aborting概要説明
    ##
    ## The aborting action when main logic error occurred.
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_aborting事前条件
    ## @post on_aborting事後条件
    ##
    #def onAborting(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_error概要説明
    ##
    ## The error action in ERROR state
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_error事前条件
    ## @post on_error事後条件
    ##
    #def onError(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_reset概要説明
    ##
    ## The reset action that is invoked resetting
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_reset事前条件
    ## @post on_reset事後条件
    ##
    #def onReset(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_state_update概要説明
    ##
    ## The state update action that is invoked after onExecute() action
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_state_update事前条件
    ## @post on_state_update事後条件
    ##
    #def onStateUpdate(self, ec_id):
    #
    #    return RTC.RTC_OK
	
    ###
    ## on_rate_changed概要説明
    ##
    ## The action that is invoked when execution context's rate is changed
    ##
    ## @param ec_id target ExecutionContext Id
    ##
    ## @return RTC::ReturnCode_t
    ##
    ## @pre on_rate_changed事前条件
    ## @post on_rate_changed事後条件
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
