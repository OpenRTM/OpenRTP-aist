cmake_minimum_required(VERSION 3.8)
project(eval4_action_server_cpp_pkg)

if(CMAKE_COMPILER_IS_GNUCXX OR CMAKE_CXX_COMPILER_ID MATCHES "Clang")
  add_compile_options(-Wall -Wextra -Wpedantic)
endif()

find_package(ament_cmake REQUIRED)
# Core dependencies
find_package(lifecycle_msgs REQUIRED)
find_package(rclcpp REQUIRED)
find_package(rclcpp_lifecycle REQUIRED)

# Action dependencies
find_package(action_msgs REQUIRED)
find_package(example_interfaces REQUIRED)
find_package(rclcpp_action REQUIRED)
add_library(${PROJECT_NAME}_lib src/eval4_action_server_node.cpp)
target_include_directories(${PROJECT_NAME}_lib PUBLIC
  $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
  $<INSTALL_INTERFACE:include>
)
ament_target_dependencies(${PROJECT_NAME}_lib
  lifecycle_msgs
  rclcpp
  rclcpp_lifecycle
  example_interfaces
  action_msgs
  rclcpp_action
)

add_executable(eval4_action_server_node src/main.cpp)
target_link_libraries(eval4_action_server_node ${PROJECT_NAME}_lib)

install(TARGETS ${PROJECT_NAME}_lib eval4_action_server_node
  ARCHIVE DESTINATION lib
  LIBRARY DESTINATION lib
  RUNTIME DESTINATION lib/${PROJECT_NAME}
)
install(DIRECTORY include/ DESTINATION include)
install(DIRECTORY launch/ DESTINATION share/${PROJECT_NAME}/launch)
install(DIRECTORY config/ DESTINATION share/${PROJECT_NAME}/config)

install(FILES LICENSE README.md DESTINATION share/${PROJECT_NAME})

# Optional local Debian package generation with CPack.
# This block is commented out so normal colcon builds are not affected.
# For ROS 2 release workflows, prefer bloom.
# To try local .deb generation, uncomment this block after checking package metadata,
# then run cpack -G DEB from the CMake build directory.
#
# string(REPLACE "_" "-" DEBIAN_PROJECT_NAME "${PROJECT_NAME}")
# set(CPACK_GENERATOR "DEB")
# set(CPACK_PACKAGE_NAME "ros-$ENV{ROS_DISTRO}-${DEBIAN_PROJECT_NAME}")
# set(CPACK_PACKAGE_VERSION "0.0.1")
# set(CPACK_PACKAGE_CONTACT "todo@example.com")
# set(CPACK_DEBIAN_PACKAGE_MAINTAINER "rsdlab")
# set(CPACK_DEBIAN_FILE_NAME DEB-DEFAULT)
# set(CPACK_DEBIAN_PACKAGE_SECTION "devel")
# set(CPACK_DEBIAN_PACKAGE_PRIORITY "optional")
# set(CPACK_DEBIAN_PACKAGE_SHLIBDEPS ON)
# include(CPack)

ament_package()
