# eval5_robot_py_pkg

## Description

Python robot-side all-in-one sample node with topics, services, actions, timers, parameters, and custom interfaces.

## Package Summary

- Package: `eval5_robot_py_pkg`
- Node executable: `eval5_robot_node`
- Node class: `Eval5RobotNode`
- Language: `Python`
- License: `LGPL-3.0`


This node uses custom interfaces from the companion `eval5_robot_py_pkg_interfaces` package. Build both packages from the same workspace.

## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval5_robot_node.launch.py`
- `config/params.yaml`
- `setup.py`
- `setup.cfg`
- `eval5_robot_py_pkg/eval5_robot_node.py`

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

The companion `eval5_robot_py_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval5_robot_py_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `eval5_robot_py_pkg_interfaces`
- `geometry_msgs`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval5_robot_py_pkg eval5_robot_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /eval5_robot_node
ros2 lifecycle set /eval5_robot_node configure
ros2 lifecycle set /eval5_robot_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /eval5_robot_node deactivate
```

## Topics

| Direction | Name | Type | QoS | Callback / Variable | Summary |
| --- | --- | --- | --- | --- | --- |
| Publish | robot_status | eval5_robot_py_pkg_interfaces/msg/CustomStatus | Reliable, Keep Last, depth=10 | Publisher variable: robot_status_publisher | Publish custom robot status. |
| Subscribe | cmd_vel | geometry_msgs/msg/Twist | Reliable, Keep Last, depth=10 | Callback: cmd_vel_callback | Receive velocity command. |

Useful commands:

```bash
ros2 topic list
ros2 topic info robot_status
ros2 topic echo robot_status
ros2 topic info cmd_vel
# ros2 topic pub cmd_vel geometry_msgs/msg/Twist '<yaml-message>'
```

## Services

| Role | Name | Type | Callback / Variable | Summary |
| --- | --- | --- | --- | --- |
| Server | set_mode | eval5_robot_py_pkg_interfaces/srv/SetMode | set_mode | Set robot operation mode. |

Useful commands:

```bash
ros2 service list
ros2 service type set_mode
# ros2 service call set_mode eval5_robot_py_pkg_interfaces/srv/SetMode '<yaml-request>'
```

## Actions

| Role | Name | Type | Callback Base | Summary |
| --- | --- | --- | --- | --- |
| Server | move_to_target | eval5_robot_py_pkg_interfaces/action/MoveToTarget | execute_move_to_target | Move robot to target pose. |

Useful commands:

```bash
ros2 action list
ros2 action info move_to_target
# ros2 action send_goal move_to_target eval5_robot_py_pkg_interfaces/action/MoveToTarget '<yaml-goal>' --feedback
```

## Parameters

| Name | Type | Default | Min | Max | Step | ReadOnly | Summary |
| --- | --- | --- | --- | --- | --- | --- | --- |
| max_speed | double | 1.0 | 0.0 | 5.0 | 0.1 | false | Maximum linear speed. |
| robot_id | string | robot_01 | - | - | - | true | Robot identifier. |

Parameter values are generated in `config/params.yaml`.

`ReadOnly=true` is generated as ROS 2 `ParameterDescriptor.read_only`. This marks
the parameter as read-only after declaration. `min`, `max`, and `step` are
preserved as ROS Profile / documentation information only. They are not used for
automatic runtime checks in the generated node. If the node must reject
out-of-range values, implement the required validation manually in
`on_set_parameters()`.

Useful commands:

```bash
ros2 param list /eval5_robot_node
ros2 param dump /eval5_robot_node
# ros2 param set /eval5_robot_node <parameter_name> <value>
```

## Timers

| Name | Period | Callback | Description |
| --- | --- | --- | --- |
| status_timer | 1.0 sec | status_timer_callback | Publish robot status periodically. |

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
ros2 launch eval5_robot_py_pkg eval5_robot_node.launch.py
ros2 lifecycle set /eval5_robot_node configure
ros2 lifecycle set /eval5_robot_node activate
```
