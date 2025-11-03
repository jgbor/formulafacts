package hu.formula.facts.network.model.standings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StandingsTable(
    val season: Int,
    @SerialName("StandingsLists")
    val standingsLists: List<StandingsListDto>,
)
