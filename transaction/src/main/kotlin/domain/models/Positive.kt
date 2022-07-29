package domain.models

import java.math.BigDecimal

data class Positive(val value: BigDecimal) {

    init {
        val template = "Value must be positive <%s>."

        check(value > BigDecimal.ZERO) { template.format(value) }
    }
}
