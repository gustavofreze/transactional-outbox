package driver.kafka

import driver.kafka.checkout.CheckoutEventBus
import driver.kafka.checkout.factories.dtos.Amount
import driver.kafka.checkout.factories.dtos.Country
import driver.kafka.checkout.factories.events.CheckoutDone
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import shared.commonvalues.Amounts.randomValue
import shared.commonvalues.Countries.randomAlphaCode
import shared.commonvalues.Currencies.randomCurrency
import shared.mocks.InstanceMock
import java.util.UUID.randomUUID

@Tag("UnitTest")
class EventBusTest {

    private val event = CheckoutDone(
        id = randomUUID().toString(),
        amount = Amount(value = randomValue(), currency = randomCurrency()),
        country = Country(alpha2 = randomAlphaCode())
    )

    @Test
    fun `Dispatch to one interested policy`() {
        val policy: Policy<CheckoutDone> = mockk(relaxed = true)
        val eventBus = CheckoutEventBus(policies = InstanceMock(mutableListOf(policy)))

        /** @Dado que exista uma política interessada no evento */
        every { policy.subscribedEvent } returns CheckoutDone::class

        /** @Quando o evento é despachado para o barramento */
        eventBus.dispatch(event)

        /** @Então a política é acionada com evento lançado */
        coVerify(exactly = 1) { policy.handle(event) }
    }

    @Test
    fun `Dispatch to several interested policies`() {
        val one: Policy<CheckoutDone> = mockk(relaxed = true)
        val two: Policy<CheckoutDone> = mockk(relaxed = true)
        val eventBus = CheckoutEventBus(policies = InstanceMock(mutableListOf(one, two)))

        /** @Dado que existam várias políticas interessadas em um mesmo evento */
        every { one.subscribedEvent } returns CheckoutDone::class
        every { two.subscribedEvent } returns CheckoutDone::class

        /** @Quando o evento é despachado para o barramento */
        eventBus.dispatch(event)

        /** @Então cada uma das políticas é acionada com evento lançado */
        coVerify(exactly = 1) { one.handle(event) }
        coVerify(exactly = 1) { two.handle(event) }
    }

    @Test
    fun `No exceptions when no interested policies`() {
        /** @Dado que não há políticas interessadas no evento */
        val eventBus = CheckoutEventBus(policies = InstanceMock(list = mutableListOf()))

        /** @Então nenhuma exceção deve ser lançada */
        assertDoesNotThrow {

            /** @Quando o evento é despachado para o barramento */
            eventBus.dispatch(event = event)
        }
    }
}
