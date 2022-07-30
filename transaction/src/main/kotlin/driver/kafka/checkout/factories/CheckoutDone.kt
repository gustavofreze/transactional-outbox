package driver.kafka.checkout.factories

import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.models.Amount
import driver.kafka.checkout.factories.models.CheckoutId
import driver.kafka.checkout.factories.models.Country
import org.apache.avro.generic.GenericRecord
import java.util.Currency.getInstance
import java.util.UUID.fromString

data class CheckoutDone(private val record: GenericRecord) : EventFactory {

    override fun build(): CheckoutDone {
        val id = record.getString("id")
        val amount = record.getRecord("amount")
        val country = record.getRecord("country")

        return CheckoutDone(
            id = CheckoutId(value = fromString(id)),
            amount = Amount(
                value = amount.getString("value").toBigDecimal(),
                currency = getInstance(amount.getString("currency"))
            ),
            country = Country(alpha2 = country.getString("alpha2"))
        )
    }
}
