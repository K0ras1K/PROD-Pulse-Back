package online.k0ras1k.pulse.data.models.dto

import kotlinx.serialization.Serializable
import online.k0ras1k.pulse.data.enums.Region

@Serializable
data class CountryData (

    val name: String,
    val alpha2: String,
    val alpha3: String,
    val region: Region?,

)
