package hu.formula.facts.network.model.season

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonTable(
    @SerialName("Seasons")
    val seasons: List<SeasonDto>? = null,
)
