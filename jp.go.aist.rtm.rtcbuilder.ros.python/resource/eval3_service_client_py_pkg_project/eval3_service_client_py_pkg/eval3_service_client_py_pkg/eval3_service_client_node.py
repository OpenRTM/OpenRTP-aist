import rclpy

from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from std_srvs.srv import Trigger, SetBool


class Eval3ServiceClientNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.reset_client = None
        self.set_bool_client = None
        self._service_call_timer = None

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        self.reset_client = self.create_client(Trigger, 'reset')
        self.set_bool_client = self.create_client(SetBool, 'set_bool')
        # Timer: service_call_timer
        # Period: 2.0 sec
        # Callback: service_call_timer_callback()
        # Description: Call services periodically.
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
    #     if self.reset_client is not None:
    #         self.destroy_client(self.reset_client)
    #         self.reset_client = None
    #     if self.set_bool_client is not None:
    #         self.destroy_client(self.set_bool_client)
    #         self.set_bool_client = None
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

    def call_reset(self):
        # Service Client: reset
        # Type: std_srvs/srv/Trigger
        # Description: Call reset service.
        # Service Client helper: create a request and send it asynchronously to the server.
        request = Trigger.Request()

        # TODO: Call this helper at the appropriate time in your node logic.
        # TODO: Fill the request fields according to the selected service type.
        # Note: After filling the request, call_async() sends it to the service server.

        return self.reset_client.call_async(request)

    def call_set_bool(self):
        # Service Client: set_bool
        # Type: std_srvs/srv/SetBool
        # Description: Call SetBool service.
        # Service Client helper: create a request and send it asynchronously to the server.
        request = SetBool.Request()

        # TODO: Call this helper at the appropriate time in your node logic.
        # TODO: Fill the request fields according to the selected service type.
        # Note: After filling the request, call_async() sends it to the service server.

        return self.set_bool_client.call_async(request)

    # ============================================================
    # Timer callbacks
    # ============================================================

    def service_call_timer_callback(self):
        # Timer: service_call_timer
        # Period: 2.0 sec
        # Callback: service_call_timer_callback()
        # Description: Call services periodically.
        # TODO: Implement the periodic behavior described above.
        pass


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval3ServiceClientNode('eval3_service_client_node')
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
