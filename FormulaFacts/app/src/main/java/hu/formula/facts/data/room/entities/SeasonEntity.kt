package hu.formula.facts.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.formula.facts.domain.model.Season

@Entity(tableName = "seasons")
data class SeasonEntity(
    @PrimaryKey val year: Int,
    val url: String?,
) {
    fun toSeason() = Season(
        year = year,
        url = url
    )
}

fun Season.toEntity() = SeasonEntity(
    year = year,
    url = url,
)
