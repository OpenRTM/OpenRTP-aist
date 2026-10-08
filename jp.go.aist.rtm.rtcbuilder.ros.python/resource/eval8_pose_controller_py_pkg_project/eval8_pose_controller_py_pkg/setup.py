from setuptools import find_packages, setup
import os
from glob import glob

package_name = 'eval8_pose_controller_py_pkg'

setup(
    name=package_name,
    version='0.0.1',
    packages=find_packages(exclude=['test']),
    data_files=[
        ('share/ament_index/resource_index/packages', ['resource/' + package_name]),
        ('share/' + package_name, ['package.xml', 'LICENSE', 'README.md']),
        (os.path.join('share', package_name, 'launch'), glob('launch/*.py')),
        (os.path.join('share', package_name, 'config'), glob('config/*.yaml')),
    ],
    install_requires=['setuptools'],
    zip_safe=True,
    maintainer='rsdlab',
    maintainer_email='todo@example.com',
    description='Lifecycle pose controller that drives a simulated mobile robot to a target pose using odometry feedback.',
    license='BSD-3-Clause',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'pose_controller_node = eval8_pose_controller_py_pkg.pose_controller_node:main',
        ],
    },
)
