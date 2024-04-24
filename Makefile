DOCKER_USERNAME=vkig
APPLICATION_NAME=pathdiscoverer
GRADLE_OPTS=-Dorg.gradle.daemon=false -Dorg.gradle.workers.max=2
PATH_DISCOVERER_LOCAL_IP=$(shell hostname -I | awk '{print $$1}')
SHELL := /bin/bash
# podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}1 --build-arg POSTGRES_PASSWORD=password --build-arg PORT=8081 --build-arg USERNAME=app1 --build-arg PATH_DISCOVERER_LOCAL_IP=${PATH_DISCOVERER_LOCAL_IP} -f Dockerfile.application .
# podman run -it --rm --entrypoint sh -p 8081:8081 --systemd always -e POSTGRES_PASSWORD=password -e PORT=8081 -e USERNAME=app1 vkig:pathdiscovererall

build:
	podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}db --build-arg POSTGRES_PASSWORD=password -f Dockerfile.database .
	echo "database image has been built"
	podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}all -f Dockerfile.application .
	echo "application has been built"
run:
	podman run -d -p 5432:5432 --systemd always --name ${APPLICATION_NAME}db ${DOCKER_USERNAME}:${APPLICATION_NAME}db
	python healthcheck.py
	podman run --rm -d -p 8081:8081 --systemd always -e POSTGRES_PASSWORD=password -e PORT=8081 -e USERNAME=app1 -e  PATH_DISCOVERER_LOCAL_IP=${PATH_DISCOVERER_LOCAL_IP} --name ${APPLICATION_NAME}1 ${DOCKER_USERNAME}:${APPLICATION_NAME}all
	podman run --rm -d -p 8082:8082 --systemd always -e POSTGRES_PASSWORD=password -e PORT=8082 -e USERNAME=app1 -e  PATH_DISCOVERER_LOCAL_IP=${PATH_DISCOVERER_LOCAL_IP} --name ${APPLICATION_NAME}2 ${DOCKER_USERNAME}:${APPLICATION_NAME}all
