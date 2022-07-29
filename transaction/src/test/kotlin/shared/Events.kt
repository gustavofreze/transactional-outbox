package shared

import domain.events.TransactionRequested
import domain.models.TransactionId
import shared.commonvalues.Amounts.randomAmount
import shared.commonvalues.Countries.randomCountry
import java.time.Instant

object Events {

    val TRANSACTION_REQUESTED = TransactionRequested(
        amount = randomAmount(),
        country = randomCountry(),
        occurredOn = Instant.now(),
        transactionId = TransactionId()
    )
}