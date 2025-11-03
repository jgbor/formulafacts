package hu.formula.facts.network.model.season

import hu.formula.facts.domain.model.Season
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDto(
    val season: Int,
    val url: String? = null,
) {
    fun toSeason() = Season(
        year = season,
        url = url,
    )
}