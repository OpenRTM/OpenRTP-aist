#include "eval8_pose_controller_cpp_pkg/pose_controller_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval8_pose_controller_cpp_pkg
{

PoseControllerNode::PoseControllerNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("pose_controller_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
  declare_parameters();
}

// ============================================================
// Parameter handling
// ============================================================

void PoseControllerNode::declare_parameters()
{
  rcl_interfaces::msg::ParameterDescriptor desc_linear_gain;
  desc_linear_gain.description = "Proportional gain for linear velocity control.";
  this->declare_parameter("linear_gain", 1.0, desc_linear_gain);
  rcl_interfaces::msg::ParameterDescriptor desc_angular_gain;
  desc_angular_gain.description = "Proportional gain for angular velocity control.";
  this->declare_parameter("angular_gain", 2.0, desc_angular_gain);
  rcl_interfaces::msg::ParameterDescriptor desc_max_linear_speed;
  desc_max_linear_speed.description = "Maximum commanded linear velocity.";
  this->declare_parameter("max_linear_speed", 0.5, desc_max_linear_speed);
  rcl_interfaces::msg::ParameterDescriptor desc_max_angular_speed;
  desc_max_angular_speed.description = "Maximum commanded angular velocity.";
  this->declare_parameter("max_angular_speed", 1.0, desc_max_angular_speed);
  rcl_interfaces::msg::ParameterDescriptor desc_goal_tolerance;
  desc_goal_tolerance.description = "Position tolerance used to determine goal completion.";
  this->declare_parameter("goal_tolerance", 0.05, desc_goal_tolerance);
  rcl_interfaces::msg::ParameterDescriptor desc_yaw_tolerance;
  desc_yaw_tolerance.description = "Yaw tolerance used to determine final orientation completion.";
  this->declare_parameter("yaw_tolerance", 0.05, desc_yaw_tolerance);
  rcl_interfaces::msg::ParameterDescriptor desc_odom_timeout_sec;
  desc_odom_timeout_sec.description = "Maximum allowed age of odometry data before stopping the robot.";
  this->declare_parameter("odom_timeout_sec", 0.5, desc_odom_timeout_sec);
  rcl_interfaces::msg::ParameterDescriptor desc_control_enabled;
  desc_control_enabled.description = "Enable or disable velocity control.";
  this->declare_parameter("control_enabled", true, desc_control_enabled);
  param_cb_handle_ = this->add_on_set_parameters_callback(std::bind(&PoseControllerNode::on_set_parameters, this, std::placeholders::_1));
}

rcl_interfaces::msg::SetParametersResult PoseControllerNode::on_set_parameters(const std::vector<rclcpp::Parameter> & params)
{
  rcl_interfaces::msg::SetParametersResult result;
  result.successful = true;

  for (const auto & param : params) {
    if (param.get_name() == "linear_gain") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "angular_gain") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "max_linear_speed") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "max_angular_speed") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "goal_tolerance") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "yaw_tolerance") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "odom_timeout_sec") {
      // TODO: Update internal state when this parameter changes.
      // Note: min / max / step are documentation metadata only. Add explicit validation here if needed.
      const double value = param.as_double();
      (void)value;
      // TODO: Store the parameter value in member variables or runtime settings as needed.
    }
    if (param.get_name() == "control_enabled") {
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

CallbackReturn PoseControllerNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: cmd_vel
  // Type: geometry_msgs/msg/Twist
  // Description: Publish linear and angular velocity commands for the simulated mobile base.
  cmd_vel_publisher_ = this->create_publisher<geometry_msgs::msg::Twist>("cmd_vel", rclcpp::QoS(10).reliable());
  odom_sub_ = this->create_subscription<nav_msgs::msg::Odometry>("odom", rclcpp::QoS(10).best_effort(), std::bind(&PoseControllerNode::odom_callback, this, std::placeholders::_1));
  move_to_pose_server_ = rclcpp_action::create_server<MoveToPose>(this, "move_to_pose", std::bind(&PoseControllerNode::handle_goal_move_to_pose, this, std::placeholders::_1, std::placeholders::_2), std::bind(&PoseControllerNode::handle_cancel_move_to_pose, this, std::placeholders::_1), std::bind(&PoseControllerNode::handle_accepted_move_to_pose, this, std::placeholders::_1));
  // Timer: control_timer
  // Period: 0.05 sec
  // Callback: control_timer_callback()
  // Description: Calculate and publish velocity commands while a move-to-pose goal is active.
  control_timer_ = this->create_wall_timer(std::chrono::milliseconds(50), std::bind(&PoseControllerNode::control_timer_callback, this));
  control_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn PoseControllerNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_activate(); }
  if (control_timer_) { control_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn PoseControllerNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_deactivate(); }
  if (control_timer_) { control_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn PoseControllerNode::on_cleanup(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
  // TODO: Release resources when the node is cleaned up.
  cmd_vel_publisher_.reset();
  odom_sub_.reset();
  move_to_pose_server_.reset();
  control_timer_.reset();
  return CallbackReturn::SUCCESS;
}

CallbackReturn PoseControllerNode::on_shutdown(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
  // TODO: Implement shutdown handling.
  return CallbackReturn::SUCCESS;
}

CallbackReturn PoseControllerNode::on_error(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_error] called.");
  // TODO: Implement error recovery or safe-stop handling.
  return CallbackReturn::SUCCESS;
}

// ============================================================
// Topic callbacks
// ============================================================

void PoseControllerNode::odom_callback(const nav_msgs::msg::Odometry::SharedPtr msg)
{
  // Subscribe Topic: odom
  // Type: nav_msgs/msg/Odometry
  // Description: Receive the current simulated robot pose and velocity.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Action callbacks and clients
// ============================================================

rclcpp_action::GoalResponse PoseControllerNode::handle_goal_move_to_pose(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const MoveToPose::Goal> goal)
{
  // Action Server: move_to_pose
  // Type: eval8_pose_controller_cpp_pkg_interfaces/action/MoveToPose
  // Description: Drive the simulated mobile robot to a requested target pose.
  (void)uuid;
  (void)goal;
  return rclcpp_action::GoalResponse::ACCEPT_AND_EXECUTE;
}

rclcpp_action::CancelResponse PoseControllerNode::handle_cancel_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle)
{
  // Action Server: move_to_pose
  // Type: eval8_pose_controller_cpp_pkg_interfaces/action/MoveToPose
  // Description: Drive the simulated mobile robot to a requested target pose.
  (void)goal_handle;
  return rclcpp_action::CancelResponse::ACCEPT;
}

void PoseControllerNode::handle_accepted_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle)
{
  // Action Server: move_to_pose
  // Type: eval8_pose_controller_cpp_pkg_interfaces/action/MoveToPose
  // Description: Drive the simulated mobile robot to a requested target pose.
  std::thread{std::bind(&PoseControllerNode::execute_move_to_pose, this, std::placeholders::_1), goal_handle}.detach();
}

void PoseControllerNode::execute_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle)
{
  // Action Server: move_to_pose
  // Type: eval8_pose_controller_cpp_pkg_interfaces/action/MoveToPose
  // Description: Drive the simulated mobile robot to a requested target pose.
  const auto goal = goal_handle->get_goal();
  auto result = std::make_shared<MoveToPose::Result>();

  // TODO: Read the goal and implement the action execution logic.
  // TODO: Set feedback and result fields as needed.
  (void)goal;

  goal_handle->succeed(result);
}

// ============================================================
// Timer callbacks
// ============================================================

void PoseControllerNode::control_timer_callback()
{
  // Timer: control_timer
  // Period: 0.05 sec
  // Callback: control_timer_callback()
  // Description: Calculate and publish velocity commands while a move-to-pose goal is active.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval8_pose_controller_cpp_pkg
