package online.k0ras1k.pulse.controller.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.controller.AbstractController
import online.k0ras1k.pulse.data.database.Country
import online.k0ras1k.pulse.data.database.User
import online.k0ras1k.pulse.data.enums.ValidationStatus
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.data.models.inout.input.PatchProfileInputModel
import online.k0ras1k.pulse.data.models.inout.input.UpdatePasswordInputModel
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse
import online.k0ras1k.pulse.data.models.inout.output.ProfileRespondModel
import org.mindrot.jbcrypt.BCrypt
import java.util.regex.Pattern

class AuthUserController(call: ApplicationCall) : AbstractController(call) {

    suspend fun getMe() {
        runBlocking {
            val user_model = User.selectUserByLogin(login)

            if (user_model == null) {
                call.respond(HttpStatusCode.Unauthorized)
                return@runBlocking
            }

            val target_profile_model = ProfileRespondModel(
                login = user_model!!.login,
                email = user_model.email,
                countryCode = user_model.countryCode,
                isPublic = user_model.isPublic,
                phone = user_model.phone.takeIf { it.isNotEmpty() },
                image = user_model.image.takeIf { it.isNotEmpty() }
            )
            call.respond(HttpStatusCode.OK, target_profile_model)
        }
    }

    suspend fun updateMe() {
        runBlocking {
            val path_model = call.receive<PatchProfileInputModel>()

            val target_user_data = User.selectUserByLogin(login)

            var target_country_code = target_user_data!!.countryCode
            var target_is_public = target_user_data.isPublic
            var target_phone = target_user_data.phone
            var target_image = target_user_data.image

            //VALIDATION
            if (path_model.isPublic != null) {
                target_is_public = path_model.isPublic
            }

            if (path_model.phone.isNotEmpty()) {
                if (validateField(path_model.phone, null, "\\+\\d+") != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка в запросе! Поле: phone"))
                    return@runBlocking
                }
                target_phone = path_model.phone
            }

            if (path_model.image.isNotEmpty()) {
                if (validateField(path_model.image, 200, null) != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка в запросе! Поле: image"))
                    return@runBlocking
                }
                target_image = path_model.image
            }

            if (path_model.countryCode.isNotEmpty()) {
                if (validateCountry(path_model.countryCode) != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка в запросе! Поле: countryCode"))
                    return@runBlocking
                }
                target_country_code = path_model.countryCode
            }

            val new_user_model = UserData(
                login = target_user_data.login,
                email = target_user_data.email,
                password = target_user_data.password,
                countryCode = target_country_code,
                isPublic = target_is_public,
                phone = target_phone,
                image = target_image
            )
            User.updateUser(new_user_model)

            //Проверим что пользователь зарегистрировался, чтобы в дальнейшем получать профиль
            val selected_user_data = User.selectUserByLogin(new_user_model.login)

            if (selected_user_data == null) {
                call.respond(HttpStatusCode.InternalServerError)
            }

            val target_profile_model = ProfileRespondModel(
                login = selected_user_data!!.login,
                email = selected_user_data.email,
                countryCode = selected_user_data.countryCode,
                isPublic = selected_user_data.isPublic,
                phone = selected_user_data.phone.takeIf { it.isNotEmpty() },
                image = selected_user_data.image.takeIf { it.isNotEmpty() }
            )
            call.respond(HttpStatusCode.OK, mapOf(Pair("profile", target_profile_model)))
        }
    }

    suspend fun getProfile() {
        runBlocking {
            val target_login = call.parameters["login"]!!
            val target_user_data = User.selectUserByLogin(target_login)

            if (target_user_data == null) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Пользователь с указанным логином не найден"))
                return@runBlocking
            }

            if (target_user_data.isPublic == false) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("У вас нет доступа к профилю данного пользователя"))
                return@runBlocking
            }

            val target_profile_model = ProfileRespondModel(
                login = target_user_data!!.login,
                email = target_user_data.email,
                countryCode = target_user_data.countryCode,
                isPublic = target_user_data.isPublic,
                phone = target_user_data.phone.takeIf { it.isNotEmpty() },
                image = target_user_data.image.takeIf { it.isNotEmpty() }
            )
            call.respond(HttpStatusCode.OK, target_profile_model)
        }
    }

    suspend fun updatePassword() {
        runBlocking {
            val receive = call.receive<UpdatePasswordInputModel>()
            val request_user = User.selectUserByLogin(login)!!

            if (!BCrypt.checkpw(receive.oldPassword, request_user.password)) {
                call.respond(HttpStatusCode.Forbidden, ErrorResponse("Старый пароль не верен"))
                return@runBlocking
            }

            if (validatePassword(receive.newPassword) != ValidationStatus.ACCEPTED) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Новый пароль не соответствует требованиям безопасности"))
                return@runBlocking
            }

            val password_hash = BCrypt.hashpw(receive.newPassword, BCrypt.gensalt())
            User.updatePassword(login, password_hash)

            call.respond(HttpStatusCode.OK, mapOf(Pair("status", "ok")))
        }
    }

    private fun validateField(field: String, maxLength: Int?, pattern: String?): ValidationStatus {
        if (maxLength != null && field.length > maxLength) {
            return ValidationStatus.INVALID_LENGTH
        }
        if (pattern != null && !Pattern.matches(pattern, field)) {
            return ValidationStatus.INVALID_FORMAT
        }
        return ValidationStatus.ACCEPTED
    }

    private fun validateCountry(country_code: String): ValidationStatus {
        val country = Country.selectCountryByAlpha2(country_code)
        if (country == null) {
            return ValidationStatus.COUNTRY_CODE_NOT_FOUND
        }
        else {
            return ValidationStatus.ACCEPTED
        }
    }

    private fun validatePassword(password: String): ValidationStatus {
        val lowercasePattern = Regex("[a-z]")
        val uppercasePattern = Regex("[A-Z]")
        val digitPattern = Regex("\\d")

        return when {
            password.length < 6 -> ValidationStatus.INVALID_LENGTH
            password.length > 100 -> ValidationStatus.INVALID_LENGTH
            !lowercasePattern.containsMatchIn(password) || !uppercasePattern.containsMatchIn(password) || !digitPattern.containsMatchIn(password) -> ValidationStatus.INVALID_FORMAT
            else -> ValidationStatus.ACCEPTED
        }
    }
}