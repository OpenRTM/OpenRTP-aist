import rclpy

from example_interfaces.action import Fibonacci
from rclpy.action import ActionClient
from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn


class Eval4ActionClientNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.fibonacci_client = None
        self._action_goal_timer = None

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        """
        Send Fibonacci action goals.
         - Goal: Doc Goal
         - Feedback: Doc Feedback
         - Result: Doc Return
        """
        self.fibonacci_client = ActionClient(self, Fibonacci, 'fibonacci')
        # Timer: action_goal_timer
        # Period: 3.0 sec
        # Callback: action_goal_timer_callback()
        # Description: Trigger action goal helper periodically.
        self._action_goal_timer = self.create_timer(3.0, self.action_goal_timer_callback)
        self._action_goal_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._action_goal_timer is not None:
            self._action_goal_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._action_goal_timer is not None:
            self._action_goal_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._action_goal_timer is not None:
    #         self.destroy_timer(self._action_goal_timer)
    #         self._action_goal_timer = None
    #     if self.fibonacci_client is not None:
    #         self.fibonacci_client.destroy()
    #         self.fibonacci_client = None
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

    def send_fibonacci_goal(self):
        # Action Client: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Send Fibonacci action goals.
        # Action Client helper: create a goal and send it to the action server.
        # TODO: Call this goal-sending helper at the appropriate time in your node logic.
        # Note: Goal fields differ by action type, so this generator does not set concrete values.
        # TODO: Create a goal, configure response / feedback / result callbacks, and send it.
        self.get_logger().warning('Goal send skeleton is disabled. Implement goal send code before use.')
        return None

    def fibonacci_action_response(self, future):
        # Action Client: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Send Fibonacci action goals.
        goal_handle = future.result()
        if not goal_handle.accepted:
            self.get_logger().error('Goal rejected')
            return
        result_future = goal_handle.get_result_async()
        result_future.add_done_callback(self.fibonacci_action_result)

    def fibonacci_action_feedback(self, feedback_msg):
        # Action Client: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Send Fibonacci action goals.
        # TODO: Use feedback data as needed.
        pass

    def fibonacci_action_result(self, future):
        # Action Client: fibonacci
        # Type: example_interfaces/action/Fibonacci
        # Description: Send Fibonacci action goals.
        # TODO: Use result data as needed.
        pass

    # ============================================================
    # Timer callbacks
    # ============================================================

    def action_goal_timer_callback(self):
        # Timer: action_goal_timer
        # Period: 3.0 sec
        # Callback: action_goal_timer_callback()
        # Description: Trigger action goal helper periodically.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval4ActionClientNode('eval4_action_client_node')
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
