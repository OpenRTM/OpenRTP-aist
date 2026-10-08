#include <memory>
#include "rclcpp/rclcpp.hpp"
#include "eval5_robot_cpp_pkg/eval5_robot_node.hpp"

int main(int argc, char ** argv)
{
  rclcpp::init(argc, argv);
  auto node = std::make_shared<eval5_robot_cpp_pkg::Eval5RobotNode>();
  rclcpp::spin(node->get_node_base_interface());
  rclcpp::shutdown();
  return 0;
}
