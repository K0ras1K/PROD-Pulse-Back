package online.k0ras1k.pulse.tests

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.module
import kotlin.test.Test
import kotlin.test.assertEquals

class CountryTest {

    @Test
    fun testRegions() = testApplication {
        application {
            module()
        }
        val response = client.get("/api/countries")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testSelectRegion() = testApplication {
        application {
            module()
        }
        val response = client.get("/api/countries/RU")
        assertEquals(HttpStatusCode.OK, response.status)

        val unreal_response = client.get("/api/countries/123")
        assertEquals(HttpStatusCode.BadRequest, unreal_response.status)
    }

}