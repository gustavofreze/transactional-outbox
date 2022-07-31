package driver.kafka.checkout.factories.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.Currency

data class Amount(@JsonProperty("value") val value: BigDecimal, @JsonProperty("currency") val currency: Currency)
