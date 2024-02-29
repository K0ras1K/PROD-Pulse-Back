package online.k0ras1k.pulse.data.models.inout.input

import kotlinx.serialization.Serializable
import online.k0ras1k.pulse.data.enums.Region

@Serializable
data class SelectCountryInputModel (

    val region: List<Region>?

)