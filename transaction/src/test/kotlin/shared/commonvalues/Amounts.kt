package shared.commonvalues

import domain.models.Amount
import domain.models.Positive
import shared.commonvalues.Currencies.randomCurrency
import java.math.BigDecimal
import java.math.RoundingMode

object Amounts {

    fun randomAmount(): Amount = Amount(positive = Positive(value = randomValue()), currency = randomCurrency())

    fun randomValue(): BigDecimal = BigDecimal(Math.random().plus(1)).setScale(2, RoundingMode.HALF_UP)
}
