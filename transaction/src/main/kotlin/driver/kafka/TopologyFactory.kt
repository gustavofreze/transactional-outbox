package driver.kafka

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
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
        val valueSerde = GenericAvroSerde().also { it.configure(settings.toMap(), false) }

        builder
            .stream(consumer.topic, with(String(), valueSerde))
            .foreach { _, value -> consumer.accept(record = value) }

        return builder.build()
    }
}
