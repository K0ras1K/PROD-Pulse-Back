package online.k0ras1k.pulse.tests

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.data.models.inout.input.LoginInputModel
import online.k0ras1k.pulse.data.models.inout.input.posts.CreatePostInputModel
import online.k0ras1k.pulse.data.models.inout.output.PostOutputModel
import online.k0ras1k.pulse.data.models.inout.output.TokenRespondOutput
import online.k0ras1k.pulse.module
import online.k0ras1k.pulse.utils.UsersUtils
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class SelectPostTest {

    @Test
    fun selectPost() = testApplication {
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

        val create_post_respond = client.post("/api/posts/new") {
            contentType(ContentType.Application.Json)

            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }

            setBody(
                CreatePostInputModel(
                    content = "Ультиматка имба??",
                    tags = mutableListOf("IntelliJ", "Prod")
                )
            )
        }

        assertEquals(HttpStatusCode.OK, create_post_respond.status)

        val post_model = create_post_respond.body<PostOutputModel>()
        assertEquals(post_model.content, "Ультиматка имба??")

        val select_post_model = client.get("/api/posts/${post_model.id}") {
            contentType(ContentType.Application.Json)

            headers {
                append(HttpHeaders.Authorization, "Bearer ${token}")
            }
        }

        assertEquals(HttpStatusCode.OK, select_post_model.status)
        val selected_post_model = select_post_model.body<PostOutputModel>()
        assertEquals(selected_post_model.tags, post_model.tags)
    }

}