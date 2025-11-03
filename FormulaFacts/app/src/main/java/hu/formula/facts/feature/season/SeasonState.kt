package hu.formula.facts.feature.season

import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing

sealed class SeasonState {
    data object Loading : SeasonState()
    data class Success(
        val season: Season?,
        val races: List<GrandPrix>?,
        val driverStandings: List<Standing>?,
        val constructorStandings: List<Standing>?
    ) : SeasonState()
}
