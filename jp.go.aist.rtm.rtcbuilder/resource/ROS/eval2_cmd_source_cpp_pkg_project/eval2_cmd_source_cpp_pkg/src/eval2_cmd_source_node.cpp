#include "eval2_cmd_source_cpp_pkg/eval2_cmd_source_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval2_cmd_source_cpp_pkg
{

Eval2CmdSourceNode::Eval2CmdSourceNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval2_cmd_source_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
  declare_parameters();
}

// ============================================================
// Parameter handling
// ============================================================

void Eval2CmdSourceNode::declare_parameters()
{
  rcl_interfaces::msg::ParameterDescriptor desc_command_speed;
  desc_command_speed.description = "Command speed.";
  this->declare_parameter("command_speed", 0.5, desc_command_speed);
  param_cb_handle_ = this->add_on_set_parameters_callback(std::bind(&Eval2CmdSourceNode::on_set_parameters, this, std::placeholders::_1));
}

rcl_interfaces::msg::SetParametersResult Eval2CmdSourceNode::on_set_parameters(const std::vector<rclcpp::Parameter> & params)
{
  rcl_interfaces::msg::SetParametersResult result;
  result.successful = true;

  for (const auto & param : params) {
    if (param.get_name() == "command_speed") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
  }

  return result;
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval2CmdSourceNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: cmd_vel
  // Type: geometry_msgs/msg/Twist
  // Description: Publish velocity command.
  cmd_vel_publisher_ = this->create_publisher<geometry_msgs::msg::Twist>("cmd_vel", rclcpp::QoS(10).reliable());
  robot_status_sub_ = this->create_subscription<std_msgs::msg::String>("robot_status", rclcpp::QoS(10).reliable(), std::bind(&Eval2CmdSourceNode::robot_status_callback, this, std::placeholders::_1));
  // Timer: cmd_timer
  // Period: 1.0 sec
  // Callback: cmd_timer_callback()
  // Description: Publish command periodically.
  cmd_timer_ = this->create_wall_timer(std::chrono::milliseconds(1000), std::bind(&Eval2CmdSourceNode::cmd_timer_callback, this));
  cmd_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval2CmdSourceNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_activate(); }
  if (cmd_timer_) { cmd_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval2CmdSourceNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_deactivate(); }
  if (cmd_timer_) { cmd_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval2CmdSourceNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   cmd_vel_publisher_.reset();
//   robot_status_sub_.reset();
//   cmd_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval2CmdSourceNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval2CmdSourceNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Topic callbacks
// ============================================================

void Eval2CmdSourceNode::robot_status_callback(const std_msgs::msg::String::SharedPtr msg)
{
  // Subscribe Topic: robot_status
  // Type: std_msgs/msg/String
  // Description: Receive robot status.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval2CmdSourceNode::cmd_timer_callback()
{
  // Timer: cmd_timer
  // Period: 1.0 sec
  // Callback: cmd_timer_callback()
  // Description: Publish command periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval2_cmd_source_cpp_pkg
