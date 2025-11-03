package hu.formula.facts.network.model.driver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DriverTable(
    val season: Int? = null,
    val driverId: String? = null,
    @SerialName("Drivers")
    val drivers: List<DriverDto>,
)
