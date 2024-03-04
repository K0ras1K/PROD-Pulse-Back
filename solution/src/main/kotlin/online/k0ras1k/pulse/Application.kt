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
    val serverAddress = System.getenv("SERVER_ADRESS") ?: "0.0.0.0:8080"
    println(serverAddress)

    val host = serverAddress.split(":")[0]
    val port = serverAddress.split(":")[1]
    embeddedServer(CIO, port = port.toInt(), host = host, module = Application::module).start(wait = true)
}

fun Application.module() {
    // connect to database using Hikari Connection Pool
    val jdbcUrl = System.getenv("POSTGRES_JDBC_URL") ?: "jdbc:postgresql://62.109.21.83:5431/pulse"
    val username = System.getenv("POSTGRES_USERNAME") ?: "K0ras1K"
    val db = Database.connect(
        DatabaseFactory.createHikariDataSource(
            jdbcUrl,
            "org.postgresql.Driver",
            username,
        ),
    )


    configureUnAuthRouting()
    configureAuthRouting()
    initializePlugins()

    Initialization.initialize()

}