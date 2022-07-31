package driver.kafka.checkout.factories.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class Country(@JsonProperty("alpha2") val alpha2: String)
