package hu.formula.facts.feature.constructor

sealed class ConstructorEvent {
    data class LoadStandings(val season: Int) : ConstructorEvent()
    data class ExpandCard(val season: Int) : ConstructorEvent()
}
