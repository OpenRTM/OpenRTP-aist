from launch import LaunchDescription
from launch_ros.actions import LifecycleNode
import os
from ament_index_python.packages import get_package_share_directory


def generate_launch_description():
    param_file = os.path.join(
        get_package_share_directory('count_client_pkg'),
        'config',
        'params.yaml'
    )

    node = LifecycleNode(
        package='count_client_pkg',
        executable='count_client_node',
        name='count_client_node',
        namespace='',
        output='screen',
        parameters=[param_file]
    )

    return LaunchDescription([node])
