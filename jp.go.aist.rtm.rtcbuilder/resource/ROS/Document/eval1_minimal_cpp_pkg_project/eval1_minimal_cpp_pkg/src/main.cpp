#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval1_minimal_cpp_pkg/eval1_minimal_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval1_minimal_cpp_pkg::Eval1MinimalNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
