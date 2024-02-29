package online.k0ras1k.pulse

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import online.k0ras1k.pulse.plugins.initializePlugins
import online.k0ras1k.pulse.routing.configureAuthRouting
import online.k0ras1k.pulse.routing.configureUnAuthRouting
import org.jetbrains.exposed.sql.Database

fun main() {
//    val site_db = Database.connect("jdbc:mariadb://86.110.212.152:3306/testsite?enabledTLSProtocols=TLSv1.2&serverTimezone=UTC", driver = "org.mariadb.jdbc.Driver", user = "neferpito", password = "Shah9Sah.")
    val host = System.getenv("SERVER_ADRESS").split(":")[0]
    val port = System.getenv("SERVER_ADRESS").split(":")[1]
    embeddedServer(CIO, port = port.toInt(), host = host, module = Application::module).start(wait = true)
}

fun Application.module() {
    println("Postgres jbdc url - ${System.getenv("POSTGRES_JDBC_URL")}")
    println("Postgres user - ${System.getenv("POSTGRES_USERNAME")}")
    println("Postgres pw - ${System.getenv("POSTGRES_PASSWORD")}")
    // connect to database using Hikari Connection Pool
    val db = Database.connect(
        DatabaseFactory.createHikariDataSource(
            System.getenv("POSTGRES_JDBC_URL"),
            "org.postgresql.Driver",
            System.getenv("POSTGRES_USERNAME"),
        ),
    )


    configureUnAuthRouting()
    configureAuthRouting()
    initializePlugins()

    Initialization.initialize()

}