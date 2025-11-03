package hu.formula.facts.data.room.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

object InstantConverter {
    @TypeConverter
    fun asString(dateTime: Instant): String = dateTime.toString()

    @TypeConverter
    fun asLocalDateTime(string: String): Instant = Instant.parse(string)
}