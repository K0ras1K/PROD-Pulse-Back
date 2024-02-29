package online.k0ras1k.pulse

import online.k0ras1k.pulse.data.database.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Initialization {

    val TABLE_LIST: MutableList<Table> = mutableListOf(
        Country,
        User,
        Friend,
        Post,
        PostTag,
        PostReaction
    )

    fun initialize() {
        transaction {
            for (table in TABLE_LIST) {
                SchemaUtils.create(table)
            }
        }
    }

}