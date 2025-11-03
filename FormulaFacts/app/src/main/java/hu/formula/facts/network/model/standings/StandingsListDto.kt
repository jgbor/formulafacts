package hu.formula.facts.network.model.standings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StandingsListDto(
    val season: Int,
    val round: Int,
    @SerialName("DriverStandings")
    val driverStandings: List<StandingsDto>? = null,
    @SerialName("ConstructorStandings")
    val constructorStandings: List<StandingsDto>? = null,
)
