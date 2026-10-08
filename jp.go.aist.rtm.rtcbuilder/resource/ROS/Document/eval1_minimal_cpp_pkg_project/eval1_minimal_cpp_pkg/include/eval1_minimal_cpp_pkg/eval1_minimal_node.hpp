// -*- C++ -*-
/*!
 * @file  eval1_minimal_node.hpp
 * @brief Minimal C++ Lifecycle Node sample.
 *
 * @author rsdlab <todo@example.com>
 * CreatorSample
 *
 */

#ifndef EVAL1_MINIMAL_CPP_PKG__EVAL1_MINIMAL_NODE_HPP_
#define EVAL1_MINIMAL_CPP_PKG__EVAL1_MINIMAL_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval1_minimal_cpp_pkg
{

/*!
 * @class Eval1MinimalNode
 * @brief Minimal C++ Lifecycle Node sample.
 *
 * InOutSample
 *
 * AlgorithmSample
 *
 * ReferSample
 *
 */
class Eval1MinimalNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval1MinimalNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval1MinimalNode() override = default;

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
};

}  // namespace eval1_minimal_cpp_pkg

#endif  // EVAL1_MINIMAL_CPP_PKG__EVAL1_MINIMAL_NODE_HPP_
