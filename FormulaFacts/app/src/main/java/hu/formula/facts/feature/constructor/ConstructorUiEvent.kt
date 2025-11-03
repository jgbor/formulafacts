package hu.formula.facts.feature.constructor

sealed class ConstructorUiEvent {
    data class LoadedStanding(
        val year: Int,
    ) : ConstructorUiEvent()

    data class LoadedRaceResults(
        val year: Int,
    ) : ConstructorUiEvent()
}