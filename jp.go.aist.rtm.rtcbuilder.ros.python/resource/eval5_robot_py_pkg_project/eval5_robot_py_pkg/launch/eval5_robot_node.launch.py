from launch import LaunchDescription
from launch_ros.actions import LifecycleNode
import os
from ament_index_python.packages import get_package_share_directory


def generate_launch_description():
    param_file = os.path.join(
        get_package_share_directory('eval5_robot_py_pkg'),
        'config',
        'params.yaml'
    )

    node = LifecycleNode(
        package='eval5_robot_py_pkg',
        executable='eval5_robot_node',
        name='eval5_robot_node',
        namespace='',
        output='screen',
        parameters=[param_file]
    )

    return LaunchDescription([node])
