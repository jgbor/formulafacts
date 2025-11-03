package hu.formula.facts.feature.driver

import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing

sealed class DriverState {
    object Loading : DriverState()
    data class Success(
        val driver: Driver,
        val seasons: List<Season>?,
        val standings: Map<Int, Standing?> = emptyMap(),
        val raceResults: List<GrandPrix>? = null
    ) : DriverState()
}