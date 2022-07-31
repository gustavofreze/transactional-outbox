package driver.kafka.checkout.factories.events

import com.fasterxml.jackson.annotation.JsonProperty
import driver.kafka.checkout.factories.dtos.Amount
import driver.kafka.checkout.factories.dtos.Country

data class CheckoutDone(
    @JsonProperty("id") val id: String,
    @JsonProperty("amount") val amount: Amount,
    @JsonProperty("country") val country: Country
) : CheckoutEvent
