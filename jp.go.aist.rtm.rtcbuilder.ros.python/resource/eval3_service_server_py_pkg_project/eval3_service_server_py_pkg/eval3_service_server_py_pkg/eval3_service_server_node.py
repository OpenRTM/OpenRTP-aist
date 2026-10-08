import rclpy

from rclpy.executors import MultiThreadedExecutor
from rclpy.lifecycle import LifecycleNode, State, TransitionCallbackReturn
from std_srvs.srv import Trigger, SetBool


class Eval3ServiceServerNode(LifecycleNode):
    def __init__(self, node_name: str, **kwargs):
        super().__init__(node_name, **kwargs)
        self.get_logger().info("[Constructor] Node created.")
        self.reset_srv = None
        self.set_bool_srv = None

    # ============================================================
    # Lifecycle callbacks
    # ============================================================

    def on_configure(self, state: State) -> TransitionCallbackReturn:
        self.get_logger().info('[on_configure] called.')
        # TODO: Implement initialization required for this lifecycle transition.
        self.reset_srv = self.create_service(Trigger, 'reset', self.handle_reset)
        self.set_bool_srv = self.create_service(SetBool, 'set_bool', self.handle_set_bool)
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
    #     if self.reset_srv is not None:
    #         self.destroy_service(self.reset_srv)
    #         self.reset_srv = None
    #     if self.set_bool_srv is not None:
    #         self.destroy_service(self.set_bool_srv)
    #         self.set_bool_srv = None
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

    def handle_reset(self, request, response):
        # Service Server: reset
        # Type: std_srvs/srv/Trigger
        # Description: Reset internal state.
        # TODO: Read the request and implement the service behavior.
        # TODO: Fill the response fields before returning.
        return response

    def handle_set_bool(self, request, response):
        # Service Server: set_bool
        # Type: std_srvs/srv/SetBool
        # Description: Set boolean state.
        # TODO: Read the request and implement the service behavior.
        # TODO: Fill the response fields before returning.
        return response


def main(args=None):
    rclpy.init(args=args)
    node_instance = Eval3ServiceServerNode('eval3_service_server_node')
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
