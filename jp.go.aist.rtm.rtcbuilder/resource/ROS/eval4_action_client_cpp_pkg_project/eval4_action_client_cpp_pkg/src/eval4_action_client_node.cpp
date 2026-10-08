#include "eval4_action_client_cpp_pkg/eval4_action_client_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval4_action_client_cpp_pkg
{

Eval4ActionClientNode::Eval4ActionClientNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval4_action_client_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval4ActionClientNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  fibonacci_client_ = rclcpp_action::create_client<Fibonacci>(this, "fibonacci");
  // Timer: action_goal_timer
  // Period: 3.0 sec
  // Callback: action_goal_timer_callback()
  // Description: Trigger action goal helper periodically.
  action_goal_timer_ = this->create_wall_timer(std::chrono::milliseconds(3000), std::bind(&Eval4ActionClientNode::action_goal_timer_callback, this));
  action_goal_timer_->cancel();
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval4ActionClientNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  if (action_goal_timer_) { action_goal_timer_->reset(); }
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval4ActionClientNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  if (action_goal_timer_) { action_goal_timer_->cancel(); }
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionClientNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   fibonacci_client_.reset();
//   action_goal_timer_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionClientNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionClientNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Action callbacks and clients
// ============================================================

void Eval4ActionClientNode::send_fibonacci_goal()
{
  // Action Client: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Send Fibonacci action goals.
  // Action Client helper: create a goal and send it to the action server.
  if (!fibonacci_client_) {
    RCLCPP_WARN(this->get_logger(), "Action client is not configured.");
    return;
  }
  // TODO: Call this goal-sending helper at the appropriate time in your node logic.
  // Note: Goal fields differ by action type, so this generator does not set concrete values.
  // TODO: Create a goal, configure response / feedback / result callbacks, and send it.
  RCLCPP_WARN(this->get_logger(), "Goal send skeleton is disabled. Implement goal send code before use.");
  return;
}

void Eval4ActionClientNode::fibonacci_action_response(rclcpp_action::ClientGoalHandle<Fibonacci>::SharedPtr goal_handle)
{
  // Action Client: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Send Fibonacci action goals.
  if (!goal_handle) {
    RCLCPP_ERROR(this->get_logger(), "Goal rejected");
    return;
  }
  RCLCPP_INFO(this->get_logger(), "Goal accepted");
}

void Eval4ActionClientNode::fibonacci_action_feedback(rclcpp_action::ClientGoalHandle<Fibonacci>::SharedPtr, const std::shared_ptr<const Fibonacci::Feedback> feedback)
{
  // Action Client: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Send Fibonacci action goals.
  // TODO: Use feedback data as needed.
  (void)feedback;
}

void Eval4ActionClientNode::fibonacci_action_result(const rclcpp_action::ClientGoalHandle<Fibonacci>::WrappedResult & result)
{
  // Action Client: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Send Fibonacci action goals.
  // TODO: Use result data as needed.
  (void)result;
}

// ============================================================
// Timer callbacks
// ============================================================

void Eval4ActionClientNode::action_goal_timer_callback()
{
  // Timer: action_goal_timer
  // Period: 3.0 sec
  // Callback: action_goal_timer_callback()
  // Description: Trigger action goal helper periodically.
  // TODO: Implement the periodic behavior described above.
}

}  // namespace eval4_action_client_cpp_pkg
