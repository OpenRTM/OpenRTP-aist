import rclpy

from geometry_msgs.msg import Twist
from rcl_interfaces.msg import ParameterDescriptor, SetParametersResult
from eval5_robot_py_pkg_interfaces.action import MoveToTarget
from rclpy.action import ActionClient
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from rclpy.qos import QoSHistoryPolicy, QoSProfile, QoSReliabilityPolicy
from eval5_robot_py_pkg_interfaces.msg import CustomStatus
from eval5_robot_py_pkg_interfaces.srv import SetMode


class Eval5OperatorNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.cmd_vel_publisher = None
        self.robot_status_sub = None
        self.set_mode_client = None
        self.move_to_target_client = None
        self._command_timer = None
        self._declare_parameters()

    # ============================================================
    # Parameter handling
    # ============================================================

    def _declare_parameters(self):
        """
        Operator name.
         - Name: Doc Data name.
         - DefaultValue: Doc Default value.
         - Unit: Doc Unit.
         - Range: Doc Range.
         - Constraint: Doc Constraint.
        """
        desc_operator_name = ParameterDescriptor(description='Operator name.', read_only=True)
        self.declare_parameter('operator_name', 'operator', desc_operator_name)
        self.add_on_set_parameters_callback(self._on_set_parameters)

    def _on_set_parameters(self, params):
        for param in params:
            if param.name == 'operator_name':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
        return SetParametersResult(successful=True, reason='')

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        # Publish Topic: cmd_vel
        # Type: geometry_msgs/msg/Twist
        # Description: Publish velocity command.
        self.cmd_vel_publisher = self.create_lifecycle_publisher(Twist, 'cmd_vel', QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.robot_status_sub = self.create_subscription(CustomStatus, 'robot_status', self.robot_status_callback, QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.set_mode_client = self.create_client(SetMode, 'set_mode')
        self.move_to_target_client = ActionClient(self, MoveToTarget, 'move_to_target')
        # Timer: command_timer
        # Period: 1.0 sec
        # Callback: command_timer_callback()
        # Description: Publish commands periodically.
        self._command_timer = self.create_timer(1.0, self.command_timer_callback)
        self._command_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._command_timer is not None:
            self._command_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._command_timer is not None:
            self._command_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._command_timer is not None:
    #         self.destroy_timer(self._command_timer)
    #         self._command_timer = None
    #     if self.cmd_vel_publisher is not None:
    #         self.destroy_publisher(self.cmd_vel_publisher)
    #         self.cmd_vel_publisher = None
    #     if self.robot_status_sub is not None:
    #         self.destroy_subscription(self.robot_status_sub)
    #         self.robot_status_sub = None
    #     if self.set_mode_client is not None:
    #         self.destroy_client(self.set_mode_client)
    #         self.set_mode_client = None
    #     if self.move_to_target_client is not None:
    #         self.move_to_target_client.destroy()
    #         self.move_to_target_client = None
    #     return TransitionCallbackReturn.SUCCESS

    # Optional: on_shutdown. Uncomment this block to use it.
    # def on_shutdown(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_shutdown] called.')
    #     # TODO: Implement shutdown handling.
    #     return TransitionCallbackReturn.SUCCESS

    # Optional: on_error. Uncomment this block to use it.
    # def on_error(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_error] called.')
    #     # TODO: Implement error recovery or safe-stop handling.
    #     return TransitionCallbackReturn.SUCCESS

    # ============================================================
    # Topic callbacks
    # ============================================================

    def robot_status_callback(self, msg):
        # Subscribe Topic: robot_status
        # Type: eval5_robot_py_pkg_interfaces/msg/CustomStatus
        # Description: Receive custom robot status.
        # TODO: Use the received message to implement this node's behavior.
        # Note: This callback is called each time a message arrives on the subscribed topic.
        pass

    # ============================================================
    # Service callbacks and clients
    # ============================================================

    def call_set_mode(self):
        # Service Client: set_mode
        # Type: eval5_robot_py_pkg_interfaces/srv/SetMode
        # Description: Request robot operation mode.
        # Service Client helper: create a request and send it asynchronously to the server.
        request = SetMode.Request()

        # TODO: Call this helper at the appropriate time in your node logic.
        # TODO: Fill the request fields according to the selected service type.
        # Note: After filling the request, call_async() sends it to the service server.

        return self.set_mode_client.call_async(request)

    # ============================================================
    # Action callbacks and clients
    # ============================================================

    def send_move_to_target_goal(self):
        # Action Client: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Send move target goal.
        # Action Client helper: create a goal and send it to the action server.
        # TODO: Call this goal-sending helper at the appropriate time in your node logic.
        # Note: Goal fields differ by action type, so this generator does not set concrete values.
        # TODO: Create a goal, configure response / feedback / result callbacks, and send it.
        self.get_logger().warning('Goal send skeleton is disabled. Implement goal send code before use.')
        return None

    def move_to_target_action_response(self, future):
        # Action Client: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Send move target goal.
        goal_handle = future.result()
        if not goal_handle.accepted:
            self.get_logger().error('Goal rejected')
            return
        result_future = goal_handle.get_result_async()
        result_future.add_done_callback(self.move_to_target_action_result)

    def move_to_target_action_feedback(self, feedback_msg):
        # Action Client: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Send move target goal.
        # TODO: Use feedback data as needed.
        pass

    def move_to_target_action_result(self, future):
        # Action Client: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Send move target goal.
        # TODO: Use result data as needed.
        pass

    # ============================================================
    # Timer callbacks
    # ============================================================

    def command_timer_callback(self):
        # Timer: command_timer
        # Period: 1.0 sec
        # Callback: command_timer_callback()
        # Description: Publish commands periodically.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval5OperatorNode('eval5_operator_node')
    executor = MultiThreadedExecutor()
    executor.add_node(node_instance)
    try:
        executor.spin()
    except KeyboardInterrupt:
        pass
    finally:
        node_instance.destroy_node()
        if rclpy.ok():
            rclpy.shutdown()


if __name__ == '__main__':
    main()
