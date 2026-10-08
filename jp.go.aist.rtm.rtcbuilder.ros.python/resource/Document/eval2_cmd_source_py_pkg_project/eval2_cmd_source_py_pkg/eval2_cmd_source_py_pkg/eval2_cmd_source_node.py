import rclpy

from geometry_msgs.msg import Twist
from rcl_interfaces.msg import ParameterDescriptor, SetParametersResult
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from rclpy.qos import QoSHistoryPolicy, QoSProfile, QoSReliabilityPolicy
from std_msgs.msg import String


class Eval2CmdSourceNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.cmd_vel_publisher = None
        self.robot_status_sub = None
        self._cmd_timer = None
        self._declare_parameters()

    # ============================================================
    # Parameter handling
    # ============================================================

    def _declare_parameters(self):
        desc_command_speed = ParameterDescriptor(description='Command speed.')
        self.declare_parameter('command_speed', 0.5, desc_command_speed)
        self.add_on_set_parameters_callback(self._on_set_parameters)

    def _on_set_parameters(self, params):
        for param in params:
            if param.name == 'command_speed':
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
        """
        Publish velocity command.
         - Type: データの型
         - Semantics: データの意味
	     - Unit: データの単位
         - Operation Cycle: データの発生頻度
        """
        # Publish Topic: cmd_vel
        # Type: geometry_msgs/msg/Twist
        # Description: Publish velocity command.
        self.cmd_vel_publisher = self.create_lifecycle_publisher(Twist, 'cmd_vel', QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        """
        Receive robot status.
	     - Type: データの型2
         - Semantics: データの意味3
	     - Unit: データの単位4
         - Operation Cycle: データの発生頻度5
        """
        self.robot_status_sub = self.create_subscription(String, 'robot_status', self.robot_status_callback, QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        # Timer: cmd_timer
        # Period: 1.0 sec
        # Callback: cmd_timer_callback()
        # Description: Publish command periodically.
        self._cmd_timer = self.create_timer(1.0, self.cmd_timer_callback)
        self._cmd_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._cmd_timer is not None:
            self._cmd_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._cmd_timer is not None:
            self._cmd_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._cmd_timer is not None:
    #         self.destroy_timer(self._cmd_timer)
    #         self._cmd_timer = None
    #     if self.cmd_vel_publisher is not None:
    #         self.destroy_publisher(self.cmd_vel_publisher)
    #         self.cmd_vel_publisher = None
    #     if self.robot_status_sub is not None:
    #         self.destroy_subscription(self.robot_status_sub)
    #         self.robot_status_sub = None
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
        # Type: std_msgs/msg/String
        # Description: Receive robot status.
        # TODO: Use the received message to implement this node's behavior.
        # Note: This callback is called each time a message arrives on the subscribed topic.
        pass

    # ============================================================
    # Timer callbacks
    # ============================================================

    def cmd_timer_callback(self):
        # Timer: cmd_timer
        # Period: 1.0 sec
        # Callback: cmd_timer_callback()
        # Description: Publish command periodically.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval2CmdSourceNode('eval2_cmd_source_node')
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
