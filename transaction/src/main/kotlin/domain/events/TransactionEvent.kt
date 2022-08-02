package domain.events

import domain.models.TransactionId

sealed interface TransactionEvent : Event {

    val transactionId: TransactionId
}
