# ==============================================================================
# OpenRTM Dockerfile
# ==============================================================================

# 1. Base Image
FROM docker.io/openrtm/devel-rtm:ubuntu24.04

# 2. Multi-Architecture and Metadata
ARG TARGETARCH
LABEL org.opencontainers.image.architecture="${TARGETARCH:-amd64}"
LABEL org.opencontainers.image.authors="Nobu19800"
LABEL org.opencontainers.image.description="OpenRTM 2.x C++ RTC sample that measures RTC startup time. The repository root is directly buildable with CMake."


# 3. Environment Variables
ENV DEBIAN_FRONTEND=noninteractive
ENV OPENRTM_VERSION=latest
ENV OPENRTM_NAMING_COMMAND=rtm2-naming
ENV OPENRTM_CONFIG_COMMAND=rtm2-config


# 4. Configure the official OpenRTM APT repository
RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl gnupg \
    && curl -fsSL https://openrtm.org/pub/openrtm-keyring.gpg \
       -o /usr/share/keyrings/openrtm-archive-keyring.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/openrtm-archive-keyring.gpg] https://openrtm.org/pub/Linux/ubuntu noble main" \
       > /etc/apt/sources.list.d/openrtm.list \
    && rm -rf /var/lib/apt/lists/*

# 5. Install System Dependencies
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    sudo \
    git \
    build-essential \
    cmake \
    python3-pip \
    python-is-python3 \
    openrtm2 \
    openrtm2-dev \
    openrtm2-idl \
    openrtm2-naming \
    libomniorb4-dev \
    omniidl \
    && apt-get clean \
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

# 7. Setup Workspace
RUN mkdir -p /workspace/workspace \
    && chown -R $USERNAME:$USERNAME /workspace

USER $USERNAME
WORKDIR /workspace/workspace

# 8. Clone and Build Source Code

RUN git clone -b main https://github.com/Nobu19800/RTCLoadTest.git \
    && cd RTCLoadTest \
    && git submodule update --init --recursive \
    && mkdir -p build && cd build \
    && cmake .. -DBUILD_DOCUMENTATION=OFF -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
    && make -j2

# 9. Setup Entrypoint
RUN echo '#!/bin/bash' > /workspace/entrypoint.sh && \
    echo 'set -e' >> /workspace/entrypoint.sh && \
    echo '# OpenRTM 2.x helper commands:' >> /workspace/entrypoint.sh && \
    echo '#   rtm2-naming' >> /workspace/entrypoint.sh && \
    echo '#   rtm2-config' >> /workspace/entrypoint.sh && \
    echo 'exec "$@"' >> /workspace/entrypoint.sh && \
    sudo chmod +x /workspace/entrypoint.sh

# 10. Setup Volume for Logs
RUN mkdir -p /workspace/workspace/logs \
    && sudo chown $USERNAME:$USERNAME /workspace/workspace/logs

WORKDIR /workspace/workspace
VOLUME ["/workspace/workspace/logs"]

# 11. Security: Revoke Sudo Privileges
RUN sudo rm -f /etc/sudoers.d/$USERNAME \
    && test ! -f /etc/sudoers.d/$USERNAME

# 12. Execution Command
STOPSIGNAL SIGINT
ENTRYPOINT ["/workspace/entrypoint.sh"]
CMD ["bash"]
