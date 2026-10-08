#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval7_extra_dependency_cpp_pkg/eval7_extra_dependency_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval7_extra_dependency_cpp_pkg::Eval7ExtraDependencyNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
