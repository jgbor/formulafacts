package hu.formula.facts.network.model.grandPrix

import hu.formula.facts.domain.model.RaceResult
import hu.formula.facts.network.model.constructor.ConstructorDto
import hu.formula.facts.network.model.driver.DriverDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaceResultDto(
    val number: Int,
    val position: Int,
    val positionText: String,
    val points: String,
    @SerialName("Driver")
    val driver: DriverDto,
    @SerialName("Constructor")
    val constructor: ConstructorDto? = null,
    val grid: String? = null,
    val laps: String? = null,
    val status: String? = null,
    @SerialName("Time")
    val time: TimeDto? = null,
) {
    fun toRaceResult() = RaceResult(
        position = position,
        positionText = positionText,
        driver = driver.toDriver(),
        constructor = constructor?.toConstructor(),
        startPosition = grid?.toIntOrNull(),
        raceTime = time?.time,
        points = points.toFloatOrNull(),
        status = status,
    )
}
