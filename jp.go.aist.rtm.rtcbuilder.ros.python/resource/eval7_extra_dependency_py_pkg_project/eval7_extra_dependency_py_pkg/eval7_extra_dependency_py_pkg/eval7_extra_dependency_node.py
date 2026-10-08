import rclpy

from std_msgs.msg import String
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from rclpy.qos import QoSHistoryPolicy, QoSProfile, QoSReliabilityPolicy


class Eval7ExtraDependencyNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.output_text_publisher = None
        self.input_text_sub = None
        self._text_timer = None

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        # Publish Topic: output_text
        # Type: std_msgs/msg/String
        # Description: Publish text output.
        self.output_text_publisher = self.create_lifecycle_publisher(String, 'output_text', QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        self.input_text_sub = self.create_subscription(String, 'input_text', self.input_text_callback, QoSProfile(depth=10, reliability=QoSReliabilityPolicy.RELIABLE, history=QoSHistoryPolicy.KEEP_LAST))
        # Timer: text_timer
        # Period: 1.0 sec
        # Callback: text_timer_callback()
        # Description: Periodic placeholder for user logic.
        self._text_timer = self.create_timer(1.0, self.text_timer_callback)
        self._text_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._text_timer is not None:
            self._text_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._text_timer is not None:
            self._text_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._text_timer is not None:
    #         self.destroy_timer(self._text_timer)
    #         self._text_timer = None
    #     if self.output_text_publisher is not None:
    #         self.destroy_publisher(self.output_text_publisher)
    #         self.output_text_publisher = None
    #     if self.input_text_sub is not None:
    #         self.destroy_subscription(self.input_text_sub)
    #         self.input_text_sub = None
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

    def input_text_callback(self, msg):
        # Subscribe Topic: input_text
        # Type: std_msgs/msg/String
        # Description: Receive text input.
        # TODO: Use the received message to implement this node's behavior.
        # Note: This callback is called each time a message arrives on the subscribed topic.
        pass

    # ============================================================
    # Timer callbacks
    # ============================================================

    def text_timer_callback(self):
        # Timer: text_timer
        # Period: 1.0 sec
        # Callback: text_timer_callback()
        # Description: Periodic placeholder for user logic.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval7ExtraDependencyNode('eval7_extra_dependency_node')
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
