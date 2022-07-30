package driver.kafka.checkout

import driver.kafka.Consumer
import driver.kafka.EventBus
import driver.kafka.checkout.factories.EventFactory
import driver.kafka.checkout.factories.events.CheckoutEvent
import org.apache.avro.generic.GenericRecord
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CheckoutConsumer(private val eventBus: EventBus<CheckoutEvent>) : Consumer<CheckoutEvent> {

    override val topic: String = "payin.checkout"

    override fun accept(record: GenericRecord) {
        val factory = EventFactory.map(record = record)
        val event = factory.build()

        eventBus.dispatch(event = event)
    }
}
