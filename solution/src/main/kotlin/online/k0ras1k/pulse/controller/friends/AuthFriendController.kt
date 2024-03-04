package online.k0ras1k.pulse.controller.friends

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.controller.AbstractController
import online.k0ras1k.pulse.data.database.Friend
import online.k0ras1k.pulse.data.database.User
import online.k0ras1k.pulse.data.models.dto.FriendData
import online.k0ras1k.pulse.data.models.inout.input.FriendInputModel
import online.k0ras1k.pulse.data.models.inout.output.ErrorResponse

class AuthFriendController(call: ApplicationCall): AbstractController(call) {

    suspend fun removeFriend() {
        runBlocking {
            val friend_login = call.receive<FriendInputModel>().login

            val removed = Friend.removeFriend(login, friend_login)
            if (removed) {
               call.respond(HttpStatusCode.OK, mapOf(Pair("status", "ok")))
            } else {
                call.respond(HttpStatusCode.NotFound, "Пользователь не найден у вас в друзьях или не существует")
            }
        }
    }

    suspend fun addFriend() {
        runBlocking {
            val friend_login = call.receive<FriendInputModel>().login

            if (User.selectUserByLogin(friend_login) == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Пользователь с указанным логином не найден"))
                return@runBlocking
            }

            val friend_data = FriendData(
                login,
                friend_login,
                System.currentTimeMillis()
            )

            if (Friend.selectFriend(friend_data) == null) {
                Friend.insertFriend(friend_data)
                call.respond(HttpStatusCode.OK, mapOf(Pair("status", "ok")))
            }

            call.respond(HttpStatusCode.OK, mapOf(Pair("status", "ok")))
        }
    }

    suspend fun getFriends() {
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
            else {
                if (0 >= limit || 50 < limit) {
                    call.respond(HttpStatusCode.BadRequest, "Недопустимое значение лимита")
                    return@runBlocking
                }
            }
            if (offset == null) {
                offset = 0
            }

            val friends = Friend.getFriends(login, limit, offset)
            call.respond(HttpStatusCode.OK, friends)
        }
    }

}