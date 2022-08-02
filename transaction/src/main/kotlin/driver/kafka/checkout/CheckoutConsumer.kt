package driver.kafka.checkout

import driver.kafka.Consumer
import driver.kafka.EventBus
import driver.kafka.checkout.factories.EventFactory
import driver.kafka.checkout.factories.events.CheckoutEvent
import driver.kafka.outbox.Outbox
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CheckoutConsumer(
    private val eventBus: EventBus<CheckoutEvent>,
    private val eventFactory: EventFactory
) : Consumer<CheckoutEvent> {

    override val topic: String = "payin.checkout"

    override fun accept(record: Outbox) {
        val event = eventFactory.map(record = record)

        eventBus.dispatch(event = event)
    }
}
