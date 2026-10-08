#include "eval2_controller_cpp_pkg/eval2_controller_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval2_controller_cpp_pkg
{

Eval2ControllerNode::Eval2ControllerNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval2_controller_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
  declare_parameters();
}

// ============================================================
// Parameter handling
// ============================================================

void Eval2ControllerNode::declare_parameters()
{
  rcl_interfaces::msg::ParameterDescriptor desc_max_speed;
  desc_max_speed.description = "Maximum speed.";
  this->declare_parameter("max_speed", 1.0, desc_max_speed);
  rcl_interfaces::msg::ParameterDescriptor desc_status_prefix;
  desc_status_prefix.description = "Status prefix.";
  desc_status_prefix.read_only = true;
  this->declare_parameter("status_prefix", std::string("status"), desc_status_prefix);
  rcl_interfaces::msg::ParameterDescriptor desc_enable_safety_limit;
  desc_enable_safety_limit.description = "Enable safety limit.";
  this->declare_parameter("enable_safety_limit", true, desc_enable_safety_limit);
  param_cb_handle_ = this->add_on_set_parameters_callback(std::bind(&Eval2ControllerNode::on_set_parameters, this, std::placeholders::_1));
}

rcl_interfaces::msg::SetParametersResult Eval2ControllerNode::on_set_parameters(const std::vector<rclcpp::Parameter> & params)
{
  rcl_interfaces::msg::SetParametersResult result;
  result.successful = true;

  for (const auto & param : params) {
    if (param.get_name() == "max_speed") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "status_prefix") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const std::string value = param.as_string();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "enable_safety_limit") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const bool value = param.as_bool();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
  }

  return result;
}

// ============================================================
// Lifecycle callbacks
// ============================================================

/*!
 * Desc1
 */
CallbackReturn Eval2ControllerNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: robot_status
  // Type: std_msgs/msg/String
  // Description: Publish robot status text.
  robot_status_publisher_ = this->create_publisher<std_msgs::msg::String>("robot_status", rclcpp::QoS(10).reliable());
  cmd_vel_sub_ = this->create_subscription<geometry_msgs::msg::Twist>("cmd_vel", rclcpp::QoS(10).reliable(), std::bind(&Eval2ControllerNode::cmd_vel_callback, this, std::placeholders::_1));
  // Timer: status_timer
  // Period: 1.0 sec
  // Callback: status_timer_callback()
  // Description: Publish status periodically.
  status_timer_ = this->create_wall_timer(std::chrono::milliseconds(1000), std::bind(&Eval2ControllerNode::status_timer_callback, this));
  status_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

/*!
 * Desc2
 */
CallbackReturn Eval2ControllerNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (robot_status_publisher_) { robot_status_publisher_->on_activate(); }
  if (status_timer_) { status_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

/*!
 * Desc3
 */
CallbackReturn Eval2ControllerNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (robot_status_publisher_) { robot_status_publisher_->on_deactivate(); }
  if (status_timer_) { status_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

/*!
 * Desc4
 */
CallbackReturn Eval2ControllerNode::on_cleanup(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
  // TODO: Release resources when the node is cleaned up.
  robot_status_publisher_.reset();
  cmd_vel_sub_.reset();
  status_timer_.reset();
  return CallbackReturn::SUCCESS;
}

/*!
 * Desc5
 */
CallbackReturn Eval2ControllerNode::on_shutdown(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
  // TODO: Implement shutdown handling.
  return CallbackReturn::SUCCESS;
}

/*!
 * Desc6
 */
CallbackReturn Eval2ControllerNode::on_error(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_error] called.");
  // TODO: Implement error recovery or safe-stop handling.
  return CallbackReturn::SUCCESS;
}

// ============================================================
// Topic callbacks
// ============================================================

void Eval2ControllerNode::cmd_vel_callback(const geometry_msgs::msg::Twist::SharedPtr msg)
{
  // Subscribe Topic: cmd_vel
  // Type: geometry_msgs/msg/Twist
  // Description: Receive velocity command.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval2ControllerNode::status_timer_callback()
{
  // Timer: status_timer
  // Period: 1.0 sec
  // Callback: status_timer_callback()
  // Description: Publish status periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval2_controller_cpp_pkg
