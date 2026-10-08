#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "count_server_pkg/count_server_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<count_server_pkg::CountServerNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
