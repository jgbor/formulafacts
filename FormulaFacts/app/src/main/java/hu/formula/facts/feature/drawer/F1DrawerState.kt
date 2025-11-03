package hu.formula.facts.feature.drawer

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.Season

sealed class F1DrawerState {
    data object Closed : F1DrawerState()
    data object Loading : F1DrawerState()
    data class Opened(
        val seasons: List<Season>?,
        val drivers: List<Driver>?,
        val teams: List<Constructor>?
    ) : F1DrawerState()
}