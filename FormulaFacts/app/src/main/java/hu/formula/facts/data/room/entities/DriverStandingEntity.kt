package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import hu.formula.facts.data.util.StandingKey
import hu.formula.facts.domain.model.Standing

@Entity(tableName = "driver_standings")
data class DriverStandingEntity(
    @PrimaryKey val standingKey: StandingKey,
    val year: Int,
    val position: Int,
    val positionText: String,
    val points: Float,
    val wins: Int,
    val driverId: String
)

@Entity(
    primaryKeys = ["constructorId", "standingKey"],
    indices = [Index("standingKey")],
)
data class DriverStandingConstructorCrossRef(
    val standingKey: StandingKey,
    val constructorId: String,
) {
    constructor(year: Int, position: Int, constructorId: String) : this(
        standingKey = StandingKey(year, position),
        constructorId = constructorId
    )
}

data class DriverInStandings(
    @Embedded val standing: DriverStandingEntity,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId",
    )
    val driver: DriverEntity,
    @Relation(
        parentColumn = "standingKey",
        entityColumn = "constructorId",
        associateBy = Junction(DriverStandingConstructorCrossRef::class)
    )
    val constructors: List<ConstructorEntity>
) {
    fun toStanding() = Standing(
        position = standing.position,
        positionText = standing.positionText,
        points = standing.points,
        wins = standing.wins,
        driver = driver.toDriver(),
        constructors = constructors.map { it.toConstructor() }
    )
}

fun Standing.toDriverStandingEntity(year: Int) = DriverStandingEntity(
    standingKey = StandingKey(year, position),
    year = year,
    position = position,
    positionText = positionText,
    points = points,
    wins = wins,
    driverId = driver?.id ?: throw IllegalArgumentException("Driver ID cannot be null")
)
