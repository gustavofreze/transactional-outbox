package shared.commonvalues

import java.util.Currency
import java.util.Currency.getInstance

object Currencies {

    fun randomCurrency(): Currency = listOf(
        getInstance("BRL"),
        getInstance("USD"),
        getInstance("EUR"),
        getInstance("GBP")
    ).random()
}
