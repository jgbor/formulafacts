package hu.formula.facts.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Driver(
    val id: String,
    val permanentNumber: Int? = null,
    val code: String? = null,
    val url: String? = null,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: LocalDate? = null,
    val nationality: String? = null,
) {
    val fullName: String
        get() = "$givenName $familyName"
}
