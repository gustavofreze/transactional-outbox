package domain.models

import domain.events.TransactionRequested
import java.time.Instant.now

data class Transaction(val id: TransactionId = TransactionId(), val amount: Amount, val country: Country) {

    companion object {

        fun request(amount: Amount, country: Country) = TransactionRequested(
            amount = amount,
            country = country,
            occurredOn = now(),
            transactionId = TransactionId()
        )
    }
}
