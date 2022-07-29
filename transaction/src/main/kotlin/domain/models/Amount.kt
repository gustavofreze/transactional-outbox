package domain.models

import java.util.Currency

data class Amount(val positive: Positive, val currency: Currency) {

    init {
        val scale = positive.value.stripTrailingZeros().scale()
        val maximum = currency.defaultFractionDigits
        val template = "Fractional value of <%s> must have at most <%s> decimal places for the currency <%s>."

        check(scale <= maximum) { template.format(positive.value, maximum, currency) }
    }
}