from launch import LaunchDescription
from launch_ros.actions import LifecycleNode
import os
from ament_index_python.packages import get_package_share_directory


def generate_launch_description():
    param_file = os.path.join(
        get_package_share_directory('eval8_pose_controller_cpp_pkg'),
        'config',
        'params.yaml'
    )

    node = LifecycleNode(
        package='eval8_pose_controller_cpp_pkg',
        executable='pose_controller_node',
        name='pose_controller_node',
        namespace='',
        output='screen',
        parameters=[param_file]
    )

    return LaunchDescription([node])
