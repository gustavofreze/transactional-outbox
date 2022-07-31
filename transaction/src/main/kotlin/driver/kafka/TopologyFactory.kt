package driver.kafka

import driver.kafka.outbox.OutboxSerde
import org.apache.kafka.common.serialization.Serdes.String
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed.with
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces

@Dependent
class TopologyFactory {

    @Produces
    fun build(settings: Settings, consumer: Consumer<*>): Topology {
        val builder = StreamsBuilder()

        builder
            .stream(consumer.topic, with(String(), OutboxSerde.build()))
            .foreach { _, value -> consumer.accept(record = value) }

        return builder.build()
    }
}
