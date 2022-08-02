package starter.platform.schema

import driver.kafka.Settings
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import io.confluent.kafka.schemaregistry.json.JsonSchema
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import starter.Environment.get
import starter.platform.schema.dtos.Subject
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Adapter(settings: Settings) : Schema {

    private val path = get("SCHEMAS_PATH")
    private val client: SchemaRegistryClient = CachedSchemaRegistryClient(settings.schemaRegistryUrl, 100)
    private val logger: Logger = LoggerFactory.getLogger(Adapter::class.java)

    override fun register() {
        subjects()
            .forEach { subject ->
                val schema = JsonSchema(subject.schema)
                val id = client.register(subject.name, schema)

                logger.info(
                    mapOf("id" to id, "schema" to schema.canonicalString(), "subject" to subject.name).toString()
                )
            }
    }

    private fun subjects(): Stream<Subject> = Files
        .walk(Paths.get(path))
        .filter { Files.isRegularFile(it) }
        .map { it.toFile() }
        .filter { it.name.endsWith(".json") }
        .map { Subject(name = it.name.removeSuffix(".json"), schema = it.readText()) }
}
