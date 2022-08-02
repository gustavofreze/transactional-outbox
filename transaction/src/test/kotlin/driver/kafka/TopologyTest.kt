package driver.kafka

import driver.kafka.checkout.CheckoutConsumer
import driver.kafka.checkout.factories.dtos.Amount
import driver.kafka.checkout.factories.dtos.Country
import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.outbox.Outbox
import driver.kafka.outbox.OutboxSerde
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry.getClientForScope
import org.apache.kafka.common.serialization.Serdes.String
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import shared.Schema
import shared.commonvalues.Amounts.randomValue
import shared.commonvalues.Countries.randomAlphaCode
import shared.commonvalues.Currencies.randomCurrency
import shared.mocks.EventBusMock
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import java.time.Instant.now
import java.util.UUID.randomUUID
import driver.kafka.checkout.factories.Adapter as EventFactory
import driver.kafka.mapper.Adapter as EventMapper

@Tag("UnitTest")
class TopologyTest {

    private lateinit var eventBus: EventBusMock
    private lateinit var inputTopic: TestInputTopic<String, Outbox>
    private lateinit var eventMapper: EventMapper
    private lateinit var eventFactory: EventFactory
    private lateinit var checkoutConsumer: CheckoutConsumer

    @BeforeEach
    fun `Before each`() {
        eventBus = EventBusMock()
        eventMapper = EventMapper()
        eventFactory = EventFactory(eventMapper = eventMapper)
        checkoutConsumer = CheckoutConsumer(eventBus = eventBus, eventFactory = eventFactory)

        val settings = Settings(
            applicationId = "transaction-test",
            bootstrapServers = "dummy:1234",
            schemaRegistryUrl = "mock://test"
        )
        val schema = Schema.load(name = "payin.checkout-value")
        val client = getClientForScope("test")
        val topology = TopologyFactory().build(settings = settings, consumer = checkoutConsumer)
        val topologyDriver = TopologyTestDriver(topology, settings.toProperties())

        val keySerializer = String().serializer()
        val valueSerializer = OutboxSerde.build().serializer()
        inputTopic = topologyDriver.createInputTopic(checkoutConsumer.topic, keySerializer, valueSerializer)

        client.register(checkoutConsumer.topic, schema)
    }

    @Test
    fun `Event consumption`() {
        /** @Dado um evento válido */
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

        /** @Quando ele é enviado para o tópico de eventos */
        inputTopic.pipeInput(record)

        /** @Então o evento deserializado deverá ser enviado para o barramento de eventos */
        expectThat(eventBus.events).hasSize(1)

        /** @E os dados do evento devem ser iguais aos do payload do registro enviado */
        expectThat(eventBus.events.first() as CheckoutDone).and {
            get { id } isEqualTo event.id
            get { amount.value } isEqualTo event.amount.value
            get { country.alpha2 } isEqualTo event.country.alpha2
            get { amount.currency.currencyCode } isEqualTo event.amount.currency.currencyCode
        }
    }
}
