package hu.formula.facts.network.model.grandPrix

import kotlinx.serialization.Serializable

@Serializable
data class StartTimeDto(
    val date: String,
    val time: String? = null
)
