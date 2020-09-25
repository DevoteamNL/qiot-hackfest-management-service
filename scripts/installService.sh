#!/bin/bash
podman run -d -p 8081:8081 --network=qiot --privileged --name=air-quality-management bentaljaard/air-quality-management:aarch64-latest
cp *.service /etc/systemd/system
systemctl enable air-quality-management.service
systemctl start air-quality-management
#systemctl status air-quality-edge
