# eval3_service_client_py_pkg_interfaces

This package is reserved for custom `.msg`, `.srv`, and `.action` definitions.

Place interface definition files under `msg/`, `srv/`, or `action/`.

If this package is empty, treat it as a lightweight extension point for future
custom interfaces.

When adding custom interfaces later, enable the rosidl settings in
`CMakeLists.txt` and `package.xml`, then add a dependency from the node package
to this interface package.

For ROS 2 distribution workflows, prefer bloom for release packages. Before
using bloom, confirm that `build_depend` and `exec_depend` in `package.xml`
match the packages used by the interface definitions.
