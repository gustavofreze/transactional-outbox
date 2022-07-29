package domain.models

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import java.math.BigDecimal
import java.util.*

@Tag("UnitTest")
class AmountTest {

    @CsvSource(
        "1.1, GNF",
        "1.1, JPY",
        "1.001, BRL",
        "1.001, USD",
        "1.0001, JOD",
        "1.0001, KWD",
    )
    @ParameterizedTest
    fun `Exception on an overly precise amount`(value: BigDecimal, currency: Currency) {
        val exception = assertThrows<IllegalStateException> {
            Amount(positive = Positive(value = value), currency = currency)
        }

        val template = "Fractional value of <%s> must have at most <%s> decimal places for the currency <%s>."

        expectThat(exception.message)
            .isNotNull()
            .isEqualTo(template.format(value, currency.defaultFractionDigits, currency))
    }
}