# ==============================================================================
# ROS Dockerfile
# ==============================================================================

# 1. Base Image
FROM docker.io/osrf/ros:jazzy-desktop-full

# 2. Multi-Architecture and Metadata
ARG TARGETARCH
LABEL org.opencontainers.image.architecture="${TARGETARCH:-amd64}"
# Metadata
LABEL org.opencontainers.image.authors="Shota Aoki, Atsushi Kuwagata, Yusuke Kato"
LABEL org.opencontainers.image.description="CRANE+ V2ロボット用のROS 2パッケージスイート。制御、記述、シミュレーション、およびMoveIt!設定ファイルが含まれています。"


# 3. Environment Variables
ENV DEBIAN_FRONTEND=noninteractive
ENV ROS_DISTRO=jazzy

# 4. Install System Dependencies
RUN apt-get update && apt-get install -y \
    sudo \
    git \
    build-essential \
    cmake \
    python3-pip \
    python3-colcon-common-extensions \
    ros-jazzy-rclcpp \
    ros-jazzy-rclpy \
    ros-jazzy-rclcpp-lifecycle \
    ros-jazzy-lifecycle-msgs \
    ros-jazzy-std-msgs \
    ros-jazzy-launch \
    ros-jazzy-launch-ros \
    ros-jazzy-rosidl-default-generators \
    ros-jazzy-rosidl-default-runtime \
    ros-jazzy-pluginlib \
    ros-jazzy-controller-manager \
    ros-jazzy-hardware-interface \
    ros-jazzy-ros2-control \
    ros-jazzy-ros2-controllers \
    ros-jazzy-ros2controlcli \
    ros-jazzy-gz-ros2-control \
    ros-jazzy-dynamixel-sdk \
    ros-jazzy-gripper-controllers \
    ros-jazzy-ros-gz \
    ros-jazzy-ros-gz-bridge \
    ros-jazzy-ros-gz-sim \
    ros-jazzy-moveit-kinematics \
    ros-jazzy-moveit-planners \
    ros-jazzy-moveit-simple-controller-manager \
    ros-jazzy-moveit-configs-utils \
    ros-jazzy-moveit-ros-move-group \
    ros-jazzy-moveit-ros-visualization \
    ros-jazzy-moveit-ros-warehouse \
    ros-jazzy-moveit-setup-assistant \
    ros-jazzy-moveit-ros-planning-interface \
    ros-jazzy-moveit-core \
    ros-jazzy-tf2 \
    ros-jazzy-tf2-ros \
    ros-jazzy-tf2-geometry-msgs \
    ros-jazzy-geometry-msgs \
    ros-jazzy-joint-state-publisher \
    ros-jazzy-joint-state-publisher-gui \
    ros-jazzy-robot-state-publisher \
    ros-jazzy-xacro \
    ros-jazzy-rviz2 \
    ros-jazzy-rviz-common \
    ros-jazzy-rviz-default-plugins \
    libopencv-dev \
    ros-jazzy-cv-bridge \
    ros-jazzy-image-geometry \
    ros-jazzy-usb-cam \
    ros-jazzy-vision-opencv \
    && rm -rf /var/lib/apt/lists/*

# 6. Create Non-root User
ARG USERNAME=container_user
ARG USER_UID=1000
ARG USER_GID=$USER_UID
ARG USER_PASSWORD=container_user

RUN (userdel -r ubuntu || true) \
    && groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m -s /bin/bash $USERNAME \
    && echo "$USERNAME:$USER_PASSWORD" | chpasswd \
    && usermod -aG sudo $USERNAME \
    && echo "$USERNAME ALL=(root) NOPASSWD:ALL" > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

USER $USERNAME

RUN echo "source /opt/ros/${ROS_DISTRO}/setup.bash" >> /home/$USERNAME/.bashrc && \
    echo "if [ -f /workspace/colcon_ws/install/setup.bash ]; then source /workspace/colcon_ws/install/setup.bash; fi" >> /home/$USERNAME/.bashrc

# 7. Setup Workspace
RUN sudo mkdir -p /workspace/colcon_ws/src \
    && sudo chown -R $USERNAME:$USERNAME /workspace
WORKDIR /workspace/colcon_ws/src

# 8. Clone and Build Source Code
RUN git clone -b jazzy https://github.com/rt-net/crane_plus.git
RUN git clone -b main https://github.com/ROBOTIS-GIT/DynamixelSDK.git

WORKDIR /workspace/colcon_ws
RUN sudo apt-get update && rosdep update && rosdep install -r -y -i --from-paths src
RUN bash -c "source /opt/ros/${ROS_DISTRO}/setup.bash && MAKEFLAGS='-j2' colcon build --symlink-install --executor sequential" 

# 9. Setup Entrypoint
RUN echo '#!/bin/bash' > /workspace/entrypoint.sh && \
    echo 'set -e' >> /workspace/entrypoint.sh && \
    echo 'source /opt/ros/${ROS_DISTRO}/setup.bash' >> /workspace/entrypoint.sh && \
    echo 'if [ -f /workspace/colcon_ws/install/setup.bash ]; then source /workspace/colcon_ws/install/setup.bash; fi' >> /workspace/entrypoint.sh && \
    echo 'exec "$@"' >> /workspace/entrypoint.sh && \
    sudo chmod +x /workspace/entrypoint.sh

WORKDIR /workspace/colcon_ws

# 10. Setup Volume for Logs
RUN mkdir -p /home/$USERNAME/.ros/log \
    && sudo chown -R $USERNAME:$USERNAME /home/$USERNAME/.ros
VOLUME ["/home/$USERNAME/.ros/log"]

# 11. Security: Revoke Sudo Privileges
RUN sudo rm -f /etc/sudoers.d/$USERNAME \
    && test ! -f /etc/sudoers.d/$USERNAME

# 12. Execution Command
STOPSIGNAL SIGINT
ENTRYPOINT ["/workspace/entrypoint.sh"]
CMD ["bash"]
