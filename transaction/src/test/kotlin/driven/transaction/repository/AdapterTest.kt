package driven.transaction.repository

import com.jayway.jsonpath.JsonPath.parse
import domain.boundaries.Transactions
import domain.events.TransactionEvent
import domain.models.Amount
import domain.models.Country
import domain.models.Positive
import domain.models.Transaction
import domain.models.TransactionId
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.withHandleUnchecked
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import shared.Events.TRANSACTION_REQUESTED
import shared.MySQL
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.util.Currency.getInstance
import java.util.UUID

@Tag("IntegrationTest")
class AdapterTest {

    private val jdbi: Jdbi = MySQL.jdbi
    private val transactions: Transactions = Adapter(jdbi = jdbi)

    @Test
    fun `Transaction request`() {
        /** @Dado que uma transação foi solicitada */
        val event = TRANSACTION_REQUESTED

        /** @Quando a persistência dessa solicitação transação for efetuada */
        transactions.save(event = event)

        /** @Então uma nova transação deverá estar persistida */
        val transaction = transactionOf(transactionId = event.transactionId)

        /** @E ela deverá manter as informações contidas no evento */
        expectThat(transaction).and {
            get { id }.isEqualTo(event.transactionId)
            get { country.iso2 }.isEqualTo(event.country.iso2)
            get { amount.currency }.isEqualTo(event.amount.currency)
            get { amount.positive.value.setScale(2) }.isEqualTo(event.amount.positive.value)
        }

        /** @E deverá estar persistido um novo registro na outbox */
        val outboxRecord = outboxEventOf(event = event)

        /** @E o registro da outbox deve ser referente ao evento criado */
        expectThat(outboxRecord).and {
            get { revision }.isEqualTo(1)
            get { eventType }.isEqualTo(event::class.java.simpleName)
            get { aggregateId }.isEqualTo(event.transactionId.value.toString())
            get { aggregateType }.isEqualTo("Transaction")
        }

        /** @E o valor do payload deve refletir os dados do evento */
        expectThat(parse(outboxRecord.payload)).and {
            get { read<Int>("$.size()") }.isEqualTo(4)
            get { read<Int>("$.country.size()") }.isEqualTo(1)
            get { read<Int>("$.amount.size()") }.isEqualTo(2)
            get { read<String>("$.country.iso2") }.isEqualTo(event.country.iso2)
            get { read<String>("$.amount.value") }.isEqualTo(event.amount.positive.value.toString())
            get { read<String>("$.amount.currency") }.isEqualTo(event.amount.currency.currencyCode)
        }
    }

    @Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
    private fun transactionOf(transactionId: TransactionId): Transaction = jdbi.withHandleUnchecked { handle ->
        val query = """
                SELECT 
                    BIN_TO_UUID(id) AS id, 
                    value, 
                    currency, 
                    country_iso_2 AS countryIso2 
                FROM transaction 
                WHERE id = UUID_TO_BIN(:id)
            """.trimIndent()

        return@withHandleUnchecked handle
            .createQuery(query)
            .bind("id", transactionId.value.toString())
            .mapTo(RecordTransaction::class.java)
            .findOne()
            .orElseThrow()
            .toTransaction()
    }

    @Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
    private fun outboxEventOf(event: TransactionEvent) = jdbi.withHandleUnchecked { handle ->
        val query = """
                SELECT 
                    payload,             
                    revision,             
                    event_type AS eventType,             
                    aggregate_id AS aggregateId,             
                    aggregate_type AS aggregateType
                FROM outbox_event 
                WHERE aggregate_id = :id AND event_type = :eventType
            """.trimIndent()

        return@withHandleUnchecked handle
            .createQuery(query)
            .bind("id", event.transactionId.value.toString())
            .bind("eventType", event::class.java.simpleName)
            .mapTo(RecordOutboxEvent::class.java)
            .findOne()
            .orElseThrow()
    }

    private data class RecordTransaction(
        val id: UUID,
        val value: BigDecimal,
        val currency: String,
        val countryIso2: String
    ) {

        fun toTransaction() = Transaction(
            id = TransactionId(value = id),
            amount = Amount(positive = Positive(value = value), currency = getInstance(currency)),
            country = Country(iso2 = countryIso2)
        )
    }

    private data class RecordOutboxEvent(
        val payload: String,
        val revision: Int,
        val eventType: String,
        val aggregateId: String,
        val aggregateType: String
    )
}