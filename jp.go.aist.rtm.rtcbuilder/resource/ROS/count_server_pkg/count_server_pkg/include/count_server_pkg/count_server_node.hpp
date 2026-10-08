#ifndef COUNT_SERVER_PKG__COUNT_SERVER_NODE_HPP_
#define COUNT_SERVER_PKG__COUNT_SERVER_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "count_server_pkg_interfaces/srv/count.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace count_server_pkg
{

class CountServerNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit CountServerNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~CountServerNode() override = default;

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
   * Add amount to the running total.
   * - Argument: amount: value to add
   * - Return: total and success
   */
  rclcpp::Service<count_server_pkg_interfaces::srv::Count>::SharedPtr count_srv_;
  void handle_count(const std::shared_ptr<count_server_pkg_interfaces::srv::Count::Request> request, std::shared_ptr<count_server_pkg_interfaces::srv::Count::Response> response);

};

}  // namespace count_server_pkg

#endif  // COUNT_SERVER_PKG__COUNT_SERVER_NODE_HPP_
