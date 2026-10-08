#include "count_server_pkg/count_server_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace count_server_pkg
{

CountServerNode::CountServerNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("count_server_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn CountServerNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  count_srv_ = this->create_service<count_server_pkg_interfaces::srv::Count>("count", std::bind(&CountServerNode::handle_count, this, std::placeholders::_1, std::placeholders::_2));
  return CallbackReturn::SUCCESS;
}

CallbackReturn CountServerNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  return CallbackReturn::SUCCESS;
}

CallbackReturn CountServerNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn CountServerNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   count_srv_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn CountServerNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn CountServerNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }
// ============================================================
// Service callbacks and clients
// ============================================================


void CountServerNode::handle_count(const std::shared_ptr<count_server_pkg_interfaces::srv::Count::Request> request, std::shared_ptr<count_server_pkg_interfaces::srv::Count::Response> response)
{
  // Service Server: count
  // Type: count_server_pkg_interfaces/srv/Count
  // Description: Add amount to the running total.
  // TODO: Read the request and implement the service behavior.
  // TODO: Fill the response fields before returning.
  (void)request;
  (void)response;
}


}  // namespace count_server_pkg
