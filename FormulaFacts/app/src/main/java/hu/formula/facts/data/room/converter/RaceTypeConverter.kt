package hu.formula.facts.data.room.converter

import androidx.room.TypeConverter
import hu.formula.facts.data.util.RaceType

object RaceTypeConverter {
    @TypeConverter
    fun asString(value: RaceType): String = value.name

    @TypeConverter
    fun asRaceType(value: String): RaceType = RaceType.valueOf(value)
}