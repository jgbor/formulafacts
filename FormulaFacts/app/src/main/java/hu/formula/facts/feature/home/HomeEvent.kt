package hu.formula.facts.feature.home

import hu.formula.facts.domain.util.SessionType

sealed class HomeEvent {
    data class ChangeNotification(val type: SessionType, val enabled: Boolean) : HomeEvent()
}