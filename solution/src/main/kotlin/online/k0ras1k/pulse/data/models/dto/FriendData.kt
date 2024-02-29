package online.k0ras1k.pulse.data.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class FriendData (

    val login: String,
    val friend: String,
    val addedAt: Long

)