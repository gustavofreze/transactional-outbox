package shared.commonvalues

import domain.models.Country

object Countries {

    fun randomCountry() = Country(iso2 = listOf("BR", "US", "GB", "CH").random())
}