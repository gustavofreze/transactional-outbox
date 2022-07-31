package shared

import io.confluent.kafka.schemaregistry.json.JsonSchema

object Schema {

    fun load(name: String): JsonSchema {
        val path = "/kafka/schemas/$name.json"
        val resource = this::class.java.getResourceAsStream(path) ?: error("Schema not found: $path")
        val schemaString = resource.bufferedReader().use { it.readText() }

        return JsonSchema(schemaString)
    }
}
