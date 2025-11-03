package hu.formula.facts.network.model.grandPrix

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaceTable(
    val season: Int? = null,
    @SerialName("Races")
    val races: List<GrandPrixDto>? = null,
)
