cmake_minimum_required(VERSION 3.8)
project(eval2_controller_py_pkg_interfaces)

find_package(ament_cmake REQUIRED)

# Enable rosidl generation after adding custom interfaces.
# find_package(rosidl_default_generators REQUIRED)
# ament_export_dependencies(rosidl_default_runtime)

install(DIRECTORY msg srv action DESTINATION share/${PROJECT_NAME})
install(FILES LICENSE README.md DESTINATION share/${PROJECT_NAME})

ament_package()
