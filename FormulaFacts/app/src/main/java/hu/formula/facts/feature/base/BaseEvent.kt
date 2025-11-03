package hu.formula.facts.feature.base

sealed class BaseEvent {
    data object Refresh : BaseEvent()
}