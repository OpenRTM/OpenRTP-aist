#ifndef EVAL4_ACTION_CLIENT_CPP_PKG__EVAL4_ACTION_CLIENT_NODE_HPP_
#define EVAL4_ACTION_CLIENT_CPP_PKG__EVAL4_ACTION_CLIENT_NODE_HPP_

#include "example_interfaces/action/fibonacci.hpp"
#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval4_action_client_cpp_pkg
{

class Eval4ActionClientNode : public rclcpp_lifecycle::LifecycleNode
{
public:
  using Fibonacci = example_interfaces::action::Fibonacci;

  explicit Eval4ActionClientNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval4ActionClientNode() override = default;

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
   * Send Fibonacci action goals.
   * - Goal: Doc Goal
   * - Feedback: Doc Feedback
   * - Result: Doc Return
   */
  rclcpp_action::Client<Fibonacci>::SharedPtr fibonacci_client_;
  void send_fibonacci_goal();
  void fibonacci_action_response(rclcpp_action::ClientGoalHandle<Fibonacci>::SharedPtr goal_handle);
  void fibonacci_action_feedback(rclcpp_action::ClientGoalHandle<Fibonacci>::SharedPtr, const std::shared_ptr<const Fibonacci::Feedback> feedback);
  void fibonacci_action_result(const rclcpp_action::ClientGoalHandle<Fibonacci>::WrappedResult & result);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Trigger action goal helper periodically.
   */
  rclcpp::TimerBase::SharedPtr action_goal_timer_;
  void action_goal_timer_callback();
};

}  // namespace eval4_action_client_cpp_pkg

#endif  // EVAL4_ACTION_CLIENT_CPP_PKG__EVAL4_ACTION_CLIENT_NODE_HPP_
