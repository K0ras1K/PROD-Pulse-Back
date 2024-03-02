package online.k0ras1k.pulse

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseFactory {

    fun createHikariDataSource(
        url: String,
        driver: String,
        login: String,
    ) = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 10
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            username = login
            password = System.getProperty("POSTGRES_PASSWORD")
            validate()
        },
    )
}