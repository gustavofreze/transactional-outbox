package driven.transaction.repository

import domain.boundaries.Transactions
import domain.events.TransactionRequested
import driven.transaction.outbox.TransactionEvent.Companion.outboxEventOf
import driven.transaction.repository.Queries.INSERT
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Adapter(private val jdbi: Jdbi) : Transactions {

    override fun save(event: TransactionRequested): Unit = jdbi.inTransactionUnchecked { handle ->
        handle
            .createUpdate(INSERT)
            .bind("id", event.transactionId.value.toString())
            .bind("value", event.amount.positive.value)
            .bind("currency", event.amount.currency.toString())
            .bind("countryIso2", event.country.iso2)
            .execute()

        outboxEventOf(event = event).also { it.insertUsing(handle = handle) }
    }
}