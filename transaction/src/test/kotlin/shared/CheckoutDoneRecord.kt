package shared

import driver.kafka.checkout.factories.events.CheckoutDone
import driver.kafka.checkout.factories.models.Amount
import driver.kafka.checkout.factories.models.CheckoutId
import driver.kafka.checkout.factories.models.Country
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder

object CheckoutDoneRecord {

    fun from(id: CheckoutId, amount: Amount, country: Country): GenericRecord {
        val schema = Schema.load(name = "payin.checkout-value")
            .rawSchema()
            .types
            .first { it.name == CheckoutDone::class.simpleName }

        val amountRecord = GenericRecordBuilder(schema.fields.first { it.name().equals("amount") }.schema())
            .set("value", amount.value.toString())
            .set("currency", amount.currency.currencyCode)
            .build()

        val countryRecord = GenericRecordBuilder(schema.fields.first { it.name().equals("country") }.schema())
            .set("alpha2", country.alpha2)
            .build()

        return GenericRecordBuilder(schema)
            .set("id", id.value.toString())
            .set("amount", amountRecord)
            .set("country", countryRecord)
            .build()
    }
}
