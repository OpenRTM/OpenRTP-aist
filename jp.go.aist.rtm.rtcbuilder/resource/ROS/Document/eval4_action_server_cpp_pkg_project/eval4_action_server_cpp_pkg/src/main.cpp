#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval4_action_server_cpp_pkg/eval4_action_server_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval4_action_server_cpp_pkg::Eval4ActionServerNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
