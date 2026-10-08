#include "eval3_service_server_cpp_pkg/eval3_service_server_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval3_service_server_cpp_pkg
{

Eval3ServiceServerNode::Eval3ServiceServerNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval3_service_server_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval3ServiceServerNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  reset_srv_ = this->create_service<std_srvs::srv::Trigger>("reset", std::bind(&Eval3ServiceServerNode::handle_reset, this, std::placeholders::_1, std::placeholders::_2));
  set_bool_srv_ = this->create_service<std_srvs::srv::SetBool>("set_bool", std::bind(&Eval3ServiceServerNode::handle_set_bool, this, std::placeholders::_1, std::placeholders::_2));
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval3ServiceServerNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval3ServiceServerNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceServerNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   reset_srv_.reset();
//   set_bool_srv_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceServerNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceServerNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Service callbacks and clients
// ============================================================

void Eval3ServiceServerNode::handle_reset(const std::shared_ptr<std_srvs::srv::Trigger::Request> request, std::shared_ptr<std_srvs::srv::Trigger::Response> response)
{
  // Service Server: reset
  // Type: std_srvs/srv/Trigger
  // Description: Reset internal state.
  // TODO: Read the request and implement the service behavior.
  // TODO: Fill the response fields before returning.
  (void)request;
  (void)response;
}

void Eval3ServiceServerNode::handle_set_bool(const std::shared_ptr<std_srvs::srv::SetBool::Request> request, std::shared_ptr<std_srvs::srv::SetBool::Response> response)
{
  // Service Server: set_bool
  // Type: std_srvs/srv/SetBool
  // Description: Set boolean state.
  // TODO: Read the request and implement the service behavior.
  // TODO: Fill the response fields before returning.
  (void)request;
  (void)response;
}

}  // namespace eval3_service_server_cpp_pkg
