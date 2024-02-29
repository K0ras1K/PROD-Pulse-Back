package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.enums.PostReact
import online.k0ras1k.pulse.data.models.dto.PostData
import online.k0ras1k.pulse.data.models.dto.PostTagData
import online.k0ras1k.pulse.data.models.inout.output.PostOutputModel
import online.k0ras1k.pulse.utils.TimeUtils
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Post: Table("posts") {

    //==============================================SCHEMA==============================================================
    private val _id = Post.uuid("id")
    private val _content = Post.varchar("content", 1000)
    private val _author = Post.varchar("author", 30)
    private val _created_at = Post.long("created_at")
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun insertPost(post_data: PostData) {
        transaction {
            Post.insert {
                it[_id] = post_data.id
                it[_content] = post_data.content
                it[_author] = post_data.author
                it[_created_at] = post_data.createdAt
            }
            for (tag in post_data.tags) {
                PostTag.insertPostTag(PostTagData(
                    post_id = post_data.id,
                    tag = tag
                ))
            }

        }
    }

    fun selectPostById(post_id: UUID): PostData? {
        return try {
            transaction {
                val post_respond = Post.select { Post._id.eq(post_id) }.single()
                val post_likes = PostReaction.countUniqueReactionsForPost(post_id, PostReact.LIKE)
                val post_dislikes = PostReaction.countUniqueReactionsForPost(post_id, PostReact.DISLIKE)
                val post_tags = PostTag.getTagsForPost(post_id)
                PostData(
                    id = post_id,
                    content = post_respond[_content],
                    author = post_respond[_author],
                    tags = post_tags,
                    createdAt = post_respond[_created_at],
                    likesCount = post_likes,
                    dislikesCount = post_dislikes
                )
            }
        }
        catch (exception: Exception) {
            null
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    fun toOutputModel(post_data: PostData): PostOutputModel {
        return PostOutputModel(
            id = post_data.id.toString(),
            content = post_data.content,
            author = post_data.author,
            tags = post_data.tags,
            createdAt = TimeUtils.convertLongToISO8601(post_data.createdAt),
            likesCount = post_data.likesCount,
            dislikesCount = post_data.dislikesCount
        )
    }

    fun getMyPostFeed(login: String, limit: Int, offset: Int): List<PostOutputModel> {
        return transaction {
            val posts = Post.select { Post._author eq login }
                .orderBy(Post._created_at, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map {
                    val postId = it[Post._id]
                    val postLikes = PostReaction.countUniqueReactionsForPost(postId, PostReact.LIKE)
                    val postDislikes = PostReaction.countUniqueReactionsForPost(postId, PostReact.DISLIKE)
                    val postTags = PostTag.getTagsForPost(postId)
                    toOutputModel(PostData(
                        id = postId,
                        content = it[_content],
                        author = it[_author],
                        tags = postTags,
                        createdAt = it[_created_at],
                        likesCount = postLikes,
                        dislikesCount = postDislikes
                    ))
                }
            posts
        }
    }

    //===============================================UTIL===============================================================

}