package online.k0ras1k.pulse.data.models.inout.input

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordInputModel (

    val oldPassword: String,
    val newPassword: String

)