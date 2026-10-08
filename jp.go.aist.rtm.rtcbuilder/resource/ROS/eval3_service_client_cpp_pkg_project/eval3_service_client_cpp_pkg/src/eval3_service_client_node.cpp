#include "eval3_service_client_cpp_pkg/eval3_service_client_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval3_service_client_cpp_pkg
{

Eval3ServiceClientNode::Eval3ServiceClientNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval3_service_client_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval3ServiceClientNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  reset_client_ = this->create_client<std_srvs::srv::Trigger>("reset");
  set_bool_client_ = this->create_client<std_srvs::srv::SetBool>("set_bool");
  // Timer: service_call_timer
  // Period: 2.0 sec
  // Callback: service_call_timer_callback()
  // Description: Call services periodically.
  service_call_timer_ = this->create_wall_timer(std::chrono::milliseconds(2000), std::bind(&Eval3ServiceClientNode::service_call_timer_callback, this));
  service_call_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval3ServiceClientNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (service_call_timer_) { service_call_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval3ServiceClientNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (service_call_timer_) { service_call_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceClientNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   reset_client_.reset();
//   set_bool_client_.reset();
//   service_call_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceClientNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval3ServiceClientNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Service callbacks and clients
// ============================================================

void Eval3ServiceClientNode::call_reset_service()
{
  // Service Client: reset
  // Type: std_srvs/srv/Trigger
  // Description: Call reset service.
  // Service Client helper: create a request and send it asynchronously to the server.
  if (!reset_client_) {
    RCLCPP_WARN(this->get_logger(), "Service client is not configured.");
    return;
  }
  auto request = std::make_shared<std_srvs::srv::Trigger::Request>();
  // TODO: Call this helper at the appropriate time in your node logic.
  // TODO: Fill the request fields according to the selected service type.
  // Note: After filling the request, async_send_request() sends it to the service server.
  auto future = reset_client_->async_send_request(request);
  (void)future;
}

void Eval3ServiceClientNode::call_set_bool_service()
{
  // Service Client: set_bool
  // Type: std_srvs/srv/SetBool
  // Description: Call SetBool service.
  // Service Client helper: create a request and send it asynchronously to the server.
  if (!set_bool_client_) {
    RCLCPP_WARN(this->get_logger(), "Service client is not configured.");
    return;
  }
  auto request = std::make_shared<std_srvs::srv::SetBool::Request>();
  // TODO: Call this helper at the appropriate time in your node logic.
  // TODO: Fill the request fields according to the selected service type.
  // Note: After filling the request, async_send_request() sends it to the service server.
  auto future = set_bool_client_->async_send_request(request);
  (void)future;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval3ServiceClientNode::service_call_timer_callback()
{
  // Timer: service_call_timer
  // Period: 2.0 sec
  // Callback: service_call_timer_callback()
  // Description: Call services periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval3_service_client_cpp_pkg
