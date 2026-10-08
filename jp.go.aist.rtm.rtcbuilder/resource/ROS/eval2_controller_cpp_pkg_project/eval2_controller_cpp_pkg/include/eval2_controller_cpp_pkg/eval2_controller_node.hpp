#ifndef EVAL2_CONTROLLER_CPP_PKG__EVAL2_CONTROLLER_NODE_HPP_
#define EVAL2_CONTROLLER_CPP_PKG__EVAL2_CONTROLLER_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "std_msgs/msg/string.hpp"
#include "geometry_msgs/msg/twist.hpp"
#include "rcl_interfaces/msg/floating_point_range.hpp"
#include "rcl_interfaces/msg/integer_range.hpp"
#include "rcl_interfaces/msg/parameter_descriptor.hpp"
#include "rcl_interfaces/msg/set_parameters_result.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval2_controller_cpp_pkg
{

class Eval2ControllerNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval2ControllerNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval2ControllerNode() override = default;

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

  rclcpp_lifecycle::LifecyclePublisher<std_msgs::msg::String>::SharedPtr robot_status_publisher_;
  rclcpp::Subscription<geometry_msgs::msg::Twist>::SharedPtr cmd_vel_sub_;
  void cmd_vel_callback(const geometry_msgs::msg::Twist::SharedPtr msg);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Publish status periodically.
   */
  rclcpp::TimerBase::SharedPtr status_timer_;
  void status_timer_callback();
};

}  // namespace eval2_controller_cpp_pkg

#endif  // EVAL2_CONTROLLER_CPP_PKG__EVAL2_CONTROLLER_NODE_HPP_
