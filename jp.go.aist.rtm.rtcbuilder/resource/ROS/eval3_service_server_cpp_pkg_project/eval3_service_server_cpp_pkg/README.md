# eval3_service_server_cpp_pkg

## Description

C++ service server node with Trigger and SetBool services.

## Package Summary

- Package: `eval3_service_server_cpp_pkg`
- Node executable: `eval3_service_server_node`
- Node class: `Eval3ServiceServerNode`
- Language: `C++`
- License: `Apache-2.0`


## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval3_service_server_node.launch.py`
- `config/params.yaml`
- `CMakeLists.txt`
- `include/eval3_service_server_cpp_pkg/eval3_service_server_node.hpp`
- `src/eval3_service_server_node.cpp`
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

The companion `eval3_service_server_cpp_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval3_service_server_cpp_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `std_srvs`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata, CMake `find_package()`, and `ament_target_dependencies()`,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval3_service_server_cpp_pkg eval3_service_server_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /eval3_service_server_node
ros2 lifecycle set /eval3_service_server_node configure
ros2 lifecycle set /eval3_service_server_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /eval3_service_server_node deactivate
```

## Topics

None.

## Services

| Role | Name | Type | Callback / Variable | Summary |
| --- | --- | --- | --- | --- |
| Server | reset | std_srvs/srv/Trigger | handle_reset | Reset internal state. |
| Server | set_bool | std_srvs/srv/SetBool | handle_set_bool | Set boolean state. |

Useful commands:

```bash
ros2 service list
ros2 service type reset
# ros2 service call reset std_srvs/srv/Trigger '<yaml-request>'
ros2 service type set_bool
# ros2 service call set_bool std_srvs/srv/SetBool '<yaml-request>'
```

## Actions

None.

## Parameters

None.

This package does not define user parameters.

## Timers

None.

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
ros2 launch eval3_service_server_cpp_pkg eval3_service_server_node.launch.py
ros2 lifecycle set /eval3_service_server_node configure
ros2 lifecycle set /eval3_service_server_node activate
```
