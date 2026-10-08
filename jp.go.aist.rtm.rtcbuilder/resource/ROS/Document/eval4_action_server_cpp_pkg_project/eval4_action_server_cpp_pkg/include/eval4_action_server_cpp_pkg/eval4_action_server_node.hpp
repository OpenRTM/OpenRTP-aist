#ifndef EVAL4_ACTION_SERVER_CPP_PKG__EVAL4_ACTION_SERVER_NODE_HPP_
#define EVAL4_ACTION_SERVER_CPP_PKG__EVAL4_ACTION_SERVER_NODE_HPP_

#include "example_interfaces/action/fibonacci.hpp"
#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval4_action_server_cpp_pkg
{

class Eval4ActionServerNode : public rclcpp_lifecycle::LifecycleNode
{
public:
  using Fibonacci = example_interfaces::action::Fibonacci;
  using GoalHandleFibonacci = rclcpp_action::ServerGoalHandle<Fibonacci>;

  explicit Eval4ActionServerNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval4ActionServerNode() override = default;

  // ============================================================
  // Lifecycle callbacks
  // ============================================================

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_configure(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_activate(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_deactivate(const rclcpp_lifecycle::State & state) override;

  // Optional: on_cleanup. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_cleanup(const rclcpp_lifecycle::State & state) override;

  // Optional: on_shutdown. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_shutdown(const rclcpp_lifecycle::State & state) override;

  // Optional: on_error. Uncomment this declaration and the matching definition to use it.
  //   rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  //   on_error(const rclcpp_lifecycle::State & state) override;

private:
  // ============================================================
  // Action handles and callbacks
  // ============================================================

  /*!
   * Execute Fibonacci action goals.
   * - Goal: Goal Status
   * - Feedback: Feedback value
   * - Result: Result value
   */
  rclcpp_action::Server<Fibonacci>::SharedPtr fibonacci_server_;
  rclcpp_action::GoalResponse handle_goal_fibonacci(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const Fibonacci::Goal> goal);
  rclcpp_action::CancelResponse handle_cancel_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle);
  void handle_accepted_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle);
  void execute_fibonacci(const std::shared_ptr<GoalHandleFibonacci> goal_handle);

};

}  // namespace eval4_action_server_cpp_pkg

#endif  // EVAL4_ACTION_SERVER_CPP_PKG__EVAL4_ACTION_SERVER_NODE_HPP_
