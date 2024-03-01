package online.k0ras1k.pulse.tests

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.data.models.inout.input.FriendInputModel
import online.k0ras1k.pulse.data.models.inout.input.LoginInputModel
import online.k0ras1k.pulse.data.models.inout.output.TokenRespondOutput
import online.k0ras1k.pulse.module
import online.k0ras1k.pulse.utils.UsersUtils
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class AddFriendTest {

    @Test
    fun testAddFriend() = testApplication {
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

        val add_friend_response = client.post("/api/friends/add") {
            contentType(ContentType.Application.Json)

            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }

            setBody(
                FriendInputModel(
                    login = "neferpito"
                )
            )
        }
        assertEquals(HttpStatusCode.NotFound, add_friend_response.status)


        val second_username = UsersUtils.generateComplexUsername(Random.nextInt(5, 16))
        val second_password = "SsssAh9.Sah"

        val second_response = client.post("/api/auth/register") {
            contentType(ContentType.Application.Json)

            setBody(
                UserData(
                    login = second_username,
                    email = "${second_username}@k0ras1k.online",
                    password = second_password,
                    countryCode = "RU",
                    isPublic = true,
                    phone = "+79524693305",
                    image = "https://api.eternalplanet.online/public/avatars/neferpito.png"
                )
            )
        }
        assertEquals(HttpStatusCode.Created, second_response.status)


        val second_add_friend_response = client.post("/api/friends/add") {
            contentType(ContentType.Application.Json)

            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }

            setBody(
                FriendInputModel(
                    login = second_username
                )
            )
        }
        assertEquals(HttpStatusCode.OK, second_add_friend_response.status)
    }

}