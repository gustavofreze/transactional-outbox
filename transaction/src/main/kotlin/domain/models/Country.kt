package domain.models

import java.util.Locale.getISOCountries

data class Country(val alpha2: String) {

    init {
        val template = "The country's ISO 3166-1-alpha-2 <%s> code is invalid."
        val countries = getISOCountries()

        check(countries.any { it.equals(alpha2) }) { template.format(alpha2) }
    }
}
