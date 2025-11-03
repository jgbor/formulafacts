package hu.formula.facts.data.room.converter

import androidx.room.TypeConverter
import hu.formula.facts.data.util.StandingKey

object StandingKeyConverter {
    @TypeConverter
    fun asString(value: StandingKey): String = value.value

    @TypeConverter
    fun asStandingKey(value: String): StandingKey = StandingKey.parse(value)
}