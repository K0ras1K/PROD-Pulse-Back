package online.k0ras1k.pulse.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import online.k0ras1k.pulse.controller.country.CountryController
import online.k0ras1k.pulse.controller.users.UsersController
import online.k0ras1k.pulse.data.static.ApplicationConstants

fun Application.configureUnAuthRouting() {

    routing {
        get("${ApplicationConstants.API_ENDPOINT}/ping") {
            call.respond(HttpStatusCode.OK)
        }

        //REGIONS
        get("${ApplicationConstants.API_ENDPOINT}/countries") {
            CountryController(call).selectCountry()
        }
        get("${ApplicationConstants.API_ENDPOINT}/countries/{alpha2}") {
            CountryController(call).selectFromAlpha()
        }

        //USERS
        post("${ApplicationConstants.API_ENDPOINT}/auth/register") {
            UsersController(call).register()
        }
        post("${ApplicationConstants.API_ENDPOINT}/auth/sign-in") {
            UsersController(call).login()
        }
    }

}