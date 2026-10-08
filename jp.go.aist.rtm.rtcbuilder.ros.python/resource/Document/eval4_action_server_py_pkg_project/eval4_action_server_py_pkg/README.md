# eval4_action_server_py_pkg

## Description

Python action server node using example_interfaces Fibonacci action.

## Package Summary

- Package: `eval4_action_server_py_pkg`
- Node executable: `eval4_action_server_node`
- Node class: `Eval4ActionServerNode`
- Language: `Python`
- License: `GPL-3.0`


## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval4_action_server_node.launch.py`
- `config/params.yaml`
- `setup.py`
- `setup.cfg`
- `eval4_action_server_py_pkg/eval4_action_server_node.py`

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

The companion `eval4_action_server_py_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval4_action_server_py_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `example_interfaces`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval4_action_server_py_pkg eval4_action_server_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /eval4_action_server_node
ros2 lifecycle set /eval4_action_server_node configure
ros2 lifecycle set /eval4_action_server_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /eval4_action_server_node deactivate
```

## Topics

None.

## Services

None.

## Actions

| Role | Name | Type | Callback Base | Summary |
| --- | --- | --- | --- | --- |
| Server | fibonacci | example_interfaces/action/Fibonacci | execute_fibonacci | Execute Fibonacci action goals. |

Useful commands:

```bash
ros2 action list
ros2 action info fibonacci
# ros2 action send_goal fibonacci example_interfaces/action/Fibonacci '<yaml-goal>' --feedback
```

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
ros2 launch eval4_action_server_py_pkg eval4_action_server_node.launch.py
ros2 lifecycle set /eval4_action_server_node configure
ros2 lifecycle set /eval4_action_server_node activate
```
