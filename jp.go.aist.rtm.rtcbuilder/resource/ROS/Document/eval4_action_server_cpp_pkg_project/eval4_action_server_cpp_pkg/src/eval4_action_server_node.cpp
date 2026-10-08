#include "eval4_action_server_cpp_pkg/eval4_action_server_node.hpp"

#include <chrono>
#include <functional>
#include <future>
#include <thread>

using CallbackReturn = rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn;

namespace eval4_action_server_cpp_pkg
{

Eval4ActionServerNode::Eval4ActionServerNode(const rclcpp::NodeOptions & options)
: rclcpp_lifecycle::LifecycleNode("eval4_action_server_node", options)
{
  RCLCPP_INFO(this->get_logger(), "[Constructor] Node created.");
}

// ============================================================
// Lifecycle callbacks
// ============================================================

CallbackReturn Eval4ActionServerNode::on_configure(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_configure] called.");
  // TODO: Implement initialization required for this lifecycle transition.
  fibonacci_server_ = rclcpp_action::create_server<Fibonacci>(this, "fibonacci", std::bind(&Eval4ActionServerNode::handle_goal_fibonacci, this, std::placeholders::_1, std::placeholders::_2), std::bind(&Eval4ActionServerNode::handle_cancel_fibonacci, this, std::placeholders::_1), std::bind(&Eval4ActionServerNode::handle_accepted_fibonacci, this, std::placeholders::_1));
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval4ActionServerNode::on_activate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_activate] called.");
  // TODO: Start processing required while the node is active.
  return CallbackReturn::SUCCESS;
}

CallbackReturn Eval4ActionServerNode::on_deactivate(const rclcpp_lifecycle::State &)
{
  RCLCPP_INFO(this->get_logger(), "[on_deactivate] called.");
  // TODO: Stop processing before returning to the inactive state.
  return CallbackReturn::SUCCESS;
}

// Optional: on_cleanup. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionServerNode::on_cleanup(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_cleanup] called.");
//   // TODO: Release resources when the node is cleaned up.
//   fibonacci_server_.reset();
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_shutdown. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionServerNode::on_shutdown(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_shutdown] called.");
//   // TODO: Implement shutdown handling.
//   return CallbackReturn::SUCCESS;
// }

// Optional: on_error. Enable the declaration in the header before using this definition.
// CallbackReturn Eval4ActionServerNode::on_error(const rclcpp_lifecycle::State &)
// {
//   RCLCPP_INFO(this->get_logger(), "[on_error] called.");
//   // TODO: Implement error recovery or safe-stop handling.
//   return CallbackReturn::SUCCESS;
// }

// ============================================================
// Action callbacks and clients
// ============================================================

rclcpp_action::GoalResponse Eval4ActionServerNode::handle_goal_fibonacci(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const Fibonacci::Goal> goal)
{
  // Action Server: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Execute Fibonacci action goals.
  (void)uuid;
  (void)goal;
  return rclcpp_action::GoalResponse::ACCEPT_AND_EXECUTE;
}

rclcpp_action::CancelResponse Eval4ActionServerNode::handle_cancel_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle)
{
  // Action Server: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Execute Fibonacci action goals.
  (void)goal_handle;
  return rclcpp_action::CancelResponse::ACCEPT;
}

void Eval4ActionServerNode::handle_accepted_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle)
{
  // Action Server: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Execute Fibonacci action goals.
  std::thread{std::bind(&Eval4ActionServerNode::execute_fibonacci, this, std::placeholders::_1), goal_handle}.detach();
}

void Eval4ActionServerNode::execute_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle)
{
  // Action Server: fibonacci
  // Type: example_interfaces/action/Fibonacci
  // Description: Execute Fibonacci action goals.
  const auto goal = goal_handle->get_goal();
  auto result = std::make_shared<Fibonacci::Result>();

  // TODO: Read the goal and implement the action execution logic.
  // TODO: Set feedback and result fields as needed.
  (void)goal;

  goal_handle->succeed(result);
}

}  // namespace eval4_action_server_cpp_pkg
