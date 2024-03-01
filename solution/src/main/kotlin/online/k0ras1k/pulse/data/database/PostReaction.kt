package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.enums.PostReact
import online.k0ras1k.pulse.data.models.dto.PostReactionData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object PostReaction: Table("post_reactions") {

    //==============================================SCHEMA==============================================================
    private val _login = PostReaction.varchar("login", 30)
    private val _post_id = PostReaction.uuid("post_id")
    private val _react_time = PostReaction.long("react_time")
    private val _react = PostReaction.enumerationByName("react", 10, PostReact::class)
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun insertPostReaction(post_reaction_data: PostReactionData) {
        transaction {
            PostReaction.insert {
                it[_login] = post_reaction_data.login
                it[_post_id] = post_reaction_data.post_id
                it[_react_time] = post_reaction_data.react_time
                it[_react] = post_reaction_data.react
            }
            PostReaction.deleteWhere { PostReaction._login.eq(post_reaction_data.login) and
                    PostReaction._react.eq(if (post_reaction_data.react == PostReact.LIKE) PostReact.DISLIKE
                    else PostReact.LIKE)
            }
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    fun countUniqueReactionsForPost(postId: UUID, reaction: PostReact): Int {
        return transaction {
            // Выбираем все реакции определенного типа для данного поста
            val reactions = PostReaction
                .select { (PostReaction._post_id eq postId) and (PostReaction._react eq reaction) }
                .map { it[PostReaction._login] }

            // Создаем множество для хранения уникальных логинов пользователей
            val uniqueLogins = mutableSetOf<String>()

            // Подсчитываем уникальные логины
            for (login in reactions) {
                uniqueLogins.add(login)
            }

            // Возвращаем количество уникальных логинов
            uniqueLogins.size
        }
    }
    //===============================================UTIL===============================================================

}