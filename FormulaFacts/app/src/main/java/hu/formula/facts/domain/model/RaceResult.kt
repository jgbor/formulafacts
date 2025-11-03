package hu.formula.facts.domain.model

data class RaceResult(
    val position: Int,
    val positionText: String,
    val driver: Driver,
    val constructor: Constructor? = null,
    val startPosition: Int? = null,
    val raceTime: String? = null,
    val points: Float? = null,
    val status: String? = null,
)
