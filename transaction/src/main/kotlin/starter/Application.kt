package starter

import driver.kafka.Settings
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.Startup
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.Topology
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@Singleton
class Application(private val settings: Settings, private val topology: Topology) {

    private lateinit var streams: KafkaStreams

    @Startup
    @Produces
    fun onStart(): KafkaStreams {
        if (::streams.isInitialized) return streams

        streams = KafkaStreams(topology, settings.toProperties())
        streams.start()

        return streams
    }

    @Suppress("UNUSED_PARAMETER")
    fun onStop(@Observes event: ShutdownEvent) {
        if (!::streams.isInitialized) return
        streams.close()
    }
}
