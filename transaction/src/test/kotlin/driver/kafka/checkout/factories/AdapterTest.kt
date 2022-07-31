package driver.kafka.checkout.factories

import driver.kafka.checkout.factories.dtos.Amount
import driver.kafka.checkout.factories.dtos.Country
import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.events.CheckoutEvent
import driver.kafka.outbox.Outbox
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import shared.commonvalues.Amounts.randomValue
import shared.commonvalues.Countries.randomAlphaCode
import shared.commonvalues.Currencies.randomCurrency
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.time.Instant.now
import java.util.UUID.randomUUID
import driver.kafka.checkout.factories.Adapter as EventFactory
import driver.kafka.mapper.Adapter as EventMapper

class AdapterTest {

    private lateinit var eventMapper: EventMapper
    private lateinit var eventFactory: EventFactory

    @BeforeEach
    fun `Before Each`() {
        eventMapper = EventMapper()
        eventFactory = EventFactory(eventMapper = eventMapper)
    }

    @Test
    fun `Map event`() {
        /** @Dado um evento de CheckoutDone */
        val event = CheckoutDone(
            id = randomUUID().toString(),
            amount = Amount(value = randomValue(), currency = randomCurrency()),
            country = Country(alpha2 = randomAlphaCode())
        )

        /** @E que esse evento esteja em um registro de outbox */
        val record = Outbox(
            id = randomUUID().toString(),
            payload = eventMapper.toJson(event = event),
            revision = 1,
            sequence = 1,
            eventType = event::class.java.simpleName,
            occurredOn = now().toString(),
            aggregateId = event.id,
            aggregateType = "Checkout"
        )

        /** @Quando o método map for executado */
        val actual = eventFactory.map(record = record)

        /** @Então o mapeamento deve retornar um evento de CheckoutDone */
        expectThat(actual).isA<CheckoutDone>()

        /** @Então os dados do payload do registro devem ser iguais aos do evento */
        actual as CheckoutDone
        expectThat(event).and {
            get { id } isEqualTo actual.id
            get { amount.value } isEqualTo actual.amount.value
            get { country.alpha2 } isEqualTo actual.country.alpha2
            get { amount.currency.currencyCode } isEqualTo actual.amount.currency.currencyCode
        }
    }

    @Test
    fun `Event cannot be mapped`() {
        /** @Dado um evento desconhecido */
        val event = mockk<CheckoutEvent>(relaxed = true)

        /** @E que esse evento esteja em um registro de outbox */
        val record = Outbox(
            id = randomUUID().toString(),
            payload = eventMapper.toJson(event = event),
            revision = 1,
            sequence = 1,
            eventType = event::class.java.simpleName,
            occurredOn = now().toString(),
            aggregateId = event.toString(),
            aggregateType = "Checkout"
        )

        /** @Então uma exceção deverá ser lançada */
        val template = "Event <%s> cannot be mapped."
        val exception = assertThrows<IllegalStateException> {

            /** @Quando o método map for executado */
            eventFactory.map(record = record)
        }

        /** @E a mensagem da exceção deve especificar que o evento não pode ser mapeado */
        expectThat(exception.message).isEqualTo(template.format(event::class.java.simpleName))
    }
}
