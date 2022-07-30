package driver.kafka

import driver.kafka.checkout.CheckoutConsumer
import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.models.Amount
import driver.kafka.checkout.factories.models.CheckoutId
import driver.kafka.checkout.factories.models.Country
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry.getClientForScope
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.common.serialization.Serdes.String
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import shared.CheckoutDoneRecord
import shared.Schema
import shared.mocks.EventBusMock
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.util.Currency.getInstance
import java.util.UUID.fromString

@Tag("UnitTest")
class TopologyTest {

    private lateinit var consumer: CheckoutConsumer
    private lateinit var eventBus: EventBusMock
    private lateinit var inputTopic: TestInputTopic<String, Any>

    @BeforeEach
    fun `Before each`() {
        eventBus = EventBusMock()
        consumer = CheckoutConsumer(eventBus = eventBus)

        val settings = Settings(
            applicationId = "test",
            bootstrapServers = "dummy:1234",
            schemaRegistryUrl = "mock://test"
        )
        val schema = Schema.load(name = "payin.checkout-value")
        val client = getClientForScope("test")
        val topology = TopologyFactory().build(settings = settings, consumer = consumer)
        val serializer = KafkaAvroSerializer(client, settings.toMap())
        val topologyDriver = TopologyTestDriver(topology, settings.toProperties())

        inputTopic = topologyDriver.createInputTopic(consumer.topic, String().serializer(), serializer)

        client.register(consumer.topic, schema)
    }

    @Test
    fun `Event consumption`() {
        /** @Dado que o registro avro é um evento válido */
        val record = CheckoutDoneRecord.from(
            id = CheckoutId(value = fromString("a81c951d-876e-4d99-9415-b494a53299fc")),
            amount = Amount(value = BigDecimal(10.00), currency = getInstance("BRL")),
            country = Country(alpha2 = "BR")
        )

        /** @Quando ele é enviado para o tópico de eventos */
        inputTopic.pipeInput(record)

        /** @Então o evento deserializado deverá ser enviado para o barramento de eventos */
        expectThat(eventBus.events).hasSize(1)

        /** @E os dados do evento devem ser iguais aos do registro avro */
        expectThat(eventBus.events.first() as CheckoutDone).and {
            get { id.value.toString() } isEqualTo "a81c951d-876e-4d99-9415-b494a53299fc"
            get { country.alpha2 } isEqualTo "BR"
            get { amount.value } isEqualTo BigDecimal(10.00)
            get { amount.currency.currencyCode } isEqualTo "BRL"
        }
    }
}
