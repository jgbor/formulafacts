package hu.formula.facts.network.model.grandPrix

import hu.formula.facts.network.model.constructor.ConstructorDto
import hu.formula.facts.network.model.driver.DriverDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QualifyingResultDto(
    val number: Int,
    val position: String? = null,
    @SerialName("Driver")
    val driver: DriverDto,
    @SerialName("Constructor")
    val constructor: ConstructorDto? = null,
    @SerialName("Q1")
    val q1: String? = null,
    @SerialName("Q2")
    val q2: String? = null,
    @SerialName("Q3")
    val q3: String? = null,
) {
    fun toQualifyingResult() = hu.formula.facts.domain.model.QualifyingResult(
        number = number,
        position = position?.toIntOrNull(),
        driver = driver.toDriver(),
        constructor = constructor?.toConstructor(),
        q1Time = q1,
        q2Time = q2,
        q3Time = q3,
    )
}
