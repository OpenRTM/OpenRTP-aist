#ifndef EVAL3_SERVICE_SERVER_CPP_PKG__EVAL3_SERVICE_SERVER_NODE_HPP_
#define EVAL3_SERVICE_SERVER_CPP_PKG__EVAL3_SERVICE_SERVER_NODE_HPP_

#include "rclcpp/rclcpp.hpp"
#include "rclcpp_lifecycle/lifecycle_node.hpp"
#include "std_srvs/srv/trigger.hpp"
#include "std_srvs/srv/set_bool.hpp"
#include <future>
#include <memory>
#include <string>
#include <vector>

namespace eval3_service_server_cpp_pkg
{

class Eval3ServiceServerNode : public rclcpp_lifecycle::LifecycleNode
{
public:

  explicit Eval3ServiceServerNode(const rclcpp::NodeOptions & options = rclcpp::NodeOptions());
  ~Eval3ServiceServerNode() override = default;

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

  /*!
   * Reset internal state.
   * - Argument: Doc Arg Server1
   * - Return: Doc Ret Server1
   */
  rclcpp::Service<std_srvs::srv::Trigger>::SharedPtr reset_srv_;
  void handle_reset(const std::shared_ptr<std_srvs::srv::Trigger::Request> request, std::shared_ptr<std_srvs::srv::Trigger::Response> response);
  /*!
   * Set boolean state.
   * - Argument: Doc Arg Server2
   * - Return: Doc Ret Server2
   */
  rclcpp::Service<std_srvs::srv::SetBool>::SharedPtr set_bool_srv_;
  void handle_set_bool(const std::shared_ptr<std_srvs::srv::SetBool::Request> request, std::shared_ptr<std_srvs::srv::SetBool::Response> response);

};

}  // namespace eval3_service_server_cpp_pkg

#endif  // EVAL3_SERVICE_SERVER_CPP_PKG__EVAL3_SERVICE_SERVER_NODE_HPP_
