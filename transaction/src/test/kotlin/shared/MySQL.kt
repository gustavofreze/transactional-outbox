package shared

import org.flywaydb.core.Flyway
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName
import com.mysql.cj.jdbc.MysqlDataSource

object MySQL {

    private val image = DockerImageName
        .parse("mysql:8.0.29")
        .asCompatibleSubstituteFor("mysql")

    private class MySqlContainer : MySQLContainer<MySqlContainer>(image)
    private val CONTAINER: MySqlContainer = MySqlContainer()

    val jdbi: Jdbi by lazy {

        CONTAINER.start()

        val dataSource = MysqlDataSource().also {
            it.user = CONTAINER.username
            it.password = CONTAINER.password
            it.setURL(CONTAINER.jdbcUrl)
        }

        Flyway
            .configure()
            .locations("db/mysql/migration")
            .dataSource(dataSource)
            .load()
            .migrate()

        Jdbi
            .create(dataSource)
            .installPlugin(KotlinPlugin())
    }
}