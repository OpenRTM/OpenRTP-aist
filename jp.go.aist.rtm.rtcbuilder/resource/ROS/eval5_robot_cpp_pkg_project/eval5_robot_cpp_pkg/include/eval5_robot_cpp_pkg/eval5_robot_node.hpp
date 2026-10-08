#ifndef EVAL5_ROBOT_CPP_PKG__EVAL5_ROBOT_NODE_HPP_
#define EVAL5_ROBOT_CPP_PKG__EVAL5_ROBOT_NODE_HPP_

#include "eval5_robot_cpp_pkg_interfaces/action/move_to_target.hpp"
#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "eval5_robot_cpp_pkg_interfaces/msg/custom_status.hpp"
#include "geometry_msgs/msg/twist.hpp"
#include "rcl_interfaces/msg/floating_point_range.hpp"
#include "rcl_interfaces/msg/integer_range.hpp"
#include "rcl_interfaces/msg/parameter_descriptor.hpp"
#include "rcl_interfaces/msg/set_parameters_result.hpp"
#include "eval5_robot_cpp_pkg_interfaces/srv/set_mode.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval5_robot_cpp_pkg
{

class Eval5RobotNode : public rclcpp_lifecycle::LifecycleNode
{
public:
  using MoveToTarget = eval5_robot_cpp_pkg_interfaces::action::MoveToTarget;
  using GoalHandleMoveToTarget = rclcpp_action::ServerGoalHandle<MoveToTarget>;

  explicit Eval5RobotNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval5RobotNode() override = default;

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

  rclcpp_lifecycle::LifecyclePublisher<eval5_robot_cpp_pkg_interfaces::msg::CustomStatus>::SharedPtr robot_status_publisher_;
  rclcpp::Subscription<geometry_msgs::msg::Twist>::SharedPtr cmd_vel_sub_;
  void cmd_vel_callback(const geometry_msgs::msg::Twist::SharedPtr msg);

  // ============================================================
  // Service handles and callbacks
  // ============================================================

  rclcpp::Service<eval5_robot_cpp_pkg_interfaces::srv::SetMode>::SharedPtr set_mode_srv_;
  void handle_set_mode(const std::shared_ptr<eval5_robot_cpp_pkg_interfaces::srv::SetMode::Request> request, std::shared_ptr<eval5_robot_cpp_pkg_interfaces::srv::SetMode::Response> response);

  // ============================================================
  // Action handles and callbacks
  // ============================================================

  rclcpp_action::Server<MoveToTarget>::SharedPtr move_to_target_server_;
  rclcpp_action::GoalResponse handle_goal_move_to_target(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const MoveToTarget::Goal> goal);
  rclcpp_action::CancelResponse handle_cancel_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle);
  void handle_accepted_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle);
  void execute_move_to_target(const std::shared_ptr<GoalHandleMoveToTarget> goal_handle);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Publish robot status periodically.
   */
  rclcpp::TimerBase::SharedPtr status_timer_;
  void status_timer_callback();
};

}  // namespace eval5_robot_cpp_pkg

#endif  // EVAL5_ROBOT_CPP_PKG__EVAL5_ROBOT_NODE_HPP_
