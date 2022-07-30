package domain.handlers

import domain.handlers.usecases.RequestTransaction
import domain.models.Country
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import shared.commonvalues.Amounts.randomAmount
import shared.commonvalues.Countries.randomAlphaCode
import shared.mocks.TransactionsMock
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import domain.handlers.commands.RequestTransaction as Command

@Tag("UnitTest")
class RequestTransactionTest {

    private lateinit var handler: RequestTransaction
    private lateinit var transactions: TransactionsMock

    private val requestTransaction = Command(amount = randomAmount(), country = Country(alpha2 = randomAlphaCode()))

    @BeforeEach
    fun `Before each`() {
        transactions = TransactionsMock()
        handler = RequestTransaction(transactions = transactions)
    }

    @Test
    fun `Transaction requested`() {
        /** @Dado que uma transação foi solicitada */
        val command = requestTransaction

        /** @Quando o caso de uso de solicitar transação for executado */
        handler.handle(command = command)

        /** @Então o evento de transação solicitada deverá ser emitido */
        expectThat(transactions.list.first()).and {
            get { country.alpha2 } isEqualTo command.country.alpha2
            get { amount.currency } isEqualTo command.amount.currency
            get { amount.positive.value } isEqualTo command.amount.positive.value
        }
    }
}
