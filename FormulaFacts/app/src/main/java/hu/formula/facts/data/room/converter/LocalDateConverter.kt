package hu.formula.facts.data.room.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

object LocalDateConverter {
    @TypeConverter
    fun asString(date: LocalDate): String = date.toString()

    @TypeConverter
    fun asLocalDateTime(string: String): LocalDate = LocalDate.parse(string)
}