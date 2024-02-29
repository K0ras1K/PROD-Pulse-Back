package online.k0ras1k.pulse.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import online.k0ras1k.pulse.controller.friends.AuthFriendController
import online.k0ras1k.pulse.controller.posts.AuthPostController
import online.k0ras1k.pulse.controller.users.AuthUserController
import online.k0ras1k.pulse.data.enums.PostReact
import online.k0ras1k.pulse.data.static.ApplicationConstants

fun Application.configureAuthRouting() {

    routing {
        authenticate("auth-jwt") {
            get("${ApplicationConstants.API_ENDPOINT}/me/profile") {
                AuthUserController(call).getMe()
            }
            patch("${ApplicationConstants.API_ENDPOINT}/me/profile") {
                AuthUserController(call).updateMe()
            }

            get("${ApplicationConstants.API_ENDPOINT}/profiles/{login}") {
                AuthUserController(call).getProfile()
            }
            post("${ApplicationConstants.API_ENDPOINT}/me/updatePassword") {
                AuthUserController(call).updatePassword()
            }

            route("${ApplicationConstants.API_ENDPOINT}/friends") {
                post("/remove") {
                    AuthFriendController(call).removeFriend()
                }
                post("/add") {
                    AuthFriendController(call).addFriend()
                }
                get {
                    AuthFriendController(call).getFriends()
                }
            }
            route("${ApplicationConstants.API_ENDPOINT}/posts") {
                post("/new") {
                    AuthPostController(call).createPost()
                }
                get("/{postId}") {
                    AuthPostController(call).selectPost()
                }
                get("/feed/my") {
                    AuthPostController(call).getMyFeed()
                }
                get("/feed/{login}") {
                    AuthPostController(call).getUserFeed()
                }
                post("/{postId}/like") {
                    AuthPostController(call).react(PostReact.LIKE)
                }
                post("/{postId}/dislike") {
                    AuthPostController(call).react(PostReact.DISLIKE)
                }
            }
        }
    }

}