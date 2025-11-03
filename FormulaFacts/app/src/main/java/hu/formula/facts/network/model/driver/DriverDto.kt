package hu.formula.facts.network.model.driver

import hu.formula.facts.domain.model.Driver
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DriverDto(
    val driverId: String,
    val permanentNumber: String? = null,
    val code: String? = null,
    val url: String? = null,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: String? = null,
    val nationality: String? = null,
){
    fun toDriver() = Driver(
        id = driverId,
        permanentNumber = permanentNumber?.toIntOrNull(),
        code = code,
        url = url,
        givenName = givenName,
        familyName = familyName,
        dateOfBirth = dateOfBirth?.let { LocalDate.parse(it) },
        nationality = nationality
    )
}
