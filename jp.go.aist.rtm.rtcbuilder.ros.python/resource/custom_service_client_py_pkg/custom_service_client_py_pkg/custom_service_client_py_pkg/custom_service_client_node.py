import rclpy

from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from custom_service_server_py_pkg_interfaces.srv import Count


class CustomServiceClientNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.client_variable = None
        self._service_call_timer = None


    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        """
        Send an amount to the Count service.
         - Argument: amount: value to add
         - Return: total and success
        """
        self.client_variable = self.create_client(Count, 'count')
        # Timer: service_call_timer
        # Period: 2.0 sec
        # Callback: service_call_timer_callback()
        # Description: Call the Count service periodically.
        self._service_call_timer = self.create_timer(2.0, self.service_call_timer_callback)
        self._service_call_timer.cancel()
        return TransitionCallbackReturn.SUCCESS

    def on_activate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_activate] called.')
        # TODO: Start processing required while the node is active.
        super().on_activate(state)
        if self._service_call_timer is not None:
            self._service_call_timer.reset()
        return TransitionCallbackReturn.SUCCESS

    def on_deactivate(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_deactivate] called.')
        # TODO: Stop processing before returning to the inactive state.
        if self._service_call_timer is not None:
            self._service_call_timer.cancel()
        super().on_deactivate(state)
        return TransitionCallbackReturn.SUCCESS

    # Optional: on_cleanup. Uncomment this block to use it.
    # def on_cleanup(self, state: State) -> TransitionCallbackReturn:
    #     self.get_logger().info('[on_cleanup] called.')
    #     # TODO: Release resources when the node is cleaned up.
    #     if self._service_call_timer is not None:
    #         self.destroy_timer(self._service_call_timer)
    #         self._service_call_timer = None
    #     if self.client_variable is not None:
    #         self.destroy_client(self.client_variable)
    #         self.client_variable = None
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

    def call_count(self):
        # Service Client: count
        # Type: custom_service_server_py_pkg_interfaces/srv/Count
        # Description: Send an amount to the Count service.
        # Service Client helper: create a request and send it asynchronously to the server.
        request = Count.Request()

        # TODO: Call this helper at the appropriate time in your node logic.
        # TODO: Fill the request fields according to the selected service type.
        # Note: After filling the request, call_async() sends it to the service server.

        return self.client_variable.call_async(request)



    # ============================================================
    # Timer callbacks
    # ============================================================

    def service_call_timer_callback(self):
        # Timer: service_call_timer
        # Period: 2.0 sec
        # Callback: service_call_timer_callback()
        # Description: Call the Count service periodically.
        # TODO: Implement the periodic behavior described above.
        pass

def main(args=None):
    rclpy.init(args=args)
    node_instance = CustomServiceClientNode('custom_service_client_node')
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
