package driven.transaction.outbox

import domain.events.TransactionEvent
import domain.events.TransactionRequested
import driven.outbox.Event
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID
import java.util.UUID.randomUUID

abstract class TransactionEvent(event: TransactionEvent) : Event {

    final override val id: UUID = randomUUID()
    final override val occurredOn: LocalDateTime = now()
    final override val aggregateId: String = event.transactionId.value.toString()
    final override val aggregateType: String = "Transaction"

    companion object {

        fun outboxEventOf(event: TransactionEvent): Event {
            return when (event) {
                is TransactionRequested -> TransactionRequested(event = event)
            }
        }
    }
}
