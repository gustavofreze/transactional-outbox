package driven.outbox

import driven.outbox.Queries.INSERT
import org.jdbi.v3.core.Handle
import java.time.LocalDateTime
import java.util.UUID

interface Event {

    val id: UUID
    val payload: String
    val revision: Int
    val eventType: String
    val occurredOn: LocalDateTime
    val aggregateId: String
    val aggregateType: String

    fun insertUsing(handle: Handle) {
        handle
            .createUpdate(INSERT)
            .bind("id", id.toString())
            .bind("payload", payload)
            .bind("revision", revision)
            .bind("eventType", eventType)
            .bind("occurredOn", occurredOn.toString())
            .bind("aggregateId", aggregateId)
            .bind("aggregateType", aggregateType)
            .execute()
    }
}
