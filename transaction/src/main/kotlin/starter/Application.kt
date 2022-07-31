package starter

import driver.kafka.Settings
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.Startup
import kotlinx.coroutines.runBlocking
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.Topology
import starter.platform.connector.Connector
import starter.platform.schema.Schema
import starter.platform.topic.Topic
import starter.platform.topic.dtos.TopicData
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Singleton
class Application(
    private val topic: Topic,
    private val schema: Schema,
    private val topology: Topology,
    private val settings: Settings,
    private val connector: Connector
) {

    private lateinit var streams: KafkaStreams

    @Startup
    @Produces
    @Singleton
    fun onStart(): KafkaStreams {
        if (::streams.isInitialized) return streams

        streams = KafkaStreams(topology, settings.toProperties())
        streams.cleanUp()

        runBlocking {
            topic.register(topic = TopicData(name = "payin.checkout"))
            schema.register()
            connector.register()
            streams.start()
        }

        return streams
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStop(@Observes event: ShutdownEvent) {
        if (!::streams.isInitialized) return
        streams.close()
    }
}
