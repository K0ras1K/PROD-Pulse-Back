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
                // Получаем все регионы из запроса
                val regionsParam = call.request.queryParameters.getAll("region")

                // Проверяем, что все регионы из запроса существуют в Enum
                val validRegions = regionsParam?.all { regionName ->
                    try {
                        Region.valueOf(regionName)
                        true
                    } catch (e: IllegalArgumentException) {
                        false
                    }
                } ?: true // Если список регионов пуст, считаем запрос валидным

                if (!validRegions) {
                    // Если найдены несуществующие регионы, отправляем ошибку
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Переданы неверные значения регионов."))
                } else {
                    // Все регионы валидны, продолжаем обработку запроса
                    val regions = regionsParam?.mapNotNull { Region.valueOf(it) } ?: emptyList()
                    val countries = Country.selectCountry(regions)
                    call.respond(HttpStatusCode.OK, countries)
                }
            } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Формат входного запроса не соответствует формату."))
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