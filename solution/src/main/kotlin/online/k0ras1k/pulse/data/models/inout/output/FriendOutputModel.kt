package online.k0ras1k.pulse.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class FriendOutputModel (

    val login: String,
    val addedAt: String

)