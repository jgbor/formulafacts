package hu.formula.facts.network.model.standings

import hu.formula.facts.domain.model.Standing
import hu.formula.facts.network.model.constructor.ConstructorDto
import hu.formula.facts.network.model.driver.DriverDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StandingsDto(
    val position: String? = null,
    val positionText: String,
    val points: String,
    val wins: String,
    @SerialName("Driver")
    val driver: DriverDto? = null,
    @SerialName("Constructors")
    val constructors: List<ConstructorDto>? = null,
    @SerialName("Constructor")
    val constructor: ConstructorDto? = null,
) {
    fun toStanding(pos: Int = 0) = Standing(
        position = position?.toInt() ?: pos,
        positionText = positionText,
        points = points.toFloat(),
        wins = wins.toInt(),
        driver = driver?.toDriver(),
        constructors = constructors?.map { it.toConstructor() },
        constructor = constructor?.toConstructor(),
    )
}
