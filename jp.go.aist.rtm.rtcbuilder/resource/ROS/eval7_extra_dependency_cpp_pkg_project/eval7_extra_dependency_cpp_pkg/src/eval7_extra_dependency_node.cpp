#include "eval7_extra_dependency_cpp_pkg/eval7_extra_dependency_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval7_extra_dependency_cpp_pkg
{

Eval7ExtraDependencyNode::Eval7ExtraDependencyNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval7_extra_dependency_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval7ExtraDependencyNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: output_text
  // Type: std_msgs/msg/String
  // Description: Publish text output.
  output_text_publisher_ = this->create_publisher<std_msgs::msg::String>("output_text", rclcpp::QoS(10).reliable());
  input_text_sub_ = this->create_subscription<std_msgs::msg::String>("input_text", rclcpp::QoS(10).reliable(), std::bind(&Eval7ExtraDependencyNode::input_text_callback, this, std::placeholders::_1));
  // Timer: text_timer
  // Period: 1.0 sec
  // Callback: text_timer_callback()
  // Description: Periodic placeholder for user logic.
  text_timer_ = this->create_wall_timer(std::chrono::milliseconds(1000), std::bind(&Eval7ExtraDependencyNode::text_timer_callback, this));
  text_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval7ExtraDependencyNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (output_text_publisher_) { output_text_publisher_->on_activate(); }
  if (text_timer_) { text_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval7ExtraDependencyNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (output_text_publisher_) { output_text_publisher_->on_deactivate(); }
  if (text_timer_) { text_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval7ExtraDependencyNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   output_text_publisher_.reset();
//   input_text_sub_.reset();
//   text_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval7ExtraDependencyNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval7ExtraDependencyNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Topic callbacks
// ============================================================

void Eval7ExtraDependencyNode::input_text_callback(const std_msgs::msg::String::SharedPtr msg)
{
  // Subscribe Topic: input_text
  // Type: std_msgs/msg/String
  // Description: Receive text input.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval7ExtraDependencyNode::text_timer_callback()
{
  // Timer: text_timer
  // Period: 1.0 sec
  // Callback: text_timer_callback()
  // Description: Periodic placeholder for user logic.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval7_extra_dependency_cpp_pkg
