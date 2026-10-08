#include "eval5_robot_cpp_pkg/eval5_robot_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval5_robot_cpp_pkg
{

Eval5RobotNode::Eval5RobotNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval5_robot_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
  declare_parameters();
}

// ============================================================
// Parameter handling
// ============================================================

void Eval5RobotNode::declare_parameters()
{
  rcl_interfaces::msg::ParameterDescriptor desc_max_speed;
  desc_max_speed.description = "Maximum linear speed.";
  this->declare_parameter("max_speed", 1.0, desc_max_speed);
  rcl_interfaces::msg::ParameterDescriptor desc_robot_id;
  desc_robot_id.description = "Robot identifier.";
  desc_robot_id.read_only = true;
  this->declare_parameter("robot_id", std::string("robot_01"), desc_robot_id);
  param_cb_handle_ = this->add_on_set_parameters_callback(std::bind(&Eval5RobotNode::on_set_parameters, this, std::placeholders::_1));
}

rcl_interfaces::msg::SetParametersResult Eval5RobotNode::on_set_parameters(const std::vector<rclcpp::Parameter> & params)
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
    if (param.get_name() == "robot_id") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const std::string value = param.as_string();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
  }

  return result;
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval5RobotNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: robot_status
  // Type: eval5_robot_cpp_pkg_interfaces/msg/CustomStatus
  // Description: Publish custom robot status.
  robot_status_publisher_ = this->create_publisher<eval5_robot_cpp_pkg_interfaces::msg::CustomStatus>("robot_status", rclcpp::QoS(10).reliable());
  cmd_vel_sub_ = this->create_subscription<geometry_msgs::msg::Twist>("cmd_vel", rclcpp::QoS(10).reliable(), std::bind(&Eval5RobotNode::cmd_vel_callback, this, std::placeholders::_1));
  set_mode_srv_ = this->create_service<eval5_robot_cpp_pkg_interfaces::srv::SetMode>("set_mode", std::bind(&Eval5RobotNode::handle_set_mode, this, std::placeholders::_1, std::placeholders::_2));
  move_to_target_server_ = rclcpp_action::create_server<MoveToTarget>(this, "move_to_target", std::bind(&Eval5RobotNode::handle_goal_move_to_target, this, std::placeholders::_1, std::placeholders::_2), std::bind(&Eval5RobotNode::handle_cancel_move_to_target, this, std::placeholders::_1), std::bind(&Eval5RobotNode::handle_accepted_move_to_target, this, std::placeholders::_1));
  // Timer: status_timer
  // Period: 1.0 sec
  // Callback: status_timer_callback()
  // Description: Publish robot status periodically.
  status_timer_ = this->create_wall_timer(std::chrono::milliseconds(1000), std::bind(&Eval5RobotNode::status_timer_callback, this));
  status_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval5RobotNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (robot_status_publisher_) { robot_status_publisher_->on_activate(); }
  if (status_timer_) { status_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval5RobotNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (robot_status_publisher_) { robot_status_publisher_->on_deactivate(); }
  if (status_timer_) { status_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5RobotNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   robot_status_publisher_.reset();
//   cmd_vel_sub_.reset();
//   set_mode_srv_.reset();
//   move_to_target_server_.reset();
//   status_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5RobotNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5RobotNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Topic callbacks
// ============================================================

void Eval5RobotNode::cmd_vel_callback(const geometry_msgs::msg::Twist::SharedPtr msg)
{
  // Subscribe Topic: cmd_vel
  // Type: geometry_msgs/msg/Twist
  // Description: Receive velocity command.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Service callbacks and clients
// ============================================================

void Eval5RobotNode::handle_set_mode(const std::shared_ptr<eval5_robot_cpp_pkg_interfaces::srv::SetMode::Request> request, std::shared_ptr<eval5_robot_cpp_pkg_interfaces::srv::SetMode::Response> response)
{
  // Service Server: set_mode
  // Type: eval5_robot_cpp_pkg_interfaces/srv/SetMode
  // Description: Set robot operation mode.
  // TODO: Read the request and implement the service behavior.
  // TODO: Fill the response fields before returning.
  (void)request;
  (void)response;
}

// ============================================================
// Action callbacks and clients
// ============================================================

rclcpp_action::GoalResponse Eval5RobotNode::handle_goal_move_to_target(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const MoveToTarget::Goal> goal)
{
  // Action Server: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Move robot to target pose.
  (void)uuid;
  (void)goal;
  return rclcpp_action::GoalResponse::ACCEPT_AND_EXECUTE;
}

rclcpp_action::CancelResponse Eval5RobotNode::handle_cancel_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle)
{
  // Action Server: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Move robot to target pose.
  (void)goal_handle;
  return rclcpp_action::CancelResponse::ACCEPT;
}

void Eval5RobotNode::handle_accepted_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle)
{
  // Action Server: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Move robot to target pose.
  std::thread{std::bind(&Eval5RobotNode::execute_move_to_target, this, std::placeholders::_1), goal_handle}.detach();
}

void Eval5RobotNode::execute_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle)
{
  // Action Server: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Move robot to target pose.
  const auto goal = goal_handle->get_goal();
  auto result = std::make_shared<MoveToTarget::Result>();

  // TODO: Read the goal and implement the action execution logic.
  // TODO: Set feedback and result fields as needed.
  (void)goal;

  goal_handle->succeed(result);
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval5RobotNode::status_timer_callback()
{
  // Timer: status_timer
  // Period: 1.0 sec
  // Callback: status_timer_callback()
  // Description: Publish robot status periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval5_robot_cpp_pkg
