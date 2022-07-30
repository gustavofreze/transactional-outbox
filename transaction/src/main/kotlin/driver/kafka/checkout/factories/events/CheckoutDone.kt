package driver.kafka.checkout.factories.events

import driver.kafka.checkout.factories.models.Amount
import driver.kafka.checkout.factories.models.CheckoutId
import driver.kafka.checkout.factories.models.Country

data class CheckoutDone(val id: CheckoutId, val amount: Amount, val country: Country) : CheckoutEvent
