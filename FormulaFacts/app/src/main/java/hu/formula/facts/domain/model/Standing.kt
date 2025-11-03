package hu.formula.facts.domain.model

data class Standing(
    val position: Int,
    val positionText: String,
    val points: Float,
    val wins: Int,
    val driver: Driver? = null,
    val constructors: List<Constructor>? = null,
    val constructor: Constructor? = null,
)
