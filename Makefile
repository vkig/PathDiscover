DOCKER_USERNAME=vkig
APPLICATION_NAME=pathdiscoverer
GRADLE_OPTS=-Dorg.gradle.daemon=false -Dorg.gradle.workers.max=2
HOST_LOCAL_IP=$(shell hostname -I)

build:
	podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}db --build-arg POSTGRES_PASSWORD=password -f Dockerfile.database .
	echo "database image has been built"
	podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}1 --build-arg POSTGRES_PASSWORD=password --build-arg PORT=8081 --build-arg USERNAME=app1 --build-arg HOST_LOCAL_IP=${HOST_LOCAL_IP} -f Dockerfile.application .
	echo "application 1 has been built"
	podman build --tag ${DOCKER_USERNAME}:${APPLICATION_NAME}2 --build-arg POSTGRES_PASSWORD=password --build-arg PORT=8082 --build-arg USERNAME=app2 --build-arg HOST_LOCAL_IP=${HOST_LOCAL_IP} -f Dockerfile.application .
	echo "application 2 has been built"
run: build
	podman run -d -p 5432:5432 --name ${APPLICATION_NAME}db ${DOCKER_USERNAME}:${APPLICATION_NAME}db
	podman run -d -p 8081:8081 --user app1 --name ${APPLICATION_NAME}1 ${DOCKER_USERNAME}:${APPLICATION_NAME}1
	podman run -d -p 8082:8082 --user app2 --name ${APPLICATION_NAME}2 ${DOCKER_USERNAME}:${APPLICATION_NAME}2
