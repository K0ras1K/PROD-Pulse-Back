package online.k0ras1k.pulse.utils

import kotlin.random.Random

object UsersUtils {

    fun generateComplexUsername(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { Random.nextInt(chars.length) }
            .map(chars::get)
            .joinToString("")
    }

}