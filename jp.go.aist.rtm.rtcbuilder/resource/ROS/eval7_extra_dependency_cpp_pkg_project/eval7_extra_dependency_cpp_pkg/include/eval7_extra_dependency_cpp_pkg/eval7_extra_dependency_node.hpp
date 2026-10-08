#ifndef EVAL7_EXTRA_DEPENDENCY_CPP_PKG__EVAL7_EXTRA_DEPENDENCY_NODE_HPP_
#define EVAL7_EXTRA_DEPENDENCY_CPP_PKG__EVAL7_EXTRA_DEPENDENCY_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "std_msgs/msg/string.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval7_extra_dependency_cpp_pkg
{

class Eval7ExtraDependencyNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval7ExtraDependencyNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval7ExtraDependencyNode() override = default;

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
  // Topic handles and callbacks
  // ============================================================

  rclcpp_lifecycle::LifecyclePublisher<std_msgs::msg::String>::SharedPtr output_text_publisher_;
  rclcpp::Subscription<std_msgs::msg::String>::SharedPtr input_text_sub_;
  void input_text_callback(const std_msgs::msg::String::SharedPtr msg);

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Periodic placeholder for user logic.
   */
  rclcpp::TimerBase::SharedPtr text_timer_;
  void text_timer_callback();
};

}  // namespace eval7_extra_dependency_cpp_pkg

#endif  // EVAL7_EXTRA_DEPENDENCY_CPP_PKG__EVAL7_EXTRA_DEPENDENCY_NODE_HPP_
