package online.k0ras1k.pulse.data.models.inout.input

import kotlinx.serialization.Serializable

@Serializable
data class PatchProfileInputModel (

    val countryCode: String = "",
    val isPublic: Boolean? = null,
    val phone: String = "",
    val image: String = ""

)