package driver.kafka.checkout.factories.models

import java.util.UUID
import java.util.UUID.randomUUID

data class CheckoutId(val value: UUID = randomUUID())
