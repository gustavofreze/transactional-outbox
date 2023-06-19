DOCKER_COMPOSE = docker-compose
DOCKER_IMAGE_PRUNE = docker image prune --all --force
DOCKER_NETWORK_PRUNE = docker network prune --force

.PHONY: configure configure-status checkout-done stop clean clean-all change-ownership

configure:
	@${DOCKER_COMPOSE} up -d --build

configure-status:
	@docker ps --format "table {{.Names}}\t{{.Status}}"

checkout-done:
	@docker exec -it checkout-adm bash /scripts/use-case/execute.sh

stop:
	@${DOCKER_COMPOSE} stop $(docker ps -a -q)

clean: stop
	@${DOCKER_COMPOSE} rm -vf $(docker ps -a -q)
	@${DOCKER_NETWORK_PRUNE} --filter label="com.docker.compose.project"="transactional-outbox"

clean-all: clean
	@${DOCKER_IMAGE_PRUNE} --filter label="io.confluent.docker=true"
	@${DOCKER_IMAGE_PRUNE} --filter label="maintainer"="Debezium Community"
	@${DOCKER_IMAGE_PRUNE} --filter label="com.docker.compose.project"="transactional-outbox"

change-ownership:
	@sudo chown -R ${USER}:${USER} ${PWD}
