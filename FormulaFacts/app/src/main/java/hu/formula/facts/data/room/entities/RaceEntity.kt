package hu.formula.facts.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import hu.formula.facts.data.room.entities.RaceEntity.Companion.CircuitEntity
import hu.formula.facts.domain.model.Circuit
import hu.formula.facts.domain.model.GrandPrix
import kotlinx.datetime.Instant

@Entity(
    tableName = "races",
    primaryKeys = ["season", "round"],
)
data class RaceEntity(
    val season: Int,
    val round: Int,
    val name: String,
    val url: String? = null,
    @Embedded val circuit: CircuitEntity? = null,
    val startTime: Instant,
    val qualifyingTime: Instant? = null,
    val sprintTime: Instant? = null,
    val firstPracticeTime: Instant? = null,
    val secondPracticeTime: Instant? = null,
    val thirdPracticeTime: Instant? = null,
    val sprintQualifyingTime: Instant? = null,
    val sprintShootoutTime: Instant? = null,
) {
    fun toGrandPrix() = GrandPrix(
        season = season,
        round = round,
        name = name,
        url = url,
        circuit = circuit?.toCircuit(),
        startTime = startTime,
        qualifyingTime = qualifyingTime,
        sprintTime = sprintTime,
        firstPracticeTime = firstPracticeTime,
        secondPracticeTime = secondPracticeTime,
        thirdPracticeTime = thirdPracticeTime,
        sprintQualifyingTime = sprintQualifyingTime,
        sprintShootoutTime = sprintShootoutTime,
    )

    companion object {
        data class CircuitEntity(
            val id: String,
            @ColumnInfo(name = "circuit_name") val circuitName: String,
            @ColumnInfo(name = "circuit_url") val circuitUrl: String? = null,
            val country: String? = null,
            val city: String? = null,
        ) {
            fun toCircuit() = Circuit(
                id = id,
                name = circuitName,
                url = circuitUrl,
                country = country,
                city = city,
            )
        }
    }
}

fun Circuit.toEntity() = CircuitEntity(
    id = id,
    circuitName = name,
    circuitUrl = url,
    country = country,
    city = city,
)

fun GrandPrix.toEntity() = RaceEntity(
    season = season,
    round = round,
    name = name,
    url = url,
    circuit = circuit?.toEntity(),
    startTime = startTime,
    qualifyingTime = qualifyingTime,
    sprintTime = sprintTime,
    firstPracticeTime = firstPracticeTime,
    secondPracticeTime = secondPracticeTime,
    thirdPracticeTime = thirdPracticeTime,
    sprintQualifyingTime = sprintQualifyingTime,
    sprintShootoutTime = sprintShootoutTime,
)
