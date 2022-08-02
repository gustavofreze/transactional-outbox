package domain.events

import domain.models.Amount
import domain.models.Country
import domain.models.TransactionId
import java.time.LocalDateTime

data class TransactionRequested(
    val amount: Amount,
    val country: Country,
    val occurredOn: LocalDateTime,
    override val transactionId: TransactionId
) : TransactionEvent
