#ifndef EVAL8_POSE_CONTROLLER_CPP_PKG__POSE_CONTROLLER_NODE_HPP_
#define EVAL8_POSE_CONTROLLER_CPP_PKG__POSE_CONTROLLER_NODE_HPP_

#include "eval8_pose_controller_cpp_pkg_interfaces/action/move_to_pose.hpp"
#include "rclcpp/rclcpp.hpp"
#include "rclcpp_action/rclcpp_action.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "geometry_msgs/msg/twist.hpp"
#include "nav_msgs/msg/odometry.hpp"
#include "rcl_interfaces/msg/floating_point_range.hpp"
#include "rcl_interfaces/msg/integer_range.hpp"
#include "rcl_interfaces/msg/parameter_descriptor.hpp"
#include "rcl_interfaces/msg/set_parameters_result.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval8_pose_controller_cpp_pkg
{

class PoseControllerNode : public rclcpp_lifecycle::LifecycleNode
{
public:
  using MoveToPose = eval8_pose_controller_cpp_pkg_interfaces::action::MoveToPose;
  using GoalHandleMoveToPose = rclcpp_action::ServerGoalHandle<MoveToPose>;

  explicit PoseControllerNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~PoseControllerNode() override = default;

  // ============================================================
  // Lifecycle callbacks
  // ============================================================

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_configure(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_activate(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_deactivate(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_cleanup(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_shutdown(const rclcpp_lifecycle::State & state) override;

  rclcpp_lifecycle::node_interfaces::LifecycleNodeInterface::CallbackReturn
  on_error(const rclcpp_lifecycle::State & state) override;

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
  rclcpp::Subscription<nav_msgs::msg::Odometry>::SharedPtr odom_sub_;
  void odom_callback(const nav_msgs::msg::Odometry::SharedPtr msg);

  // ============================================================
  // Action handles and callbacks
  // ============================================================

  rclcpp_action::Server<MoveToPose>::SharedPtr move_to_pose_server_;
  rclcpp_action::GoalResponse handle_goal_move_to_pose(const rclcpp_action::GoalUUID & uuid, std::shared_ptr<const MoveToPose::Goal> goal);
  rclcpp_action::CancelResponse handle_cancel_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle);
  void handle_accepted_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle);
  void execute_move_to_pose(const std::shared_ptr<GoalHandleMoveToPose> goal_handle);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Calculate and publish velocity commands while a move-to-pose goal is active.
   */
  rclcpp::TimerBase::SharedPtr control_timer_;
  void control_timer_callback();
};

}  // namespace eval8_pose_controller_cpp_pkg

#endif  // EVAL8_POSE_CONTROLLER_CPP_PKG__POSE_CONTROLLER_NODE_HPP_
