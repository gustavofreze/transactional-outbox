package driven.transaction.repository

object Queries {

    val INSERT = """        
        INSERT INTO transaction (id, value, currency, country_alpha2)
        VALUES (UUID_TO_BIN(:id), :value, :currency, :countryAlpha2);
    """.trimIndent()
}
