package domain.models

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

@Tag("UnitTest")
class CountryTest {

    @CsvSource(
        "XX",
        "BRA",
        "USD"
    )
    @ParameterizedTest
    fun `Exception where ISO 3166-1-alpha-2 code is invalid`(alpha2: String) {
        val exception = assertThrows<IllegalStateException> {
            Country(alpha2 = alpha2)
        }

        val template = "The country's ISO 3166-1-alpha-2 <%s> code is invalid."

        expectThat(exception.message)
            .isNotNull()
            .isEqualTo(template.format(alpha2))
    }
}
