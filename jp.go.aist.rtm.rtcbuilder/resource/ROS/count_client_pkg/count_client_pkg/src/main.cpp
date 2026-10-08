#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "count_client_pkg/count_client_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<count_client_pkg::CountClientNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
