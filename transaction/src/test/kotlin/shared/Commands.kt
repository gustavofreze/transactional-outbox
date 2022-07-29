package shared

import domain.handlers.commands.RequestTransaction
import domain.models.Amount
import domain.models.Positive
import shared.commonvalues.Amounts
import shared.commonvalues.Amounts.randomAmount
import shared.commonvalues.Countries.randomCountry
import shared.commonvalues.Currencies.randomCurrency
import java.lang.Math.random
import java.math.BigDecimal
import java.math.RoundingMode

object Commands {

    val REQUEST_TRANSACTION = RequestTransaction(amount = randomAmount(), country = randomCountry())
}