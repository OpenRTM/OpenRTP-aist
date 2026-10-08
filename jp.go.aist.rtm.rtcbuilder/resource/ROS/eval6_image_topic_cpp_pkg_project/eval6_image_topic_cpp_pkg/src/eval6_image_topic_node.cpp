#include "eval6_image_topic_cpp_pkg/eval6_image_topic_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval6_image_topic_cpp_pkg
{

Eval6ImageTopicNode::Eval6ImageTopicNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval6_image_topic_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval6ImageTopicNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: image_debug
  // Type: sensor_msgs/msg/Image
  // Description: Publish debug image output.
  image_debug_publisher_ = this->create_publisher<sensor_msgs::msg::Image>("image_debug", rclcpp::QoS(10).reliable());
  image_raw_sub_ = this->create_subscription<sensor_msgs::msg::Image>("image_raw", rclcpp::QoS(10).reliable(), std::bind(&Eval6ImageTopicNode::image_raw_callback, this, std::placeholders::_1));
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval6ImageTopicNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (image_debug_publisher_) { image_debug_publisher_->on_activate(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval6ImageTopicNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (image_debug_publisher_) { image_debug_publisher_->on_deactivate(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval6ImageTopicNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   image_debug_publisher_.reset();
//   image_raw_sub_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval6ImageTopicNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval6ImageTopicNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Topic callbacks
// ============================================================

void Eval6ImageTopicNode::image_raw_callback(const sensor_msgs::msg::Image::SharedPtr msg)
{
  // Subscribe Topic: image_raw
  // Type: sensor_msgs/msg/Image
  // Description: Receive raw image input.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

}  // namespace eval6_image_topic_cpp_pkg
