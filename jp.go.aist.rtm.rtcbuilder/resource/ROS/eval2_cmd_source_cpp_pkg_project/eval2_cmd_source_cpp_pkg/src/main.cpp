#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval2_cmd_source_cpp_pkg/eval2_cmd_source_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval2_cmd_source_cpp_pkg::Eval2CmdSourceNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
