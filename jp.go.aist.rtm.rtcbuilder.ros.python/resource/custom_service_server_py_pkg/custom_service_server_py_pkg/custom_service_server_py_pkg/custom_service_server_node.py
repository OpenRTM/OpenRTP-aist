import rclpy

from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from custom_service_server_py_pkg_interfaces.srv import Count


class CustomServiceServerNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.count_srv = None


    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        """
        Add amount to the running total.
         - Argument: amount: value to add
         - Return: total and success
        """
        self.count_srv = self.create_service(Count, 'count', self.handle_count)
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
    #     if self.count_srv is not None:
    #         self.destroy_service(self.count_srv)
    #         self.count_srv = None
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
    # Service callbacks and clients
    # ============================================================


    def handle_count(self, request, response):
        # Service Server: count
        # Type: custom_service_server_py_pkg_interfaces/srv/Count
        # Description: Add amount to the running total.
        # TODO: Read the request and implement the service behavior.
        # TODO: Fill the response fields before returning.
        return response



def main(args=None):
    rclpy.init(args=args)
    node_instance = CustomServiceServerNode('custom_service_server_node')
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
