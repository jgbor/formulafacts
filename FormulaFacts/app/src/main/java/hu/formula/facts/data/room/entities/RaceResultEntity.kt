package hu.formula.facts.data.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import hu.formula.facts.data.util.RaceType
import hu.formula.facts.domain.model.RaceResult

@Entity(tableName = "race_results", primaryKeys = ["year", "round", "position", "type"])
data class RaceResultEntity(
    val year: Int,
    val round: Int,
    val position: Int,
    val type: RaceType,
    val positionText: String,
    val driverId: String,
    val consId: String? = null,
    val startPosition: Int? = null,
    val raceTime: String? = null,
    val points: Float? = null,
    val status: String? = null,
)

data class RaceRes(
    @Embedded val result: RaceResultEntity,
    @Relation(
        parentColumn = "consId",
        entityColumn = "constructorId",
    )
    val constructor: ConstructorEntity?,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId",
    )
    val driver: DriverEntity,
) {
    fun toRaceResult() = RaceResult(
        position = result.position,
        positionText = result.positionText,
        driver = driver.toDriver(),
        constructor = constructor?.toConstructor(),
        startPosition = result.startPosition,
        raceTime = result.raceTime,
        points = result.points,
        status = result.status,
    )
}

fun RaceResult.toRaceResultEntity(year: Int, round: Int, type: RaceType) = RaceResultEntity(
    year = year,
    round = round,
    position = position,
    type = type,
    positionText = positionText,
    driverId = driver.id,
    consId = constructor?.id,
    startPosition = startPosition,
    raceTime = raceTime,
    points = points,
    status = status,
)
