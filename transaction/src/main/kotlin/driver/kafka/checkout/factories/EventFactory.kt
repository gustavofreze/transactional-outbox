package driver.kafka.checkout.factories

import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.events.CheckoutEvent
import org.apache.avro.generic.GenericRecord

interface EventFactory {

    fun build(): CheckoutEvent

    fun GenericRecord.getString(field: String): String = get(field).toString()

    fun GenericRecord.getRecord(field: String): GenericRecord = get(field) as GenericRecord

    companion object {

        fun map(record: GenericRecord): EventFactory {
            return when (val name = record.schema.name) {
                CheckoutDone::class.simpleName -> CheckoutDone(record = record)
                else -> error("Event <$name> cannot be mapped.")
            }
        }
    }
}
