package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.models.dto.UserData
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object User: Table("users") {

    //==============================================SCHEMA==============================================================
    private val _login = User.varchar("login", 30)
    private val _email = User.varchar("email", 50)
    private val _password = User.varchar("password", 200)
    private val _is_public = User.bool("is_public")
    private val _country_code = User.varchar("country_code", 3)
    private val _phone = User.varchar("phone", 12)
    private val _image = User.varchar("image", 200)
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun insertUser(user_data: UserData) {
        transaction {
            User.insert {
                it[_login] = user_data.login
                it[_email] = user_data.email
                it[_password] = user_data.password
                it[_country_code] = user_data.countryCode
                it[_is_public] = user_data.isPublic
                it[_phone] = user_data.phone
                it[_image] = user_data.image
            }
        }
    }

    fun selectUserByLogin(login: String): UserData? {
        return try {
            transaction {
                val respond = User.select { User._login.eq(login) }.single()
                UserData(
                    login = respond[_login],
                    email = respond[_email],
                    password = respond[_password],
                    countryCode = respond[_country_code],
                    isPublic = respond[_is_public],
                    phone = respond[_phone],
                    image = respond[_image]
                )
            }
        }
        catch (exception: Exception) {
            null
        }
    }

    fun selectUserByMail(mail: String): UserData? {
        return try {
            transaction {
                val respond = User.select { User._email.eq(mail) }.single()
                UserData(
                    login = respond[_login],
                    email = respond[_email],
                    password = respond[_password],
                    countryCode = respond[_country_code],
                    isPublic = respond[_is_public],
                    phone = respond[_phone],
                    image = respond[_image]
                )
            }
        }
        catch (exception: Exception) {
            null
        }
    }

    fun updateUser(user_data: UserData) {
        transaction {
            User.update( { User._login.eq(user_data.login) } ) {
                it[_country_code] = user_data.countryCode
                it[_is_public] = user_data.isPublic
                it[_phone] = user_data.phone
                it[_image] = user_data.image
            }
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    fun updatePassword(target_login: String, target_hashed_password: String) {
        transaction {
            User.update( { User._login.eq(target_login) } ) {
                it[_password] = target_hashed_password
            }
        }
    }
    //===============================================UTIL===============================================================

}