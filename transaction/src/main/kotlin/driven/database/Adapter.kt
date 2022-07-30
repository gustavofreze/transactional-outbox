package driven.database

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces
import javax.sql.DataSource

@Dependent
class Adapter {

    @Produces
    fun jdbi(dataSource: DataSource): Jdbi = Jdbi
        .create(dataSource)
        .installPlugin(KotlinPlugin())
}
