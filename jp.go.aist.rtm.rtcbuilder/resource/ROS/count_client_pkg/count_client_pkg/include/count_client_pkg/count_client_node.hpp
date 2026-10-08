#ifndef COUNT_CLIENT_PKG__COUNT_CLIENT_NODE_HPP_
#define COUNT_CLIENT_PKG__COUNT_CLIENT_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "count_server_pkg_interfaces/srv/count.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace count_client_pkg
{

class CountClientNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit CountClientNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~CountClientNode() override = default;

  // ============================================================
  // Lifecycle callbacks
  // ============================================================

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_configure(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_activate(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_deactivate(const rclcpp_lifecycle::State & state) override;

  // Optional: on_cleanup. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_cleanup(const rclcpp_lifecycle::State & state) override;

  // Optional: on_shutdown. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_shutdown(const rclcpp_lifecycle::State & state) override;

  // Optional: on_error. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_error(const rclcpp_lifecycle::State & state) override;

private:
  // ============================================================
  // Service handles and callbacks
  // ============================================================

  /*!
   * Send an amount to the count service.
   * - Argument: amount: value to add
   * - Return: total and success
   */
  rclcpp::Client<count_server_pkg_interfaces::srv::Count>::SharedPtr count_client_;
  void call_count_service();

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Call the count service periodically.
   */
  rclcpp::TimerBase::SharedPtr count_call_timer_;
  void count_call_timer_callback();
};

}  // namespace count_client_pkg

#endif  // COUNT_CLIENT_PKG__COUNT_CLIENT_NODE_HPP_
