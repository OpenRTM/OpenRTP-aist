package RTC;


/**
* RTC/LightweightRTObject.java .
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
public interface LightweightRTObject extends LightweightRTObjectOperations, RTC.ComponentAction, org.omg.CORBA.portable.IDLEntity 
{
} // interface LightweightRTObject
