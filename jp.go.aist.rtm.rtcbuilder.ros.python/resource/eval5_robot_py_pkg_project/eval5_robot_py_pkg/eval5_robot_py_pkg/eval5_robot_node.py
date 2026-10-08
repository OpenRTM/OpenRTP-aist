import rclpy

from eval5_robot_py_pkg_interfaces.msg import CustomStatus
from rcl_interfaces.msg import ParameterDescriptor, SetParametersResult
from eval5_robot_py_pkg_interfaces.action import MoveToTarget
from rclpy.action import ActionServer, CancelResponse, GoalResponse
from rclpy.callback_groups import ReentrantCallbackGroup
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from rclpy.qos import QoSHistoryPolicy, QoSProfile, QoSReliabilityPolicy
from geometry_msgs.msg import Twist
from eval5_robot_py_pkg_interfaces.srv import SetMode


class Eval5RobotNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.robot_status_publisher = None
        self.cmd_vel_sub = None
        self.set_mode_srv = None
        self.move_to_target_server = None
        self._status_timer = None
        self._declare_parameters()

    # ============================================================
    # Parameter handling
    # ============================================================

    def _declare_parameters(self):
        desc_max_speed = ParameterDescriptor(description='Maximum linear speed.')
        self.declare_parameter('max_speed', 1.0, desc_max_speed)
        desc_robot_id = ParameterDescriptor(description='Robot identifier.', read_only=True)
        self.declare_parameter('robot_id', 'robot_01', desc_robot_id)
        self.add_on_set_parameters_callback(self._on_set_parameters)

    def _on_set_parameters(self, params):
        for param in params:
            if param.name == 'max_speed':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'robot_id':
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
        # Publish Topic: robot_status
        # Type: eval5_robot_py_pkg_interfaces/msg/CustomStatus
        # Description: Publish custom robot status.
        self.robot_status_publisher = self.create_lifecycle_publisher(CustomStatus, 'robot_status', QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.cmd_vel_sub = self.create_subscription(Twist, 'cmd_vel', self.cmd_vel_callback, QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.set_mode_srv = self.create_service(SetMode, 'set_mode', self.handle_set_mode)
        self._action_cb_group_move_to_target = ReentrantCallbackGroup()
        self.move_to_target_server = ActionServer(self, MoveToTarget, 'move_to_target', execute_callback=self.execute_move_to_target, goal_callback=self.handle_goal_move_to_target, cancel_callback=self.handle_cancel_move_to_target, callback_group=self._action_cb_group_move_to_target)
        # Timer: status_timer
        # Period: 1.0 sec
        # Callback: status_timer_callback()
        # Description: Publish robot status periodically.
        self._status_timer = self.create_timer(1.0, self.status_timer_callback)
        self._status_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._status_timer is not None:
            self._status_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._status_timer is not None:
            self._status_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._status_timer is not None:
    #         self.destroy_timer(self._status_timer)
    #         self._status_timer = None
    #     if self.robot_status_publisher is not None:
    #         self.destroy_publisher(self.robot_status_publisher)
    #         self.robot_status_publisher = None
    #     if self.cmd_vel_sub is not None:
    #         self.destroy_subscription(self.cmd_vel_sub)
    #         self.cmd_vel_sub = None
    #     if self.set_mode_srv is not None:
    #         self.destroy_service(self.set_mode_srv)
    #         self.set_mode_srv = None
    #     if self.move_to_target_server is not None:
    #         self.move_to_target_server.destroy()
    #         self.move_to_target_server = None
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

    def cmd_vel_callback(self, msg):
        # Subscribe Topic: cmd_vel
        # Type: geometry_msgs/msg/Twist
        # Description: Receive velocity command.
        # TODO: Use the received message to implement this node's behavior.
        # Note: This callback is called each time a message arrives on the subscribed topic.
        pass

    # ============================================================
    # Service callbacks and clients
    # ============================================================

    def handle_set_mode(self, request, response):
        # Service Server: set_mode
        # Type: eval5_robot_py_pkg_interfaces/srv/SetMode
        # Description: Set robot operation mode.
        # TODO: Read the request and implement the service behavior.
        # TODO: Fill the response fields before returning.
        return response

    # ============================================================
    # Action callbacks and clients
    # ============================================================

    def handle_goal_move_to_target(self, goal_request):
        # Action Server: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Move robot to target pose.
        # TODO: Decide whether this goal should be accepted.
        return GoalResponse.ACCEPT

    def handle_cancel_move_to_target(self, goal_handle):
        # Action Server: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Move robot to target pose.
        # TODO: Decide whether this cancel request should be accepted.
        return CancelResponse.ACCEPT

    def execute_move_to_target(self, goal_handle):
        # Action Server: move_to_target
        # Type: eval5_robot_py_pkg_interfaces/action/MoveToTarget
        # Description: Move robot to target pose.
        goal = goal_handle.request
        result = MoveToTarget.Result()

        # TODO: Read the goal and implement the action execution logic.
        # TODO: Set feedback and result fields as needed.
        _ = goal

        goal_handle.succeed()
        return result

    # ============================================================
    # Timer callbacks
    # ============================================================

    def status_timer_callback(self):
        # Timer: status_timer
        # Period: 1.0 sec
        # Callback: status_timer_callback()
        # Description: Publish robot status periodically.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval5RobotNode('eval5_robot_node')
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
