package driven.outbox

object Queries {

    val INSERT = """
       INSERT INTO outbox_event (id, aggregate_type, aggregate_id, event_type, revision, payload, occurred_on)
       VALUES (UUID_TO_BIN(:id), :aggregateType, :aggregateId, :eventType, :revision, :payload, :occurredOn);
    """.trimIndent()
}
