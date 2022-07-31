package driver.kafka.outbox

import com.fasterxml.jackson.annotation.JsonProperty

data class Outbox(
    @JsonProperty("id") val id: String,
    @JsonProperty("payload") val payload: String,
    @JsonProperty("revision") val revision: Int,
    @JsonProperty("sequence") val sequence: Int,
    @JsonProperty("eventType") val eventType: String,
    @JsonProperty("occurredOn") val occurredOn: String,
    @JsonProperty("aggregateId") val aggregateId: String,
    @JsonProperty("aggregateType") val aggregateType: String
)
