package online.k0ras1k.pulse.tests

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.module
import online.k0ras1k.pulse.utils.UsersUtils
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class RegisterTest {

    @Test
    fun testRegister() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        application {
            module()
        }
        val response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            val username = UsersUtils.generateComplexUsername(Random.nextInt(5, 16))
            setBody(
                UserData(
                    login = username,
                    email = "${username}@k0ras1k.online",
                    password = "SsssAh9.Sah",
                    countryCode = "RU",
                    isPublic = true,
                    phone = "+79524693305",
                    image = "https://api.eternalplanet.online/public/avatars/neferpito.png"
                )
            )
        }
        println(response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }

}