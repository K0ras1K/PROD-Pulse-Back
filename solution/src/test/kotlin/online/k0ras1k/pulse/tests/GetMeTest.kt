package online.k0ras1k.pulse.tests

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.data.models.inout.input.LoginInputModel
import online.k0ras1k.pulse.data.models.inout.output.TokenRespondOutput
import online.k0ras1k.pulse.module
import online.k0ras1k.pulse.utils.UsersUtils
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class GetMeTest {

    @Test
    fun testGetMy() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        application {
            module()
        }

        val username = UsersUtils.generateComplexUsername(Random.nextInt(5, 16))
        val password = "SsssAh9.Sah"

        val response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)

            setBody(
                UserData(
                    login = username,
                    email = "${username}@k0ras1k.online",
                    password = password,
                    countryCode = "RU",
                    isPublic = true,
                    phone = "+79524693305",
                    image = "https://api.eternalplanet.online/public/avatars/neferpito.png"
                )
            )
        }
        assertEquals(HttpStatusCode.Created, response.status)

        val sign_response = client.post("/api/auth/sign-in") {
            contentType(ContentType.Application.Json)

            setBody(
                LoginInputModel(
                    login = username,
                    password = password
                )
            )
        }

        assertEquals(HttpStatusCode.OK, sign_response.status)
        val token = sign_response.body<TokenRespondOutput>().token

        val get_me = client.get("/api/me/profile") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }
        }

        println(get_me.bodyAsText())
        assertEquals(HttpStatusCode.OK, get_me.status)

    }

}