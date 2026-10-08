#ifndef EVAL5_OPERATOR_CPP_PKG__EVAL5_OPERATOR_NODE_HPP_
#define EVAL5_OPERATOR_CPP_PKG__EVAL5_OPERATOR_NODE_HPP_

#include "eval5_robot_cpp_pkg_interfaces/action/move_to_target.hpp"
#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "geometry_msgs/msg/twist.hpp"
#include "eval5_robot_cpp_pkg_interfaces/msg/custom_status.hpp"
#include "rcl_interfaces/msg/floating_point_range.hpp"
#include "rcl_interfaces/msg/integer_range.hpp"
#include "rcl_interfaces/msg/parameter_descriptor.hpp"
#include "rcl_interfaces/msg/set_parameters_result.hpp"
#include "eval5_robot_cpp_pkg_interfaces/srv/set_mode.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval5_operator_cpp_pkg
{

class Eval5OperatorNode : public rclcpp_lifecycle::LifecycleNode
{
public:
  using MoveToTarget = eval5_robot_cpp_pkg_interfaces::action::MoveToTarget;

  explicit Eval5OperatorNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval5OperatorNode() override = default;

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
  // Parameter handling
  // ============================================================

  void declare_parameters();
  rclcpp::node_interfaces::OnSetParametersCallbackHandle::SharedPtr param_cb_handle_;
  rcl_interfaces::msg::SetParametersResult on_set_parameters(const std::vector<rclcpp::Parameter> & params);

  // ============================================================
  // Topic handles and callbacks
  // ============================================================

  rclcpp_lifecycle::LifecyclePublisher<geometry_msgs::msg::Twist>::SharedPtr cmd_vel_publisher_;
  rclcpp::Subscription<eval5_robot_cpp_pkg_interfaces::msg::CustomStatus>::SharedPtr robot_status_sub_;
  void robot_status_callback(const eval5_robot_cpp_pkg_interfaces::msg::CustomStatus::SharedPtr msg);

  // ============================================================
  // Service handles and callbacks
  // ============================================================

  rclcpp::Client<eval5_robot_cpp_pkg_interfaces::srv::SetMode>::SharedPtr set_mode_client_;
  void call_set_mode_service();

  // ============================================================
  // Action handles and callbacks
  // ============================================================

  rclcpp_action::Client<MoveToTarget>::SharedPtr move_to_target_client_;
  void send_move_to_target_goal();
  void move_to_target_action_response(rclcpp_action::ClientGoalHandle<MoveToTarget>::SharedPtr goal_handle);
  void move_to_target_action_feedback(rclcpp_action::ClientGoalHandle<MoveToTarget>::SharedPtr, const std::shared_ptr<const MoveToTarget::Feedback> feedback);
  void move_to_target_action_result(const rclcpp_action::ClientGoalHandle<MoveToTarget>::WrappedResult & result);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Publish commands periodically.
   */
  rclcpp::TimerBase::SharedPtr command_timer_;
  void command_timer_callback();
};

}  // namespace eval5_operator_cpp_pkg

#endif  // EVAL5_OPERATOR_CPP_PKG__EVAL5_OPERATOR_NODE_HPP_
