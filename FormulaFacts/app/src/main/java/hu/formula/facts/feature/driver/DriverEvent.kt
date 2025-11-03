package hu.formula.facts.feature.driver

sealed class DriverEvent {
    data class LoadStandings(val season: Int) : DriverEvent()
    data class ExpandCard(val season: Int) : DriverEvent()
}
