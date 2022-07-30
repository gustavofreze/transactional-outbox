package driver.kafka

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG
import org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG
import starter.Environment.get
import java.util.Properties
import javax.inject.Singleton

@Singleton
data class Settings(
    val applicationId: String = get("APPLICATION_ID"),
    val bootstrapServers: String = get("BOOTSTRAP_SERVERS"),
    val schemaRegistryUrl: String = get("SCHEMA_REGISTRY_URL")
) {

    fun toMap(): Map<String, Any> {
        val map: MutableMap<String, Any> = mutableMapOf()
        toProperties().forEach { map[it.key.toString()] = it.value }

        return map
    }

    fun toProperties(): Properties {
        val properties = Properties()
        properties[APPLICATION_ID_CONFIG] = applicationId
        properties[BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        properties[SCHEMA_REGISTRY_URL_CONFIG] = schemaRegistryUrl

        return properties
    }
}
