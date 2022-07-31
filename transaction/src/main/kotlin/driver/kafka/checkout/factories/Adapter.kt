package driver.kafka.checkout.factories

import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.events.CheckoutEvent
import driver.kafka.mapper.EventMapper
import driver.kafka.outbox.Outbox
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Adapter(private val eventMapper: EventMapper) : EventFactory {

    override fun map(record: Outbox): CheckoutEvent {
        val payload = record.payload

        return when (val name = record.eventType) {
            CheckoutDone::class.java.simpleName -> eventMapper.toEvent(json = payload, type = CheckoutDone::class)
            else -> error("Event <$name> cannot be mapped.")
        }
    }
}
