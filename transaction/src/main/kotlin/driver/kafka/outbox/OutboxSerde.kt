package driver.kafka.outbox

import io.confluent.kafka.serializers.KafkaJsonDeserializer
import io.confluent.kafka.serializers.KafkaJsonSerializer
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

class OutboxSerde {

    companion object {

        fun build(): Serde<Outbox> {
            val properties = hashMapOf(JSON_VALUE_TYPE to Outbox::class.java)

            val serializer = KafkaJsonSerializer<Outbox>()
            serializer.configure(properties, false)

            val deserializer = KafkaJsonDeserializer<Outbox>()
            deserializer.configure(properties, false)

            return Serdes.serdeFrom(serializer, deserializer)
        }
    }
}
