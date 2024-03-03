package online.k0ras1k.pulse.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserData(

    val login: String,
    val email: String,
    val password: String,
    val countryCode: String,
    val isPublic: Boolean,
    val phone: String = "",
    val image: String = ""

)
