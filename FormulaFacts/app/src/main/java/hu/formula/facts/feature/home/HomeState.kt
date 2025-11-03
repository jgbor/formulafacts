package hu.formula.facts.feature.home

import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Standing

sealed class HomeState {
    data object Loading : HomeState()
    data class Success(
        val nextGp: GrandPrix?,
        val previousGp: GrandPrix?,
        val driverStandings: List<Standing>?,
        val constructorStandings: List<Standing>?,
        val raceNotification: Boolean,
        val practiceNotification: Boolean,
        val qualifyingNotification: Boolean,
    ) : HomeState()
}