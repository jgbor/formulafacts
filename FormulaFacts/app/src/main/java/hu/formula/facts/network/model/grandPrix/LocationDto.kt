package hu.formula.facts.network.model.grandPrix

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val lat: String,
    val long: String,
    val locality: String,
    val country: String,
)
