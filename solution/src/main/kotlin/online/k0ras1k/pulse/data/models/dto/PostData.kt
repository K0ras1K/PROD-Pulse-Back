package online.k0ras1k.pulse.data.models.dto

import java.util.*

data class PostData (

    val id: UUID,
    val content: String,
    val author: String,
    val tags: MutableList<String>,
    val createdAt: Long,
    val likesCount: Int,
    val dislikesCount: Int

)