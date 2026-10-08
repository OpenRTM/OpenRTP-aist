# eval8_pose_controller_py_pkg

## Description

Lifecycle pose controller that drives a simulated mobile robot to a target pose using odometry feedback.

## Package Summary

- Package: `eval8_pose_controller_py_pkg`
- Node executable: `pose_controller_node`
- Node class: `PoseControllerNode`
- Language: `Python`
- License: `BSD-3-Clause`


This node uses custom interfaces from the companion `eval8_pose_controller_py_pkg_interfaces` package. Build both packages from the same workspace.

## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/pose_controller_node.launch.py`
- `config/params.yaml`
- `setup.py`
- `setup.cfg`
- `eval8_pose_controller_py_pkg/pose_controller_node.py`

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

The companion `eval8_pose_controller_py_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval8_pose_controller_py_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `eval8_pose_controller_py_pkg_interfaces`
- `geometry_msgs`
- `nav_msgs`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval8_pose_controller_py_pkg pose_controller_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /pose_controller_node
ros2 lifecycle set /pose_controller_node configure
ros2 lifecycle set /pose_controller_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /pose_controller_node deactivate
```

## Topics

| Direction | Name | Type | QoS | Callback / Variable | Summary |
| --- | --- | --- | --- | --- | --- |
| Publish | cmd_vel | geometry_msgs/msg/Twist | Reliable, Keep Last, depth=10 | Publisher variable: cmd_vel_publisher | Publish linear and angular velocity commands for the simulated mobile base. |
| Subscribe | odom | nav_msgs/msg/Odometry | BestEffort, Keep Last, depth=10 | Callback: odom_callback | Receive the current simulated robot pose and velocity. |

Useful commands:

```bash
ros2 topic list
ros2 topic info cmd_vel
ros2 topic echo cmd_vel
ros2 topic info odom
# ros2 topic pub odom nav_msgs/msg/Odometry '<yaml-message>'
```

## Services

None.

## Actions

| Role | Name | Type | Callback Base | Summary |
| --- | --- | --- | --- | --- |
| Server | move_to_pose | eval8_pose_controller_py_pkg_interfaces/action/MoveToPose | execute_move_to_pose | Drive the simulated mobile robot to a requested target pose. |

Useful commands:

```bash
ros2 action list
ros2 action info move_to_pose
# ros2 action send_goal move_to_pose eval8_pose_controller_py_pkg_interfaces/action/MoveToPose '<yaml-goal>' --feedback
```

## Parameters

| Name | Type | Default | Min | Max | Step | ReadOnly | Summary |
| --- | --- | --- | --- | --- | --- | --- | --- |
| linear_gain | double | 1.0 | 0.0 | 10.0 | 0.1 | false | Proportional gain for linear velocity control. |
| angular_gain | double | 2.0 | 0.0 | 10.0 | 0.1 | false | Proportional gain for angular velocity control. |
| max_linear_speed | double | 0.5 | 0.0 | 2.0 | 0.1 | false | Maximum commanded linear velocity. |
| max_angular_speed | double | 1.0 | 0.0 | 5.0 | 0.1 | false | Maximum commanded angular velocity. |
| goal_tolerance | double | 0.05 | 0.001 | 1.0 | 0.01 | false | Position tolerance used to determine goal completion. |
| yaw_tolerance | double | 0.05 | 0.001 | 3.14 | 0.01 | false | Yaw tolerance used to determine final orientation completion. |
| odom_timeout_sec | double | 0.5 | 0.05 | 10.0 | 0.05 | false | Maximum allowed age of odometry data before stopping the robot. |
| control_enabled | bool | true | - | - | - | false | Enable or disable velocity control. |

Parameter values are generated in `config/params.yaml`.

`ReadOnly=true` is generated as ROS 2 `ParameterDescriptor.read_only`. This marks
the parameter as read-only after declaration. `min`, `max`, and `step` are
preserved as ROS Profile / documentation information only. They are not used for
automatic runtime checks in the generated node. If the node must reject
out-of-range values, implement the required validation manually in
`on_set_parameters()`.

Useful commands:

```bash
ros2 param list /pose_controller_node
ros2 param dump /pose_controller_node
# ros2 param set /pose_controller_node <parameter_name> <value>
```

## Timers

| Name | Period | Callback | Description |
| --- | --- | --- | --- |
| control_timer | 0.05 sec | control_timer_callback | Calculate and publish velocity commands while a move-to-pose goal is active. |

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
ros2 launch eval8_pose_controller_py_pkg pose_controller_node.launch.py
ros2 lifecycle set /pose_controller_node configure
ros2 lifecycle set /pose_controller_node activate
```
