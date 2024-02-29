package online.k0ras1k.pulse.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRespondModel (

    val login: String,
    val email: String,
    val countryCode: String,
    val isPublic: Boolean,
    val phone: String,
    val image: String

)

@Serializable
data class ImageEmptyProfileRespondModel (
    val login: String,
    val email: String,
    val countryCode: String,
    val isPublic: Boolean,
    val phone: String,
)