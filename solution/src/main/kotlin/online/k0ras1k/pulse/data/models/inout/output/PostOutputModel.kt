package online.k0ras1k.pulse.data.models.inout.output

import kotlinx.serialization.Serializable

@Serializable
data class PostOutputModel (

    val id: String,
    val content: String,
    val author: String,
    val tags: MutableList<String>,
    val createdAt: String,
    val likesCount: Int,
    val dislikesCount: Int

)