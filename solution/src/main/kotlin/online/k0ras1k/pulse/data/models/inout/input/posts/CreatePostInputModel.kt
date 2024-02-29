package online.k0ras1k.pulse.data.models.inout.input.posts

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostInputModel (

    val content: String,
    val tags: MutableList<String>

)