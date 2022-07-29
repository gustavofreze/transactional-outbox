package shared.mocks

import domain.boundaries.Transactions
import domain.events.TransactionEvent
import domain.events.TransactionRequested

class TransactionsMock : Transactions {

    val list = mutableListOf<TransactionRequested>()

    override fun save(event: TransactionRequested) {
        list.add(event)
    }
}