package online.k0ras1k.pulse.data.models.dto

import online.k0ras1k.pulse.data.enums.PostReact
import java.util.UUID

data class PostReactionData (

    val login: String,
    val post_id: UUID,
    val react_time: Long,
    val react: PostReact

)