package hu.formula.facts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Circuit(
    val id: String,
    val name: String,
    val url: String? = null,
    val country: String? = null,
    val city: String? = null,
)
