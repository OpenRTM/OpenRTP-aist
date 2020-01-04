﻿// -*- C++ -*-
/*!
 * @file  ModuleNameFSM.h
 * @date  $Date$
 * $Id$
 */

#ifndef MODULENAMEFSM_H
#define MODULENAMEFSM_H

#include <rtm/StaticFSM.h>
#include <rtm/idl/BasicDataTypeSkel.h>
#include <rtm/RTC.h>

class ModuleName;

namespace ModuleNameFsm {

  /*!
   * @if jp
   * @class TOP状態
   *
   *
   * @else
   * @brief TOP state
   *
   * @endif
   */
  FSM_TOPSTATE(Top) {
    // Top state variables (visible to all substates)
  
    FSM_STATE(Top);

    // Machine's event protocol
    /*!
     */
    virtual void Event01-02() {}
    
    /*!
     */
    virtual void Event02-Final() {}
    
  
   private:
     RTC::ReturnCode_t onInit() override;
     RTC::ReturnCode_t onEntry() override;
     RTC::ReturnCode_t onExit() override;
  };

  FSM_SUBSTATE(State01, Top) {
    FSM_STATE(State01);
  

    // Event handler
    void Event01-02() override;

    private:
      // RTC::ReturnCode_t onInit() override;
  };

  FSM_SUBSTATE(State02, Top) {
    FSM_STATE(State02);
  

    // Event handler
    void Event02-Final() override;

    private:
      // RTC::ReturnCode_t onInit() override;
  };


} //end namespace 'ModuleNameFSM'

#endif // MODULENAMEFSM_H