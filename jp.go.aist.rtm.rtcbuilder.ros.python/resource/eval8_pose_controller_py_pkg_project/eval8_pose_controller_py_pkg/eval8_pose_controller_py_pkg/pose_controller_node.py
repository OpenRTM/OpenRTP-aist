import rclpy

from geometry_msgs.msg import Twist
from rcl_interfaces.msg import ParameterDescriptor, SetParametersResult
from eval8_pose_controller_py_pkg_interfaces.action import MoveToPose
from rclpy.action import ActionServer, CancelResponse, GoalResponse
from rclpy.callback_groups import ReentrantCallbackGroup
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from rclpy.qos import QoSHistoryPolicy, QoSProfile, QoSReliabilityPolicy
from nav_msgs.msg import Odometry


class PoseControllerNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.cmd_vel_publisher = None
        self.odom_sub = None
        self.move_to_pose_server = None
        self._control_timer = None
        self._declare_parameters()

    # ============================================================
    # Parameter handling
    # ============================================================

    def _declare_parameters(self):
        desc_linear_gain = ParameterDescriptor(description='Proportional gain for linear velocity control.')
        self.declare_parameter('linear_gain', 1.0, desc_linear_gain)
        desc_angular_gain = ParameterDescriptor(description='Proportional gain for angular velocity control.')
        self.declare_parameter('angular_gain', 2.0, desc_angular_gain)
        desc_max_linear_speed = ParameterDescriptor(description='Maximum commanded linear velocity.')
        self.declare_parameter('max_linear_speed', 0.5, desc_max_linear_speed)
        desc_max_angular_speed = ParameterDescriptor(description='Maximum commanded angular velocity.')
        self.declare_parameter('max_angular_speed', 1.0, desc_max_angular_speed)
        desc_goal_tolerance = ParameterDescriptor(description='Position tolerance used to determine goal completion.')
        self.declare_parameter('goal_tolerance', 0.05, desc_goal_tolerance)
        desc_yaw_tolerance = ParameterDescriptor(description='Yaw tolerance used to determine final orientation completion.')
        self.declare_parameter('yaw_tolerance', 0.05, desc_yaw_tolerance)
        desc_odom_timeout_sec = ParameterDescriptor(description='Maximum allowed age of odometry data before stopping the robot.')
        self.declare_parameter('odom_timeout_sec', 0.5, desc_odom_timeout_sec)
        desc_control_enabled = ParameterDescriptor(description='Enable or disable velocity control.')
        self.declare_parameter('control_enabled', True, desc_control_enabled)
        self.add_on_set_parameters_callback(self._on_set_parameters)

    def _on_set_parameters(self, params):
        for param in params:
            if param.name == 'linear_gain':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'angular_gain':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'max_linear_speed':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'max_angular_speed':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'goal_tolerance':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'yaw_tolerance':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'odom_timeout_sec':
                # TODO: Update internal state when this parameter changes.
                # Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
                value = param.value
                # TODO: Store the parameter value in member variables or runtime settings as needed.
                _ = value
            if param.name == 'control_enabled':
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
        # Description: Publish linear and angular velocity commands for the simulated mobile base.
        self.cmd_vel_publisher = self.create_lifecycle_publisher(Twist, 'cmd_vel', QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.odom_sub = self.create_subscription(Odometry, 'odom', self.odom_callback, QoSProfile(depth=10, reliability=QoSReliabilityPolicy.BEST_EFFORT, history=QoSHistoryPolicy.KEEP_LAST))
        self._action_cb_group_move_to_pose = ReentrantCallbackGroup()
        self.move_to_pose_server = ActionServer(self, MoveToPose, 'move_to_pose', execute_callback=self.execute_move_to_pose, goal_callback=self.handle_goal_move_to_pose, cancel_callback=self.handle_cancel_move_to_pose, callback_group=self._action_cb_group_move_to_pose)
        # Timer: control_timer
        # Period: 0.05 sec
        # Callback: control_timer_callback()
        # Description: Calculate and publish velocity commands while a move-to-pose goal is active.
        self._control_timer = self.create_timer(0.05, self.control_timer_callback)
        self._control_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._control_timer is not None:
            self._control_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._control_timer is not None:
            self._control_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    def on_cleanup(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_cleanup] called.')
        # TODO: Release resources when the node is cleaned up.
        if self._control_timer is not None:
            self.destroy_timer(self._control_timer)
            self._control_timer = None
        if self.cmd_vel_publisher is not None:
            self.destroy_publisher(self.cmd_vel_publisher)
            self.cmd_vel_publisher = None
        if self.odom_sub is not None:
            self.destroy_subscription(self.odom_sub)
            self.odom_sub = None
        if self.move_to_pose_server is not None:
            self.move_to_pose_server.destroy()
            self.move_to_pose_server = None
        return TransitionCallbackReturn.SUCCESS

    def on_shutdown(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_shutdown] called.')
        # TODO: Implement shutdown handling.
        return TransitionCallbackReturn.SUCCESS

    def on_error(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_error] called.')
        # TODO: Implement error recovery or safe-stop handling.
        return TransitionCallbackReturn.SUCCESS

    # ============================================================
    # Topic callbacks
    # ============================================================

    def odom_callback(self, msg):
        # Subscribe Topic: odom
        # Type: nav_msgs/msg/Odometry
        # Description: Receive the current simulated robot pose and velocity.
        # TODO: Use the received message to implement this node's behavior.
        # Note: This callback is called each time a message arrives on the subscribed topic.
        pass

    # ============================================================
    # Action callbacks and clients
    # ============================================================

    def handle_goal_move_to_pose(self, goal_request):
        # Action Server: move_to_pose
        # Type: eval8_pose_controller_py_pkg_interfaces/action/MoveToPose
        # Description: Drive the simulated mobile robot to a requested target pose.
        # TODO: Decide whether this goal should be accepted.
        return GoalResponse.ACCEPT

    def handle_cancel_move_to_pose(self, goal_handle):
        # Action Server: move_to_pose
        # Type: eval8_pose_controller_py_pkg_interfaces/action/MoveToPose
        # Description: Drive the simulated mobile robot to a requested target pose.
        # TODO: Decide whether this cancel request should be accepted.
        return CancelResponse.ACCEPT

    def execute_move_to_pose(self, goal_handle):
        # Action Server: move_to_pose
        # Type: eval8_pose_controller_py_pkg_interfaces/action/MoveToPose
        # Description: Drive the simulated mobile robot to a requested target pose.
        goal = goal_handle.request
        result = MoveToPose.Result()

        # TODO: Read the goal and implement the action execution logic.
        # TODO: Set feedback and result fields as needed.
        _ = goal

        goal_handle.succeed()
        return result

    # ============================================================
    # Timer callbacks
    # ============================================================

    def control_timer_callback(self):
        # Timer: control_timer
        # Period: 0.05 sec
        # Callback: control_timer_callback()
        # Description: Calculate and publish velocity commands while a move-to-pose goal is active.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = PoseControllerNode('pose_controller_node')
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
