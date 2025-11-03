package hu.formula.facts.domain.model

data class QualifyingResult(
    val position: Int? = null,
    val number: Int,
    val driver: Driver,
    val constructor: Constructor? = null,
    val q1Time: String? = null,
    val q2Time: String? = null,
    val q3Time: String? = null,
)
