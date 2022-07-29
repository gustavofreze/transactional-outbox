package domain.handlers

import domain.handlers.usecases.RequestTransaction
import shared.mocks.TransactionsMock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import shared.Commands.REQUEST_TRANSACTION
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("UnitTest")
class RequestTransactionTest {

    private lateinit var handler: RequestTransaction
    private lateinit var transactions: TransactionsMock

    @BeforeEach
    fun `Before each`() {
        transactions = TransactionsMock()
        handler = RequestTransaction(transactions = transactions)
    }

    @Test
    fun `Transaction requested`() {
        /** @Dado que uma transação foi solicitada */
        val command = REQUEST_TRANSACTION

        /** @Quando o caso de uso de solicitar transação for executado */
        handler.handle(command = command)

        /** @Então o evento de transação solicitada deverá ser emitido */
        expectThat(transactions.list.first()).and {
            get { country.iso2 } isEqualTo command.country.iso2
            get { amount.currency } isEqualTo command.amount.currency
            get { amount.positive.value } isEqualTo command.amount.positive.value
        }
    }
}