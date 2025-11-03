package hu.formula.facts.feature.driver

sealed class DriverUiEvent {
    data class LoadedStanding(
        val year: Int,
    ) : DriverUiEvent()

    data class LoadedRaceResults(
        val year: Int,
    ) : DriverUiEvent()
}