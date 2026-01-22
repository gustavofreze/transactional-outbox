DOCKER_COMPOSE = docker compose
DOCKER_IMAGE_PRUNE = docker image prune --all --force
DOCKER_NETWORK_PRUNE = docker network prune --force

.PHONY: configure
configure:
	@cd transaction && ./gradlew assemble
	@${DOCKER_COMPOSE} up -d --build --wait --wait-timeout 180

.PHONY: configure-status
configure-status:
	@docker ps --format "table {{.Names}}\t{{.Status}}"

.PHONY: checkout-done
checkout-done:
	@docker exec -it checkout-adm bash /scripts/use-case/execute.sh

.PHONY: stop
stop:
	@${DOCKER_COMPOSE} stop $(docker ps -a -q)

.PHONY: clean
clean: stop
	@${DOCKER_COMPOSE} rm -vf $(docker ps -a -q)
	@${DOCKER_NETWORK_PRUNE} --filter label="com.docker.compose.project"="transactional-outbox"

.PHONY: clean-all
clean-all: clean
	@${DOCKER_IMAGE_PRUNE} --filter label="io.confluent.docker=true"
	@${DOCKER_IMAGE_PRUNE} --filter label="maintainer"="Debezium Community"
	@${DOCKER_IMAGE_PRUNE} --filter label="com.docker.compose.project"="transactional-outbox"

.PHONY: change-ownership
change-ownership:
	@sudo chown -R ${USER}:${USER} ${PWD}
