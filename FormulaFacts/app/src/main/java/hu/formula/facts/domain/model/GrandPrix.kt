package hu.formula.facts.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class GrandPrix(
    val season: Int,
    val round: Int,
    val name: String,
    val url: String? = null,
    val circuit: Circuit? = null,
    val startTime: Instant,
    val qualifyingTime: Instant? = null,
    val sprintTime: Instant? = null,
    val firstPracticeTime: Instant? = null,
    val secondPracticeTime: Instant? = null,
    val thirdPracticeTime: Instant? = null,
    val sprintQualifyingTime: Instant? = null,
    val sprintShootoutTime: Instant? = null,
    @Transient
    val raceResults: List<RaceResult>? = null,
    @Transient
    val qualifyingResults: List<QualifyingResult>? = null,
    @Transient
    val sprintResults: List<RaceResult>? = null,
)
