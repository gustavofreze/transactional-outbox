package driver.kafka.checkout

import driver.kafka.EventBus
import driver.kafka.Policy
import driver.kafka.checkout.factories.events.CheckoutEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance

@ApplicationScoped
class CheckoutEventBus(private val policies: Instance<Policy<*>>) : EventBus<CheckoutEvent> {

    override fun dispatch(event: CheckoutEvent) {
        policiesOf(event = event).forEach { policy -> policy.handle(event = event) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : CheckoutEvent> policiesOf(event: T): List<Policy<T>> {
        return policies
            .filter { policy -> policy.subscribedEvent.isInstance(event) }
            .map { it as Policy<T> }
    }
}
