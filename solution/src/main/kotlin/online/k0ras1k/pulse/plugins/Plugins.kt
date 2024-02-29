package online.k0ras1k.pulse.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse
import online.k0ras1k.pulse.data.static.ApplicationConstants
import java.time.Duration

fun Application.initializePlugins() {

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JWT.require(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN)).build())
            validate { credential ->
                if (credential.payload.getClaim("login").asString() != "") {
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
        json()
    }
}