package online.k0ras1k.pulse.data.models.inout.input

import kotlinx.serialization.Serializable

@Serializable
data class LoginInputModel (

    val login: String,
    val password: String

)