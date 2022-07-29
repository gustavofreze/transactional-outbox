package domain.events

import domain.models.TransactionId

sealed interface TransactionEvent {

    val transactionId: TransactionId
}