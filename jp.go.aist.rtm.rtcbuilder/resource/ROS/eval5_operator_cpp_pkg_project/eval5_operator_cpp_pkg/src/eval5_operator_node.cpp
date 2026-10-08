#include "eval5_operator_cpp_pkg/eval5_operator_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval5_operator_cpp_pkg
{

Eval5OperatorNode::Eval5OperatorNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval5_operator_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
  declare_parameters();
}

// ============================================================
// Parameter handling
// ============================================================

void Eval5OperatorNode::declare_parameters()
{
  rcl_interfaces::msg::ParameterDescriptor desc_operator_name;
  desc_operator_name.description = "Operator name.";
  desc_operator_name.read_only = true;
  this->declare_parameter("operator_name", std::string("operator"), desc_operator_name);
  param_cb_handle_ = this->add_on_set_parameters_callback(std::bind(&Eval5OperatorNode::on_set_parameters, this, std::placeholders::_1));
}

rcl_interfaces::msg::SetParametersResult Eval5OperatorNode::on_set_parameters(const std::vector<rclcpp::Parameter> & params)
{
  rcl_interfaces::msg::SetParametersResult result;
  result.successful = true;

  for (const auto & param : params) {
    if (param.get_name() == "operator_name") {
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

CallbackReturn Eval5OperatorNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  // Publish Topic: cmd_vel
  // Type: geometry_msgs/msg/Twist
  // Description: Publish velocity command.
  cmd_vel_publisher_ = this->create_publisher<geometry_msgs::msg::Twist>("cmd_vel", rclcpp::QoS(10).reliable());
  robot_status_sub_ = this->create_subscription<eval5_robot_cpp_pkg_interfaces::msg::CustomStatus>("robot_status", rclcpp::QoS(10).reliable(), std::bind(&Eval5OperatorNode::robot_status_callback, this, std::placeholders::_1));
  set_mode_client_ = this->create_client<eval5_robot_cpp_pkg_interfaces::srv::SetMode>("set_mode");
  move_to_target_client_ = rclcpp_action::create_client<MoveToTarget>(this, "move_to_target");
  // Timer: command_timer
  // Period: 1.0 sec
  // Callback: command_timer_callback()
  // Description: Publish commands periodically.
  command_timer_ = this->create_wall_timer(std::chrono::milliseconds(1000), std::bind(&Eval5OperatorNode::command_timer_callback, this));
  command_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval5OperatorNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_activate(); }
  if (command_timer_) { command_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval5OperatorNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (cmd_vel_publisher_) { cmd_vel_publisher_->on_deactivate(); }
  if (command_timer_) { command_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5OperatorNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   cmd_vel_publisher_.reset();
//   robot_status_sub_.reset();
//   set_mode_client_.reset();
//   move_to_target_client_.reset();
//   command_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5OperatorNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval5OperatorNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Topic callbacks
// ============================================================

void Eval5OperatorNode::robot_status_callback(const eval5_robot_cpp_pkg_interfaces::msg::CustomStatus::SharedPtr msg)
{
  // Subscribe Topic: robot_status
  // Type: eval5_robot_cpp_pkg_interfaces/msg/CustomStatus
  // Description: Receive custom robot status.
  // TODO: Use the received message to implement this node's behavior.
  // Note: This callback is called each time a message arrives on the subscribed topic.
  (void)msg;
}

// ============================================================
// Service callbacks and clients
// ============================================================

void Eval5OperatorNode::call_set_mode_service()
{
  // Service Client: set_mode
  // Type: eval5_robot_cpp_pkg_interfaces/srv/SetMode
  // Description: Request robot operation mode.
  // Service Client helper: create a request and send it asynchronously to the server.
  if (!set_mode_client_) {
    RCLCPP_WARN(this->get_logger(), "Service client is not configured.");
    return;
  }
  auto request = std::make_shared<eval5_robot_cpp_pkg_interfaces::srv::SetMode::Request>();
  // TODO: Call this helper at the appropriate time in your node logic.
  // TODO: Fill the request fields according to the selected service type.
  // Note: After filling the request, async_send_request() sends it to the service server.
  auto future = set_mode_client_->async_send_request(request);
  (void)future;
}

// ============================================================
// Action callbacks and clients
// ============================================================

void Eval5OperatorNode::send_move_to_target_goal()
{
  // Action Client: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Send move target goal.
  // Action Client helper: create a goal and send it to the action server.
  if (!move_to_target_client_) {
    RCLCPP_WARN(this->get_logger(), "Action client is not configured.");
    return;
  }
  // TODO: Call this goal-sending helper at the appropriate time in your node logic.
  // Note: Goal fields differ by action type, so this generator does not set concrete values.
  // TODO: Create a goal, configure response / feedback / result callbacks, and send it.
  RCLCPP_WARN(this->get_logger(), "Goal send skeleton is disabled. Implement goal send code before use.");
  return;
}

void Eval5OperatorNode::move_to_target_action_response(rclcpp_action::ClientGoalHandle<MoveToTarget>::SharedPtr goal_handle)
{
  // Action Client: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Send move target goal.
  if (!goal_handle) {
    RCLCPP_ERROR(this->get_logger(), "Goal rejected");
    return;
  }
  RCLCPP_INFO(this->get_logger(), "Goal accepted");
}

void Eval5OperatorNode::move_to_target_action_feedback(rclcpp_action::ClientGoalHandle<MoveToTarget>::SharedPtr, const std::shared_ptr<const MoveToTarget::Feedback> feedback)
{
  // Action Client: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Send move target goal.
  // TODO: Use feedback data as needed.
  (void)feedback;
}

void Eval5OperatorNode::move_to_target_action_result(const rclcpp_action::ClientGoalHandle<MoveToTarget>::WrappedResult & result)
{
  // Action Client: move_to_target
  // Type: eval5_robot_cpp_pkg_interfaces/action/MoveToTarget
  // Description: Send move target goal.
  // TODO: Use result data as needed.
  (void)result;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval5OperatorNode::command_timer_callback()
{
  // Timer: command_timer
  // Period: 1.0 sec
  // Callback: command_timer_callback()
  // Description: Publish commands periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval5_operator_cpp_pkg
