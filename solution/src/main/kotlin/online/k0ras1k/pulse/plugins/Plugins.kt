package online.k0ras1k.pulse.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import online.k0ras1k.pulse.data.database.User
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse
import online.k0ras1k.pulse.data.static.ApplicationConstants
import java.time.Duration

fun Application.initializePlugins() {

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JWT.require(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN)).build())
            validate { credential ->
                val passwordHash = credential.payload.getClaim("passwordHash").asString()
                if (credential.payload.getClaim("login").asString() != "" && isValidPasswordHash(credential.payload.getClaim("login").asString() , passwordHash)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Переданный токен не существует, либо некорректен"))
            }
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = false
            }
        )
    }

//    install(ContentNegotiation) {
//        jackson {
//            // customize the Jackson serializer as usual
//            configure(SerializationFeature.INDENT_OUTPUT, true)
//            setDefaultPrettyPrinter(DefaultPrettyPrinter().apply {
//                indentArraysWith(DefaultPrettyPrinter.FixedSpaceIndenter.instance)
//            })
//        }
//    }
}

fun isValidPasswordHash(username: String, passwordHash: String): Boolean {
    return (User.selectUserByLogin(username)!!.password == passwordHash)
}