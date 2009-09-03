package RTC;


/**
* RTC/LightweightRTObjectPOA.java .
* IDL-to-Java コンパイラ (ポータブル), バージョン "3.1" で生成
* 生成元: svn/jp.go.aist.rtm.systemeditor/idl/RTC10.idl
* 2008年12月4日 (木曜日) 14時12分46秒 JST
*/


/*!
   * @if jp
   * @brief 
   * @else
   * @brief LightweightRTObject
   * 
   * @section Description
   *
   * This interface is realized by all lightweight RTCs (as required
   * by the lightweightRTComponent stereotype). It defines the states
   * and transitions through which all RTCs will pass from the time
   * they are created until the time they are destroyed.
   *
   * @section Semantics
   * @subsection Initialization
   *
   * An RTC begins in the Created state; at this point, it has been
   * instantiated but not yet fully initialized. Note that this state
   * is highly implementation-dependent. For example, it may
   * correspond to the invocation of a constructor in languages that
   * support that concept, but not all languages do. Furthermore, how
   * soon this state is entered before initialize is invoked is
   * implementation-dependent. Therefore, it should be relied on by
   * RTC implementers only to the minimum extent possible.  An RTC
   * that has completed its initialization and has not been finalized
   * is said to be Alive.
   *
   * @subsection Execution Context
   *
   * An RTC in the Alive state may participate in any number of
   * execution contexts (see Section 5.2.2.5 ). These contexts shall
   * be represented to an RTC as distinct instances of the
   * ExecutionContext class. The ExecutionContext manages the behavior
   * of each RTC that participates in it. This relationship is defined
   * by the following state machine, which is embedded within the
   * ExecutionContext's own lifecycle (see Figure 5.5 ). Each
   * participating RTC is represented as a separate parallel region.
   *
   * Relative to a given execution context, an RTC may either be
   * Active, Inactive, or in Error. When the RTC is Active in a
   * Running execution context, the ComponentAction callbacks (see
   * Section 5.2.2.4) shall be invoked as appropriate for the contexta?
   * s ExecutionKind. The callbacks shall not be invoked relative to
   * that context when either the RTC is Inactive in that context or
   * the context is Stopped. (Note that starting and stopping an
   * execution context shall not impact whether its participating RTCs
   * are Active or Inactive.)  It may be that a given RTC does not
   * directly participate in any execution contexts. Such an RTC is
   * referred to as passive.  A passive RTC may provide services to
   * other components upon request. At any other time, it shall not be
   * required to perform any ongoing activity of its own; therefore,
   * instances of such an RTC typically exist only as parts (directly
   * or indirectly) of a containing active RTC.
   *
   * @subsection Error Handling
   *
   * If an operation fails while the RTC is Active in a given
   * execution context, the RTC will transition to the Error state
   * corresponding to that context. While the RTC is in Error, the
   * ComponentAction::on_error callback will be invoked in place of
   * those callbacks that would otherwise have been invoked according
   * to the contexta?s ExecutionKind. For example, if the kind is
   * PERIODIC, on_error shall be invoked instead of the pair of
   * on_execute, and on_state_update.  When an RTC is in Error, it may
   * be reset. If resetting is successful, the RTC shall return to the
   * Inactive state. If resetting is unsuccessful, it shall remain in
   * the Error state.
   * 
   * @endif
   */
public abstract class LightweightRTObjectPOA extends org.omg.PortableServer.Servant
 implements RTC.LightweightRTObjectOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("initialize", new java.lang.Integer (0));
    _methods.put ("finalize", new java.lang.Integer (1));
    _methods.put ("is_alive", new java.lang.Integer (2));
    _methods.put ("exit", new java.lang.Integer (3));
    _methods.put ("attach_context", new java.lang.Integer (4));
    _methods.put ("detach_context", new java.lang.Integer (5));
    _methods.put ("get_context", new java.lang.Integer (6));
    _methods.put ("get_owned_contexts", new java.lang.Integer (7));
    _methods.put ("get_participating_contexts", new java.lang.Integer (8));
    _methods.put ("get_context_handle", new java.lang.Integer (9));
    _methods.put ("on_initialize", new java.lang.Integer (10));
    _methods.put ("on_finalize", new java.lang.Integer (11));
    _methods.put ("on_startup", new java.lang.Integer (12));
    _methods.put ("on_shutdown", new java.lang.Integer (13));
    _methods.put ("on_activated", new java.lang.Integer (14));
    _methods.put ("on_deactivated", new java.lang.Integer (15));
    _methods.put ("on_aborting", new java.lang.Integer (16));
    _methods.put ("on_error", new java.lang.Integer (17));
    _methods.put ("on_reset", new java.lang.Integer (18));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  /*!
     * @if jp
     * @brief 
     * @else
     * @brief initialize
     *
     * @section Description
     * Initialize the RTC that realizes this interface.
     *
     * @section Semantics
     * The invocation of this operation shall result in the invocation
     * of the callback ComponentAction::on_initialize.
     *
     * @section Constraints
     *
     * - An RTC may be initialized only while it is in the Created
     *   state. Any attempt to invoke this operation while in another
     *   state shall fail with ReturnCode_t::PRECONDITION_NOT_MET.
     * - Application developers are not expected to call this operation
     *   directly; it exists for use by the RTC infrastructure.
     *
     * @endif
     */
       case 0:  // RTC/LightweightRTObject/initialize
       {
         RTC.ReturnCode_t $result = null;
         $result = this.initialize ();
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief finalize
     *
     * @section Description
     *
     * Finalize the RTC that realizes this interface, preparing it for
     * destruction.
     *
     * @section Semantics
     *
     * This invocation of this operation shall result in the
     * invocation of the callback ComponentAction::on_finalize
     *
     * @section Constraints
     *
     * - An RTC may not be finalized while it is participating in any
     *   execution context. It must first be removed with
     *   ExecutionContextOperations::remove_component. Otherwise, this
     *   operation shall fail with
     *   ReturnCode_t::PRECONDITION_NOT_MET. See Figure 5.9.
     *
     * - An RTC may not be finalized while it is in the Created state. Any
     *   attempt to invoke this operation while in that state shall fail with
     *   ReturnCode_t::PRECONDITION_NOT_MET.
     *
     * - Application developers are not expected to call this operation
     *   directly; it exists for use by the RTC infrastructure.
     *
     * @endif
     */
       case 1:  // RTC/LightweightRTObject/_finalize
       {
         RTC.ReturnCode_t $result = null;
         $result = this._finalize ();
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief is_alive
     * 
     * @section Description
     *
     * A component is alive or not regardless of the execution context
     * from which it is observed. However, whether or not it is
     * Active, Inactive, or in Error is dependent on the execution
     * context(s) (see Figure 5.7) in which it is running. That is, it
     * may be Active in one context but Inactive in
     * another. Therefore, this operation shall report whether this
     * RTC is either Active, Inactive, or in Error; which of those
     * states a component is in with respect to a particular context
     * may be queried from the context itself.
     *
     * @endif
     *
     */
       case 2:  // RTC/LightweightRTObject/is_alive
       {
         RTC.ExecutionContext exec_context = RTC.ExecutionContextHelper.read (in);
         boolean $result = false;
         $result = this.is_alive (exec_context);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief exit
     *
     * @section Description
     *
     * Stop the RTCa?s execution context(s) and finalize it along with
     * its contents.
     *
     * @section Semantics
     *
     * Any execution contexts for which the RTC is the owner shall be
     * stopped.  If the RTC participates in any execution contexts
     * belonging to another RTC that contains it, directly or
     * indirectly (i.e., the containing RTC is the owner of the
     * ExecutionContext), it shall be deactivated in those contexts.
     * After the RTC is no longer Active in any Running execution
     * context, it and any RTCs contained transitively within it shall
     * be finalized.
     *
     * @section Constraints
     *
     * An RTC cannot be exited if it has not yet been initialized. Any
     * attempt to exit an RTC that is in the Created state shall fail
     * with ReturnCode_t::PRECONDITION_NOT_MET.  
     *
     * @endif
     */
       case 3:  // RTC/LightweightRTObject/exit
       {
         RTC.ReturnCode_t $result = null;
         $result = this.exit ();
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief attach_context
     *
     * @section Description
     *
     * Inform this RTC that it is participating in the given execution
     * context. Return a handle that represents the association of
     * this RTC with the context.
     *
     * @section Semantics
     *
     * This operation is intended to be invoked by
     * ExecutionContextOperations::add_component (see Section
     * 5.2.2.6.6). It is not intended for use by other clients.
     *
     * @endif
     */
       case 4:  // RTC/LightweightRTObject/attach_context
       {
         RTC.ExecutionContext exec_context = RTC.ExecutionContextHelper.read (in);
         int $result = (int)0;
         $result = this.attach_context (exec_context);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief detach_context
     * @section Description
     * 
     * Inform this RTC that it is no longer participating in the given
     * execution context.
     *
     * @section Semantics
     * 
     * This operation is intended to be invoked by
     * ExecutionContextOperations::remove_component (see Section
     * 5.2.2.6.7). It is not intended for use by other clients.
     *
     * @section Constraints
     *
     * - This operation may not be invoked if this RTC is not already
     *   participating in the execution context. Such a call shall fail
     *   with ReturnCode_t::PRECONDITION_NOT_MET.
     *
     * - This operation may not be invoked if this RTC is Active in
     *   the indicated execution context. Otherwise, it shall fail with
     *   ReturnCode_t::PRECONDITION_NOT_MET.
     *
     * @endif
     */
       case 5:  // RTC/LightweightRTObject/detach_context
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.detach_context (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief get_context
     *
     * @section Description
     *
     * Obtain a reference to the execution context represented by the
     * given handle.
     *
     * @section Semantics
     *
     * The mapping from handle to context is specific to a particular
     * RTC instance. The given handle must have been obtained by a
     * previous call to attach_context on this RTC.
     *
     * @endif
     */
       case 6:  // RTC/LightweightRTObject/get_context
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ExecutionContext $result = null;
         $result = this.get_context (exec_handle);
         out = $rh.createReply();
         RTC.ExecutionContextHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief get_owned_contexts
     *
     * @section Description 
     *
     * dsd? RTC dl}??mdyd? ExecutionContext d?e?eye?d?9?dyd?ac
     * 
     * @else
     * @brief get_owned_contexts
     *
     * @section Description 
     *
     * This operation returns a list of all execution contexts owned
     * by this RTC.
     *
     * @endif
     */
       case 7:  // RTC/LightweightRTObject/get_owned_contexts
       {
         RTC.ExecutionContext $result[] = null;
         $result = this.get_owned_contexts ();
         out = $rh.createReply();
         RTC.ExecutionContextListHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief get_participating_contexts
     *  
     * @section Description
     *
     * dsd? RTC dl{rr?dwd?ddd?dyd?d?d? ExecutionContext d?e?eye?d?9?dyd?ac
     *
     * @section Semantics
     *
     * dsd?e?eye?d?t?d?d?d?|?y?ese??emeye?d?abattach_context dlx?d?}?
     * dud?d?dtd?d?abe?eye?d???r?dud?abdetach_context dlx?d?}?dud?d?dt
     * d?d?abe?eye?dkd??}?dud?d?ac
     *
     * @else
     * @brief* get_participating_contexts
     *
     * @section Description
     *
     * This operation returns a list of all execution contexts in
     * which this RTC participates.
     *
     * @section Semantics
     *
     * Each call to attach_context causes the provided context to be
     * added to this list. Each call to detach_context causes the
     * provided context to be removed from this list.
     *
     * @endif
     */
       case 8:  // RTC/LightweightRTObject/get_participating_contexts
       {
         RTC.ExecutionContext $result[] = null;
         $result = this.get_participating_contexts ();
         out = $rh.createReply();
         RTC.ExecutionContextListHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * ### [x?b] RTC.idl d?d?t?d?d?d?ddd?dddlPIMd?d?t?d?d?d?ddd?ac
     * ### PIMdl?udwddac
     *
     * @brief 
     * @else
     * @brief get_context_handle
     *
     * @section Description
     * 
     * This operation returns a handle that is associated with the given
     * execution context.
     *
     * @endif
     */
       case 9:  // RTC/LightweightRTObject/get_context_handle
       {
         RTC.ExecutionContext cxt = RTC.ExecutionContextHelper.read (in);
         int $result = (int)0;
         $result = this.get_context_handle (cxt);
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_initialize
     *
     * @section Description
     *
     * The RTC has been initialized and entered the Alive state.
     *
     * @section Semantics
     *
     * Any RTC-specific initialization logic should be performed here.
     *
     * @endif
     */
       case 10:  // RTC/ComponentAction/on_initialize
       {
         RTC.ReturnCode_t $result = null;
         $result = this.on_initialize ();
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_finalize
     *
     * @section Description
     *
     * The RTC is being destroyed.
     *
     * @section Semantics
     *
     * Any final RTC-specific tear-down logic should be performed here.
     *
     * @endif
     */
       case 11:  // RTC/ComponentAction/on_finalize
       {
         RTC.ReturnCode_t $result = null;
         $result = this.on_finalize ();
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_startup
     *
     * @section Description
     *
     * The given execution context, in which the RTC is participating,
     * has transitioned from Stopped to Running.
     *
     * @endif
     */
       case 12:  // RTC/ComponentAction/on_startup
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_startup (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_shutdown
     *
     * @section Description
     *
     * The given execution context, in which the RTC is participating,
     * has transitioned from Running to Stopped.
     *
     * @endif
     */
       case 13:  // RTC/ComponentAction/on_shutdown
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_shutdown (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_activated
     *
     * @section Description
     *
     * The RTC has been activated in the given execution context.
     *
     * @endif
     */
       case 14:  // RTC/ComponentAction/on_activated
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_activated (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_deactivated
     *
     * @section Description
     *
     * The RTC has been deactivated in the given execution context.
     *
     * @endif
     */
       case 15:  // RTC/ComponentAction/on_deactivated
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_deactivated (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_aborting
     *
     * @section Description
     *
     * The RTC is transitioning from the Active state to the Error
     * state in some execution context.
     *
     * @section Semantics
     *
     * This callback is invoked only a single time for time that the
     * RTC transitions into the Error state from another state. This
     * behavior is in contrast to that of on_error.  
     *
     * @endif
     */
       case 16:  // RTC/ComponentAction/on_aborting
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_aborting (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_error
     *
     * @section Description
     *
     * The RTC remains in the Error state.
     *
     * @section Semantics
     *
     * If the RTC is in the Error state relative to some execution
     * context when it would otherwise be invoked from that context
     * (according to the contexta?s ExecutionKind), this callback
     * shall be invoked instead. For example,
     *
     * - If the ExecutionKind is PERIODIC, this operation shall be
     *   invoked in sorted order at the rate of the context instead of
     *   DataFlowComponentAction::on_execute and on_state_update.
     *
     * - If the ExecutionKind is EVENT_DRIVEN, this operation shall be
     *   invoked whenever FsmParticipantAction::on_action would
     *   otherwise have been invoked.
     *
     * @endif
     */
       case 17:  // RTC/ComponentAction/on_error
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_error (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }


  /*!
     * @if jp
     * @brief 
     * @else
     * @brief on_reset
     *
     * @section Description
     *
     * The RTC is in the Error state. An attempt is being made to
     * recover it such that it can return to the Inactive state.
     *
     * @section Semantics
     *
     * If the RTC was successfully recovered and can safely return to
     * the Inactive state, this method shall complete with
     * ReturnCode_t::OK. Any other result shall indicate that the RTC
     * should remain in the Error state.
     *
     * @endif
     */
       case 18:  // RTC/ComponentAction/on_reset
       {
         int exec_handle = RTC.ExecutionContextHandle_tHelper.read (in);
         RTC.ReturnCode_t $result = null;
         $result = this.on_reset (exec_handle);
         out = $rh.createReply();
         RTC.ReturnCode_tHelper.write (out, $result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:omg.org/RTC/LightweightRTObject:1.0", 
    "IDL:omg.org/RTC/ComponentAction:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public LightweightRTObject _this() 
  {
    return LightweightRTObjectHelper.narrow(
    super._this_object());
  }

  public LightweightRTObject _this(org.omg.CORBA.ORB orb) 
  {
    return LightweightRTObjectHelper.narrow(
    super._this_object(orb));
  }


} // class LightweightRTObjectPOA
