package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.formula.facts.data.util.StandingKey
import hu.formula.facts.domain.model.Standing

@Entity(tableName = "cons_standings")
data class ConstructorStandingEntity(
    @PrimaryKey val key: StandingKey,
    val year: Int,
    val position: Int,
    val positionText: String,
    val points: Float,
    val wins: Int,
    val consId: String,
)

data class ConstructorInStandings(
    @Embedded val standing: ConstructorStandingEntity,
    @Relation(
        parentColumn = "consId",
        entityColumn = "constructorId",
    )
    val constructor: ConstructorEntity,
) {
    fun toStanding() = Standing(
        position = standing.position,
        positionText = standing.positionText,
        points = standing.points,
        wins = standing.wins,
        constructor = constructor.toConstructor(),
    )
}

fun Standing.toConstructorStandingEntity(year: Int) = ConstructorStandingEntity(
    key = StandingKey(year, position),
    year = year,
    position = position,
    positionText = positionText,
    points = points,
    wins = wins,
    consId = constructor?.id ?: throw IllegalArgumentException("Constructor ID cannot be null"),
)
