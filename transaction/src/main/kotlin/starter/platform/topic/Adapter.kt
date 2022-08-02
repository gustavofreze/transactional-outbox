package starter.platform.topic

import driver.kafka.Settings
import org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.admin.AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.NewTopic
import starter.platform.topic.dtos.TopicData
import java.util.Properties
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Adapter(private val settings: Settings) : Topic {

    override fun register(topic: TopicData) {
        val kafkaAdminClient = KafkaAdminClient.create(properties())
        val newTopic = NewTopic(topic.name, topic.numPartitions, topic.replicationFactor.toShort())

        kafkaAdminClient.createTopics(listOf(newTopic))
        kafkaAdminClient.close()
    }

    private fun properties(): Properties {
        val properties = Properties()
        properties[BOOTSTRAP_SERVERS_CONFIG] = settings.bootstrapServers
        properties[REQUEST_TIMEOUT_MS_CONFIG] = 10000
        return properties
    }
}
