package domain.events

import domain.models.Amount
import domain.models.Country
import domain.models.TransactionId
import java.time.Instant

data class TransactionRequested(
    val amount: Amount,
    val country: Country,
    val occurredOn: Instant,
    override val transactionId: TransactionId
) : TransactionEvent