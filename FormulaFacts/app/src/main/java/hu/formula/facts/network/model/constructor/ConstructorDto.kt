package hu.formula.facts.network.model.constructor

import hu.formula.facts.domain.model.Constructor
import kotlinx.serialization.Serializable

@Serializable
data class ConstructorDto(
    val constructorId: String,
    val url: String? = null,
    val name: String,
    val nationality: String? = null,
) {
    fun toConstructor() = Constructor(
        id = constructorId,
        url = url,
        name = name,
        nationality = nationality
    )
}
