package online.k0ras1k.pulse.controller.users

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.data.database.Country
import online.k0ras1k.pulse.data.database.User
import online.k0ras1k.pulse.data.enums.ValidationStatus
import online.k0ras1k.pulse.data.models.dto.UserData
import online.k0ras1k.pulse.data.models.inout.input.LoginInputModel
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse
import online.k0ras1k.pulse.data.models.inout.output.ImageEmptyProfileRespondModel
import online.k0ras1k.pulse.data.models.inout.output.ProfileRespondModel
import online.k0ras1k.pulse.data.static.ApplicationConstants
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import java.util.regex.Pattern

class UsersController(val call: ApplicationCall) {

    suspend fun register() {
        runBlocking {
            println("-----------------STARTING REGISTER-----------------")
            val receive = call.receive<UserData>()
            println("Register name - ${receive.login}")


            val validations = mutableMapOf<String, ValidationStatus>()

            validations["login"] = validateField(receive.login, 30, "[a-zA-Z0-9-]+")
            validations["email"] = validateField(receive.email, 50, null)
            validations["isPublic"] = ValidationStatus.ACCEPTED
            validations["phone"] = validateField(receive.phone, null, "\\+\\d+")
            validations["image"] = validateField(receive.image, 200, null)
            validations["countryCode"] = validateCountry(receive.countryCode)
            validations["password"] = validatePassword(receive.password)

            for (field in validations) {
                if (field.value != ValidationStatus.ACCEPTED) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("${field.value.text} Поле: ${field.key}"))
                    return@runBlocking
                }
            }

            if (User.selectUserByLogin(receive.login) != null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Пользователь с таким логином уже зарегистрирован!"))
                return@runBlocking
            }
            if (User.selectUserByMail(receive.email) != null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Пользователь с такой почтой уже зарегистрирован!"))
                return@runBlocking
            }

            val password_hash = BCrypt.hashpw(receive.password, BCrypt.gensalt())
            val target_user_data = UserData(
                login = receive.login,
                email = receive.email,
                password = password_hash,
                countryCode = receive.countryCode,
                isPublic = receive.isPublic,
                phone = receive.phone,
                image = receive.image
            )
            User.insertUser(target_user_data)

            println("-----------------REGISTER END-----------------")

            //Проверим что пользователь зарегистрировался, чтобы в дальнейшем получать профиль
            val selected_user_data = User.selectUserByLogin(receive.login)

            if (selected_user_data == null) {
                call.respond(HttpStatusCode.InternalServerError)
            }

            if (selected_user_data!!.image == "") {
                val target_profile_model = ImageEmptyProfileRespondModel (
                    login = selected_user_data.login,
                    email = selected_user_data.email,
                    countryCode = selected_user_data.countryCode,
                    isPublic = selected_user_data.isPublic,
                    phone = selected_user_data.phone,
                )
                call.respond(HttpStatusCode.Created, mapOf(Pair("profile", target_profile_model)))
                return@runBlocking
            }

            val target_profile_model = ProfileRespondModel (
                login = selected_user_data!!.login,
                email = selected_user_data.email,
                countryCode = selected_user_data.countryCode,
                isPublic = selected_user_data.isPublic,
                phone = selected_user_data.phone,
                image = selected_user_data.image
            )
            call.respond(HttpStatusCode.Created, mapOf(Pair("profile", target_profile_model)))
        }
    }

    suspend fun login() {
        runBlocking {
            println("-----------------STARTING LOGIN-----------------")
            val receive = call.receive<LoginInputModel>()
            println("Login name -  ${receive.login}")

            if (User.selectUserByLogin(receive.login) == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Пользователь с указанным логином и паролем не найден"))
                return@runBlocking
            }

            val selected_user = User.selectUserByLogin(receive.login)!!

            if (!BCrypt.checkpw(receive.password, selected_user.password)) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Пользователь с указанным логином и паролем не найден"))
                return@runBlocking
            }

            val token = JWT.create()
                .withClaim("login", selected_user.login)
                .withClaim("password", selected_user.password)
                .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 60 * 24 * 60))
                .sign(Algorithm.HMAC256(ApplicationConstants.SERVICE_SECRET_TOKEN))
            call.respond(hashMapOf("token" to token))

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