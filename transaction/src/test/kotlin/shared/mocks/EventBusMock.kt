package shared.mocks

import driver.kafka.EventBus
import driver.kafka.checkout.factories.events.CheckoutEvent
import io.quarkus.test.Mock
import javax.inject.Singleton

@Mock
@Singleton
class EventBusMock : EventBus<CheckoutEvent> {

    val events = mutableListOf<CheckoutEvent>()

    override fun dispatch(event: CheckoutEvent) {
        events.add(event)
    }
}
