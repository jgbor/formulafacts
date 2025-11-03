package hu.formula.facts.network.model.grandPrix

import hu.formula.facts.domain.model.Circuit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CircuitDto(
    val circuitId: String,
    val url: String,
    val circuitName: String,
    @SerialName("Location")
    val location: LocationDto? = null,
) {
    fun toCircuit() = Circuit(
        id = circuitId,
        name = circuitName,
        url = url,
        country = location?.country,
        city = location?.locality,
    )
}
