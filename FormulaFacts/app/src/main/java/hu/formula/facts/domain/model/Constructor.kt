package hu.formula.facts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Constructor(
    val id: String,
    val url: String? = null,
    val name: String,
    val nationality: String? = null,
)
