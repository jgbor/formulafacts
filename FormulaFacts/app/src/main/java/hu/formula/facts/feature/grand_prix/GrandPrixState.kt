package hu.formula.facts.feature.grand_prix

import hu.formula.facts.domain.model.GrandPrix

sealed class GrandPrixState {
    data object Loading : GrandPrixState()
    data class Success(
        val grandPrix: GrandPrix,
    ) : GrandPrixState()
}
