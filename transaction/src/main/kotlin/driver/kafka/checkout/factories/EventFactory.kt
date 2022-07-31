package driver.kafka.checkout.factories

import driver.kafka.checkout.factories.events.CheckoutEvent
import driver.kafka.outbox.Outbox

interface EventFactory {

    fun map(record: Outbox): CheckoutEvent
}
