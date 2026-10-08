from setuptools import find_packages, setup
import os
from glob import glob

package_name = 'eval7_extra_dependency_py_pkg'

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
    description='Python sample node that demonstrates explicit cv_bridge extra dependency without generated cv_bridge API code.',
    license='Apache-2.0',
    tests_require=['pytest'],
    entry_points={
        'console_scripts': [
            'eval7_extra_dependency_node = eval7_extra_dependency_py_pkg.eval7_extra_dependency_node:main',
        ],
    },
)
