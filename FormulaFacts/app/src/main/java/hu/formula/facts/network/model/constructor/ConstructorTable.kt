package hu.formula.facts.network.model.constructor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConstructorTable(
    val season: Int? = null,
    val constructorId: String? = null,
    @SerialName("Constructors")
    val constructors: List<ConstructorDto>
)