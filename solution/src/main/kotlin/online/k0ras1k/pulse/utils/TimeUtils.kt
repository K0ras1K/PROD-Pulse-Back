package online.k0ras1k.pulse.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun convertLongToISO8601(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(instant.atOffset(ZoneOffset.UTC))
    }

}