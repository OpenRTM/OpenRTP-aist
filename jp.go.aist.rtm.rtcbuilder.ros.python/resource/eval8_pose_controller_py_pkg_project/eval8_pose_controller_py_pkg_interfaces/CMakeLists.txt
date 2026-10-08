cmake_minimum_required(VERSION 3.8)
project(eval8_pose_controller_py_pkg_interfaces)

# Core dependencies
find_package(ament_cmake REQUIRED)
find_package(rosidl_default_generators REQUIRED)

# Custom interface dependencies
find_package(builtin_interfaces REQUIRED)
find_package(std_msgs REQUIRED)

# Action interface dependencies
find_package(action_msgs REQUIRED)
rosidl_generate_interfaces(${PROJECT_NAME}
  "action/MoveToPose.action"
  DEPENDENCIES builtin_interfaces std_msgs action_msgs
)

install(DIRECTORY msg srv action DESTINATION share/${PROJECT_NAME})
install(FILES LICENSE README.md DESTINATION share/${PROJECT_NAME})

ament_package()
