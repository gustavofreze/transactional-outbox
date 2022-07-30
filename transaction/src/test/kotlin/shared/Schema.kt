package shared

import io.confluent.kafka.schemaregistry.avro.AvroSchema

object Schema {

    fun load(name: String): AvroSchema {
        val schemaResource = this::class.java.getResourceAsStream("/schemas/$name.avsc")
            ?: error("Schema not found: /schemas/$name.avsc")
        val schemaString = schemaResource.bufferedReader().use { it.readText() }

        return AvroSchema(schemaString)
    }
}
