package hu.formula.facts.network.model.grandPrix

import kotlinx.serialization.Serializable

@Serializable
data class TimeDto(
    val millis: String? = null,
    val time: String? = null,
)
