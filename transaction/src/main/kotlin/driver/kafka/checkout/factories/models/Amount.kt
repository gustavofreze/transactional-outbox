package driver.kafka.checkout.factories.models

import java.math.BigDecimal
import java.util.Currency

data class Amount(val value: BigDecimal, val currency: Currency)
