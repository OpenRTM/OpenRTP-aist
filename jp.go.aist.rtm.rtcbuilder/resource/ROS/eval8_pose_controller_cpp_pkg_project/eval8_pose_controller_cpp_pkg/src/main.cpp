#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval8_pose_controller_cpp_pkg/pose_controller_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval8_pose_controller_cpp_pkg::PoseControllerNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
