# eval2_cmd_source_cpp_pkg
## Description
C++ command source node that publishes cmd_vel and subscribes robot_status for evaluation.
## Package Summary
- Package: `eval2_cmd_source_cpp_pkg`
- Node executable: `eval2_cmd_source_node`
- Node class: `Eval2CmdSourceNode`
- Language: `C++`
- License: `BSD-3-Clause`
## Generated Files
- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval2_cmd_source_node.launch.py`
- `config/params.yaml`
- `CMakeLists.txt`
- `include/eval2_cmd_source_cpp_pkg/eval2_cmd_source_node.hpp`
- `src/eval2_cmd_source_node.cpp`
- `src/main.cpp`
## Build
From the generated workspace directory:
```bash
colcon build
source install/setup.bash
```
## Distribution Notes
`.deb` is the Ubuntu / Debian install package format. For official ROS 2 style
distribution, prefer `bloom`; it reads `package.xml` and generates the release
metadata used by the ROS packaging workflow.
This generated package keeps build-time and runtime dependencies explicit in
`package.xml` so the package is easier to review before a bloom release.
For C++ packages, CPack can be used as a simple local `.deb` helper when an optional CPack DEB block is enabled in `CMakeLists.txt`. Keep this separate from the normal `colcon build` flow, and prefer bloom for ROS 2 release workflows.
The companion `eval2_cmd_source_cpp_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval2_cmd_source_cpp_pkg_interfaces`.
## Dependency Rules
Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.
Inferred interface dependencies:
- `geometry_msgs`
- `std_msgs`
Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata, CMake `find_package()`, and `ament_target_dependencies()`,
but the generator does not add arbitrary library includes or API calls for them.
Extra dependencies:
- None
## Run
```bash
ros2 launch eval2_cmd_source_cpp_pkg eval2_cmd_source_node.launch.py
```
## Lifecycle Operation
This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:
```bash
ros2 lifecycle get /eval2_cmd_source_node
ros2 lifecycle set /eval2_cmd_source_node configure
ros2 lifecycle set /eval2_cmd_source_node activate
```
To stop active publishers and timers:
```bash
ros2 lifecycle set /eval2_cmd_source_node deactivate
```
## Topics
| Direction | Name | Type | QoS | Callback / Variable | Summary |
| --- | --- | --- | --- | --- | --- |
| Publish | cmd_vel | geometry_msgs/msg/Twist | Reliable, Keep Last, depth=10 | Publisher variable: cmd_vel_pub | Publish velocity command. |
| Subscribe | robot_status | std_msgs/msg/String | Reliable, Keep Last, depth=10 | Callback: robot_status_callback | Receive robot status text. |
Useful commands:
```bash
ros2 topic list
ros2 topic info cmd_vel
ros2 topic echo cmd_vel
ros2 topic info robot_status
# ros2 topic pub robot_status std_msgs/msg/String '<yaml-message>'
```
## Services
None.
## Actions
None.
## Parameters
None.
This package does not define user parameters.
## Timers
| Name | Period | Callback | Description |
| --- | --- | --- | --- |
| cmd_timer | 1.0 sec | cmd_timer_callback | Publish velocity command periodically. |
Timers are created during `on_configure`, started during `on_activate`, and stopped during `on_deactivate`.
## Manual Editing Guide
- Add topic receive logic in generated subscriber callbacks.
- Add publish logic after the lifecycle node is active. Lifecycle publishers should publish after activation.
- Add service server behavior in generated service callback functions.
- Fill service client request fields before calling the generated client helper.
- Fill action goal, feedback, and result fields according to the selected action type.
- Add timer behavior in generated timer callbacks.
- Keep reusable helper functions near the generated callback that uses them, or add a clearly named user helper section.
## Quick Evaluation Checklist
```bash
colcon build
source install/setup.bash
ros2 launch eval2_cmd_source_cpp_pkg eval2_cmd_source_node.launch.py
ros2 lifecycle set /eval2_cmd_source_node configure
ros2 lifecycle set /eval2_cmd_source_node activate
```
