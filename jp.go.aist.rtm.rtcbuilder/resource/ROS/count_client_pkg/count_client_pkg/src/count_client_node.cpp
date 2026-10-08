#include "count_client_pkg/count_client_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace count_client_pkg
{

CountClientNode::CountClientNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("count_client_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn CountClientNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  count_client_ = this->create_client<count_server_pkg_interfaces::srv::Count>("count");
  // Timer: count_call_timer
  // Period: 2.0 sec
  // Callback: count_call_timer_callback()
  // Description: Call the count service periodically.
  count_call_timer_ = this->create_wall_timer(std::chrono::milliseconds(2000), std::bind(&CountClientNode::count_call_timer_callback, this));
  count_call_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn CountClientNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (count_call_timer_) { count_call_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn CountClientNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (count_call_timer_) { count_call_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn CountClientNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   count_client_.reset();
//   count_call_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn CountClientNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn CountClientNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }
// ============================================================
// Service callbacks and clients
// ============================================================

void CountClientNode::call_count_service()
{
  // Service Client: count
  // Type: count_server_pkg_interfaces/srv/Count
  // Description: Send an amount to the count service.
  // Service Client helper: create a request and send it asynchronously to the server.
  if (!count_client_) {
    RCLCPP_WARN(this->get_logger(), "Service client is not configured.");
    return;
  }
  auto request = std::make_shared<count_server_pkg_interfaces::srv::Count::Request>();
  // TODO: Call this helper at the appropriate time in your node logic.
  // TODO: Fill the request fields according to the selected service type.
  // Note: After filling the request, async_send_request() sends it to the service server.
  auto future = count_client_->async_send_request(request);
  (void)future;
}


// ============================================================
// Timer callbacks
// ============================================================

void CountClientNode::count_call_timer_callback()
{
  // Timer: count_call_timer
  // Period: 2.0 sec
  // Callback: count_call_timer_callback()
  // Description: Call the count service periodically.
  // TODO: Implement the periodic behavior described above.
}


}  // namespace count_client_pkg
