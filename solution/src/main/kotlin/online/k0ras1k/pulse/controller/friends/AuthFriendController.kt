package online.k0ras1k.pulse.controller.friends

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking
import online.k0ras1k.pulse.controller.AbstractController
import online.k0ras1k.pulse.data.database.Friend
import online.k0ras1k.pulse.data.models.dto.FriendData
import online.k0ras1k.pulse.data.models.inout.input.FriendInputModel

class AuthFriendController(call: ApplicationCall): AbstractController(call) {

    suspend fun removeFriend() {
        runBlocking {
            val friend_login = call.receive<FriendInputModel>().login

            val removed = Friend.removeFriend(login, friend_login)
            if (removed) {
                call.respond(HttpStatusCode.OK, mapOf(Pair("status", "ok")))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Friend not found in your list")
            }
        }
    }

    suspend fun addFriend() {
        runBlocking {
            val friend_login = call.receive<FriendInputModel>().login

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
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 5
            val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

            val friends = Friend.getFriends(login, limit, offset)
            call.respond(HttpStatusCode.OK, friends)
        }
    }

}