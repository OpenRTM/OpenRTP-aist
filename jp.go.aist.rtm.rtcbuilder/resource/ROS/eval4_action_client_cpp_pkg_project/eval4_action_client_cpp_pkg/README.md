# eval4_action_client_cpp_pkg

## Description

C++ action client node that sends Fibonacci goals periodically.

## Package Summary

- Package: `eval4_action_client_cpp_pkg`
- Node executable: `eval4_action_client_node`
- Node class: `Eval4ActionClientNode`
- Language: `C++`
- License: `GPL-3.0`


## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval4_action_client_node.launch.py`
- `config/params.yaml`
- `CMakeLists.txt`
- `include/eval4_action_client_cpp_pkg/eval4_action_client_node.hpp`
- `src/eval4_action_client_node.cpp`
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

The companion `eval4_action_client_cpp_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval4_action_client_cpp_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `example_interfaces`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata, CMake `find_package()`, and `ament_target_dependencies()`,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval4_action_client_cpp_pkg eval4_action_client_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /eval4_action_client_node
ros2 lifecycle set /eval4_action_client_node configure
ros2 lifecycle set /eval4_action_client_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /eval4_action_client_node deactivate
```

## Topics

None.

## Services

None.

## Actions

| Role | Name | Type | Callback Base | Summary |
| --- | --- | --- | --- | --- |
| Client | fibonacci | example_interfaces/action/Fibonacci | fibonacci_action | Send Fibonacci action goals. |

Useful commands:

```bash
ros2 action list
ros2 action info fibonacci
```

## Action Client Note

Action Client helper functions such as `send_<action_name>_goal()` are not
called automatically. Call them from a timer, topic callback, service callback,
or other user-defined logic when the node should send a goal.

## Parameters

None.

This package does not define user parameters.

## Timers

| Name | Period | Callback | Description |
| --- | --- | --- | --- |
| action_goal_timer | 3.0 sec | action_goal_timer_callback | Trigger action goal helper periodically. |

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
ros2 launch eval4_action_client_cpp_pkg eval4_action_client_node.launch.py
ros2 lifecycle set /eval4_action_client_node configure
ros2 lifecycle set /eval4_action_client_node activate
```
