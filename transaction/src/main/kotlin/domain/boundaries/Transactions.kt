package domain.boundaries

import domain.events.TransactionRequested

interface Transactions {

    fun save(event: TransactionRequested)
}