package online.k0ras1k.pulse.tests

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.module
import kotlin.test.Test
import kotlin.test.assertEquals

class PingTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val response = client.get("/api/ping")
        assertEquals(HttpStatusCode.OK, response.status)
    }

}