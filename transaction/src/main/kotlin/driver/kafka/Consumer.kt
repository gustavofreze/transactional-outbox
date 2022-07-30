package driver.kafka

import org.apache.avro.generic.GenericRecord

interface Consumer<T : Event> {

    val topic: String

    fun accept(record: GenericRecord)
}
