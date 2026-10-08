import rclpy

from example_interfaces.action import Fibonacci
from rclpy.action import ActionServer, CancelResponse, GoalResponse
from rclpy.callback_groups import ReentrantCallbackGroup
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn


class Eval4ActionServerNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.fibonacci_server = None

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        self._action_cb_group_fibonacci = ReentrantCallbackGroup()
        self.fibonacci_server = ActionServer(self, Fibonacci, 'fibonacci', execute_callback=self.execute_fibonacci, goal_callback=self.handle_goal_fibonacci, cancel_callback=self.handle_cancel_fibonacci, callback_group=self._action_cb_group_fibonacci)
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self.fibonacci_server is not None:
    #         self.fibonacci_server.destroy()
    #         self.fibonacci_server = None
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
    # Action callbacks and clients
    # ============================================================

    def handle_goal_fibonacci(self, goal_request):
        # Action Server: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Execute Fibonacci action goals.
        # TODO: Decide whether this goal should be accepted.
        return GoalResponse.ACCEPT

    def handle_cancel_fibonacci(self, goal_handle):
        # Action Server: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Execute Fibonacci action goals.
        # TODO: Decide whether this cancel request should be accepted.
        return CancelResponse.ACCEPT

    def execute_fibonacci(self, goal_handle):
        # Action Server: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Execute Fibonacci action goals.
        goal = goal_handle.request
        result = Fibonacci.Result()

        # TODO: Read the goal and implement the action execution logic.
        # TODO: Set feedback and result fields as needed.
        _ = goal

        goal_handle.succeed()
        return result


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval4ActionServerNode('eval4_action_server_node')
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
