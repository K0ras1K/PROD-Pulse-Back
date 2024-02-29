package online.k0ras1k.pulse.controller.country

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.data.database.Country
import online.k0ras1k.pulse.data.enums.Region
import online.k0ras1k.pulse.data.models.inout.input.SelectCountryInputModel
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse

class CountryController(val call: ApplicationCall) {

    fun selectCountry() {
        runBlocking {
            try {
                val regionsParam = call.request.queryParameters.getAll("region")
                val regions = regionsParam?.mapNotNull { Region.valueOf(it) } ?: emptyList()

                val countries = Country.selectCountry(regions)
                call.respond(HttpStatusCode.OK, countries)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message!!))
            }
        }
    }

    fun selectFromAlpha() {
        runBlocking {
            val alpha2 = call.parameters["alpha2"] ?: return@runBlocking call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Missing or invalid alpha2 parameter")
            )

            val country = Country.selectCountryByAlpha2(alpha2)
            if (country != null) {
                call.respond(country)
            } else {
                val alpha2 = call.parameters["alpha2"] ?: return@runBlocking call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Country not found")
                )
            }
        }
    }

}