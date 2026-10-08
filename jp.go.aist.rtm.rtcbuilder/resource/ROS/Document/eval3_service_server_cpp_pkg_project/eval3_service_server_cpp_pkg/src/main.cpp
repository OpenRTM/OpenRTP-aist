#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval3_service_server_cpp_pkg/eval3_service_server_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval3_service_server_cpp_pkg::Eval3ServiceServerNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
