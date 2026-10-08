#ifndef EVAL3_SERVICE_CLIENT_CPP_PKG__EVAL3_SERVICE_CLIENT_NODE_HPP_
#define EVAL3_SERVICE_CLIENT_CPP_PKG__EVAL3_SERVICE_CLIENT_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "std_srvs/srv/trigger.hpp"
#include "std_srvs/srv/set_bool.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval3_service_client_cpp_pkg
{

class Eval3ServiceClientNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval3ServiceClientNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval3ServiceClientNode() override = default;

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
  // Service handles and callbacks
  // ============================================================

  rclcpp::Client<std_srvs::srv::Trigger>::SharedPtr reset_client_;
  void call_reset_service();
  rclcpp::Client<std_srvs::srv::SetBool>::SharedPtr set_bool_client_;
  void call_set_bool_service();

  // ============================================================
  // Timer handles and callbacks
  // ============================================================

  /*!
   * Call services periodically.
   */
  rclcpp::TimerBase::SharedPtr service_call_timer_;
  void service_call_timer_callback();
};

}  // namespace eval3_service_client_cpp_pkg

#endif  // EVAL3_SERVICE_CLIENT_CPP_PKG__EVAL3_SERVICE_CLIENT_NODE_HPP_
