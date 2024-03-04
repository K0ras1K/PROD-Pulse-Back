package online.k0ras1k.pulse.controller.posts

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.controller.AbstractController
import online.k0ras1k.pulse.data.database.Friend
import online.k0ras1k.pulse.data.database.Post
import online.k0ras1k.pulse.data.database.PostReaction
import online.k0ras1k.pulse.data.database.User
import online.k0ras1k.pulse.data.enums.PostReact
import online.k0ras1k.pulse.data.models.dto.FriendData
import online.k0ras1k.pulse.data.models.dto.PostData
import online.k0ras1k.pulse.data.models.dto.PostReactionData
import online.k0ras1k.pulse.data.models.inout.input.posts.CreatePostInputModel
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse
import java.util.*

class AuthPostController(call: ApplicationCall): AbstractController(call) {

    suspend fun createPost() {
        runBlocking {
            val receive = call.receive<CreatePostInputModel>()

            val post_data = PostData(
                id = UUID.randomUUID(),
                content = receive.content,
                author = login,
                tags = receive.tags,
                createdAt = System.currentTimeMillis(),
                likesCount = 0,
                dislikesCount = 0
            )

            Post.insertPost(post_data)
            call.respond(HttpStatusCode.OK, Post.toOutputModel(Post.selectPostById(post_data.id)!!))
        }
    }

    suspend fun selectPost() {
        runBlocking {
            val target_post_id = UUID.fromString(call.parameters["postId"])

            var accept = false

            val post_data = Post.selectPostById(target_post_id)!!
            val author_data = User.selectUserByLogin(post_data.author)

            accept = if (!author_data!!.isPublic) {
                Friend.selectFriend(FriendData(author_data.login, login, 1)) != null
            } else {
                true
            }

            accept = accept || login == author_data.login

            if (accept) {
                call.respond(HttpStatusCode.OK, Post.toOutputModel(post_data))
            }
            else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Указанный пост не найден либо к нему нет доступа."))
            }
        }
    }

    suspend fun getMyFeed() {
        runBlocking {
            var limit = call.request.queryParameters["limit"]?.toIntOrNull()
            var offset = call.request.queryParameters["offset"]?.toIntOrNull()

            if ((limit == null && call.request.queryParameters["limit"] != null) || (offset == null && call.request.queryParameters["offset"] != null)) {
                call.respond(HttpStatusCode.BadRequest, "Лимит и офсет должны быть Int")
                return@runBlocking
            }

            if (limit == null) {
                limit = 5
            }
            if (offset == null) {
                offset = 0
            }

            call.respond(HttpStatusCode.OK, Post.getMyPostFeed(login, limit, offset))
        }
    }

    suspend fun getUserFeed() {
        runBlocking {
            val target_login = call.parameters["login"]!!

            var limit = call.request.queryParameters["limit"]?.toIntOrNull()
            var offset = call.request.queryParameters["offset"]?.toIntOrNull()

            if ((limit == null && call.request.queryParameters["limit"] != null) || (offset == null && call.request.queryParameters["offset"] != null)) {
                call.respond(HttpStatusCode.BadRequest, "Лимит и офсет должны быть Int")
                return@runBlocking
            }

            if (limit == null) {
                limit = 5
            }
            if (offset == null) {
                offset = 0
            }

            var accept = false

            val author_data = User.selectUserByLogin(target_login)

            accept = if (!author_data!!.isPublic) {
                Friend.selectFriend(FriendData(author_data.login, login, 1)) != null
            } else {
                true
            }

            accept = accept || login == author_data.login

            if (accept) {
                call.respond(HttpStatusCode.OK, Post.getMyPostFeed(target_login, limit, offset))
            }
            else {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь не найден либо к нему нет доступа."))
            }
        }
    }

    suspend fun react(reaction: PostReact) {
        val target_post_id = UUID.fromString(call.parameters["postId"]!!)

        var accept = false

        val post_data = Post.selectPostById(target_post_id)!!
        val author_data = User.selectUserByLogin(post_data.author)

        accept = if (!author_data!!.isPublic) {
            Friend.selectFriend(FriendData(author_data.login, login, 1)) != null
        } else {
            true
        }

        accept = accept || login == author_data.login

        if (accept) {
            val post_reaction = PostReactionData(
                login = login,
                post_id = post_data.id,
                react_time = System.currentTimeMillis(),
                react = reaction
            )
            PostReaction.insertPostReaction(post_reaction)
            call.respond(HttpStatusCode.OK, Post.toOutputModel(Post.selectPostById(post_data.id)!!))
        }
        else {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Указанный пост не найден либо к нему нет доступа."))
        }
    }

}