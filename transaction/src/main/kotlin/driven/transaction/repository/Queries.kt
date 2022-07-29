package driven.transaction.repository

object Queries {

    val INSERT = """        
        INSERT INTO transaction (id, value, currency, country_iso_2)
        VALUES (UUID_TO_BIN(:id), :value, :currency, :countryIso2);
    """.trimIndent()
}