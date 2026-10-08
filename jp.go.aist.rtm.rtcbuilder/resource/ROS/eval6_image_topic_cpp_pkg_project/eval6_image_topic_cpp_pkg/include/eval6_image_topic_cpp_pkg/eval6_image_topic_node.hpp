#ifndef EVAL6_IMAGE_TOPIC_CPP_PKG__EVAL6_IMAGE_TOPIC_NODE_HPP_
#define EVAL6_IMAGE_TOPIC_CPP_PKG__EVAL6_IMAGE_TOPIC_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "rclcpp_lifecycle/lifecycle_publisher.hpp"
#include "sensor_msgs/msg/image.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval6_image_topic_cpp_pkg
{

class Eval6ImageTopicNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval6ImageTopicNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval6ImageTopicNode() override = default;

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

  rclcpp_lifecycle::LifecyclePublisher<sensor_msgs::msg::Image>::SharedPtr image_debug_publisher_;
  rclcpp::Subscription<sensor_msgs::msg::Image>::SharedPtr image_raw_sub_;
  void image_raw_callback(const sensor_msgs::msg::Image::SharedPtr msg);

};

}  // namespace eval6_image_topic_cpp_pkg

#endif  // EVAL6_IMAGE_TOPIC_CPP_PKG__EVAL6_IMAGE_TOPIC_NODE_HPP_
