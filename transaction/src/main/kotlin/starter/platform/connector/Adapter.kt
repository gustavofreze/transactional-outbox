package starter.platform.connector

import com.fasterxml.jackson.databind.JsonNode
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import starter.Environment.get
import starter.platform.connector.dtos.ConnectorData
import java.nio.file.Files
import java.nio.file.Paths
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Adapter : Connector {

    private val url = get("KAFKA_CONNECT_URL")
    private val path = get("CONNECTORS_PATH")
    private val logger: Logger = LoggerFactory.getLogger(Adapter::class.java)

    override suspend fun register() {
        waitingForConnection()

        val connectors = listFromBroker()

        listFromClasspath()
            .filter { !connectors.contains(it.name) }
            .forEach {
                val (_, _, result) = Fuel
                    .post(path = "$url/connectors")
                    .body(it.data)
                    .appendHeader("Content-Type", "application/json")
                    .responseString()

                when (result) {
                    is Result.Success -> {
                        val template = "The connector <%s> has been registered."
                        logger.info(template.format(it.name))
                    }

                    is Result.Failure -> {
                        val template = "The connector <%s> has not been registered. Reason: %s."
                        logger.error(template.format(it.name, result.error.message))
                    }
                }
            }
    }

    private suspend fun waitingForConnection() {
        while (true) {
            val (_, _, result) = Fuel
                .get(path = url)
                .appendHeader("Content-Type", "application/json")
                .responseString()

            when (result) {
                is Result.Success -> break
                is Result.Failure -> {
                    logger.info("Waiting connection with kafka connect.")
                    delay(3000L)
                }
            }
        }
    }

    private fun listFromBroker(): List<String> {
        val (_, _, result) = Fuel
            .get(path = "$url/connectors", parameters = listOf("expand" to "status"))
            .responseObject<JsonNode>()

        return when (result) {
            is Result.Failure -> emptyList()
            is Result.Success ->
                result
                    .value
                    .fields()
                    .asSequence()
                    .map { it.value["status"] }
                    .map { it["name"].asText() }
                    .toList()
        }
    }

    private fun listFromClasspath(): List<ConnectorData> {
        return Files
            .walk(Paths.get(path))
            .filter { Files.isRegularFile(it) }
            .map { it.toFile() }
            .filter { it.name.endsWith(".json") }
            .map { ConnectorData(name = it.name.removeSuffix(".json"), data = it.readText()) }
            .toList()
    }
}
