# eval6_image_topic_py_pkg

## Description

Python image topic sample node that demonstrates sensor_msgs Image type dependency inference.

## Package Summary

- Package: `eval6_image_topic_py_pkg`
- Node executable: `eval6_image_topic_node`
- Node class: `Eval6ImageTopicNode`
- Language: `Python`
- License: `Apache-2.0`


## Generated Files

- `package.xml`
- `LICENSE`
- `README.md`
- `launch/eval6_image_topic_node.launch.py`
- `config/params.yaml`
- `setup.py`
- `setup.cfg`
- `eval6_image_topic_py_pkg/eval6_image_topic_node.py`

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

The companion `eval6_image_topic_py_pkg_interfaces` package is generated as a sibling package.
If it is empty, it is a lightweight extension point. When adding `.msg`, `.srv`,
or `.action` files later, enable the rosidl settings in that package and make
sure the node package depends on `eval6_image_topic_py_pkg_interfaces`.

## Dependency Rules

Interface type dependencies are inferred from Topic, Service, and Action type
names. For example, `sensor_msgs/msg/Image` adds `sensor_msgs` and generates the
corresponding message include or import in the node code.

Inferred interface dependencies:
- `sensor_msgs`

Extra dependencies are user-declared packages that the generated package should
depend on for later manual implementation work. They are added to package
metadata,
but the generator does not add arbitrary library includes or API calls for them.

Extra dependencies:
- None

## Run

```bash
ros2 launch eval6_image_topic_py_pkg eval6_image_topic_node.launch.py
```

## Lifecycle Operation

This package generates a ROS 2 Lifecycle node. After launch, configure and activate it:

```bash
ros2 lifecycle get /eval6_image_topic_node
ros2 lifecycle set /eval6_image_topic_node configure
ros2 lifecycle set /eval6_image_topic_node activate
```

To stop active publishers and timers:

```bash
ros2 lifecycle set /eval6_image_topic_node deactivate
```

## Topics

| Direction | Name | Type | QoS | Callback / Variable | Summary |
| --- | --- | --- | --- | --- | --- |
| Publish | image_debug | sensor_msgs/msg/Image | Reliable, KeepLast, depth=10 | Publisher variable: image_debug_publisher | Publish debug image output. |
| Subscribe | image_raw | sensor_msgs/msg/Image | Reliable, KeepLast, depth=10 | Callback: image_raw_callback | Receive raw image input. |

Useful commands:

```bash
ros2 topic list
ros2 topic info image_debug
ros2 topic echo image_debug
ros2 topic info image_raw
# ros2 topic pub image_raw sensor_msgs/msg/Image '<yaml-message>'
```

## Services

None.

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
ros2 launch eval6_image_topic_py_pkg eval6_image_topic_node.launch.py
ros2 lifecycle set /eval6_image_topic_node configure
ros2 lifecycle set /eval6_image_topic_node activate
```
