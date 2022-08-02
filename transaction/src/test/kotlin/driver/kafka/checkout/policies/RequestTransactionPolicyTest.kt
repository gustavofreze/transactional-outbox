package driver.kafka.checkout.policies

import driver.kafka.checkout.factories.dtos.Amount
import driver.kafka.checkout.factories.dtos.Country
import driver.kafka.checkout.factories.events.CheckoutDone
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import shared.commonvalues.Amounts.randomValue
import shared.commonvalues.Countries.randomAlphaCode
import shared.commonvalues.Currencies.randomCurrency
import shared.mocks.CommandBusMock
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import java.util.UUID.randomUUID

class RequestTransactionPolicyTest {

    private lateinit var policy: RequestTransactionPolicy
    private lateinit var commandBus: CommandBusMock

    @BeforeEach
    fun `Before Each`() {
        commandBus = CommandBusMock()
        policy = RequestTransactionPolicy(commandBus = commandBus)
    }

    @Test
    fun `Subscribed event`() {
        /** @Dado uma instância da política RequestTransactionPolicy */
        val policy = this.policy

        /** @Quando solicitado o evento interessado pela política */
        val subscribedEvent = policy.subscribedEvent

        /** @Então o evento CheckoutDone deve ser retornado */
        expectThat(CheckoutDone::class).isEqualTo(subscribedEvent)
    }

    @Test
    fun `Command dispatch`() {
        /** @Dado o evento CheckoutDone */
        val event = CheckoutDone(
            id = randomUUID().toString(),
            amount = Amount(value = randomValue(), currency = randomCurrency()),
            country = Country(alpha2 = randomAlphaCode())
        )

        /** @Quando a política é acionada */
        policy.handle(event = event)

        /** @Então um único comando deve ser enviado para o barramento de comandos */
        expectThat(commandBus.commands).hasSize(1)
    }
}
