package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.models.dto.PostTagData
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object PostTag: Table("post_tags") {

    //==============================================SCHEMA==============================================================
    private val _post_id = PostTag.uuid("post_id")
    private val _tag = PostTag.varchar("tag", 20)
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun insertPostTag(post_tag_data: PostTagData) {
        transaction {
            PostTag.insert {
                it[_post_id] = post_tag_data.post_id
                it[_tag] = post_tag_data.tag
            }
        }
    }

    fun getTagsForPost(postId: UUID): MutableList<String> {
        return transaction {
            // Выборка всех тегов, связанных с данным постом
            val tags = PostTag.select { PostTag._post_id.eq(postId) }
                .map { it[_tag] } // Получение значений столбца "tag"
            tags.toMutableList()
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    //===============================================UTIL===============================================================

}