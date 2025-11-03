package hu.formula.facts.feature.constructor

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing

sealed class ConstructorState {
    object Loading : ConstructorState()
    data class Success(
        val constructor: Constructor,
        val seasons: List<Season>?,
        val standings: Map<Int, Standing?> = emptyMap(),
        val raceResults: List<GrandPrix>? = null
    ) : ConstructorState()
}