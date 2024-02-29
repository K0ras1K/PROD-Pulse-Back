package online.k0ras1k.pulse.data.database

import online.k0ras1k.pulse.data.models.dto.FriendData
import online.k0ras1k.pulse.data.models.inout.output.FriendOutputModel
import online.k0ras1k.pulse.utils.TimeUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object Friend: Table("friend") {

    //==============================================SCHEMA==============================================================
    private val _login = Friend.varchar("login", 30)
    private val _friend = Friend.varchar("friend", 30)
    private val _added_ad = Friend.long("added_at")
    //==============================================SCHEMA==============================================================


    //===============================================CRUD===============================================================
    fun insertFriend(friend_data: FriendData) {
        transaction {
            Friend.insert {
                it[_login] = friend_data.login
                it[_friend] = friend_data.friend
                it[_added_ad] = System.currentTimeMillis()
            }
        }
    }

    fun selectFriend(friend_data: FriendData): FriendData? {
        return try {
            transaction {
                val respond = Friend.select { Friend._login.eq(friend_data.login) and Friend._friend.eq(friend_data.friend) }.single()
                FriendData(
                    login = respond[_login],
                    friend = respond[_friend],
                    addedAt = respond[_added_ad]
                )
            }
        }
        catch (exception: Exception) {
            null
        }
    }
    //===============================================CRUD===============================================================


    //===============================================UTIL===============================================================
    fun removeFriend(userLogin: String, friendLogin: String): Boolean {
        return transaction {
            Friend.deleteWhere { (Friend._login eq userLogin) and (Friend._friend eq friendLogin) } > 0
        }
    }

    fun getFriends(userLogin: String, limit: Int, offset: Int): List<FriendOutputModel> {
        return transaction {
            Friend.select { Friend._login eq userLogin }
                .limit(limit, offset.toLong())
                .map {
                    FriendOutputModel(
                        login = it[Friend._friend],
                        addedAt = TimeUtils.convertLongToISO8601(it[Friend._added_ad])
                    )
                }
        }
    }
    //===============================================UTIL===============================================================

}