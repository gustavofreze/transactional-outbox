package shared.mocks

import domain.boundaries.Transactions
import domain.events.TransactionRequested
import io.quarkus.test.Mock
import javax.inject.Singleton

@Mock
@Singleton
class TransactionsMock : Transactions {

    val list = mutableListOf<TransactionRequested>()

    override fun save(event: TransactionRequested) {
        list.add(event)
    }
}
