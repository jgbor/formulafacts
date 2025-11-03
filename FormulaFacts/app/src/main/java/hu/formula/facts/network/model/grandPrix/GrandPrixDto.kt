package hu.formula.facts.network.model.grandPrix

import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.network.util.parseInstant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GrandPrixDto(
    val season: Int,
    val round: Int,
    val url: String? = null,
    val raceName: String,
    @SerialName("Circuit")
    val circuit: CircuitDto? = null,
    val date: String,
    val time: String? = null,
    @SerialName("Results")
    val raceResults: List<RaceResultDto>? = null,
    @SerialName("QualifyingResults")
    val qualifyingResults: List<QualifyingResultDto>? = null,
    @SerialName("SprintResults")
    val sprintResults: List<RaceResultDto>? = null,
    @SerialName("FirstPractice")
    val firstPractice: StartTimeDto? = null,
    @SerialName("SecondPractice")
    val secondPractice: StartTimeDto? = null,
    @SerialName("ThirdPractice")
    val thirdPractice: StartTimeDto? = null,
    @SerialName("Sprint")
    val sprint: StartTimeDto? = null,
    @SerialName("Qualifying")
    val qualifying: StartTimeDto? = null,
    @SerialName("SprintQualifying")
    val sprintQualifying: StartTimeDto? = null,
    @SerialName("SprintShootout")
    val sprintShootout: StartTimeDto? = null,
) {
    fun toGrandPrix() = GrandPrix(
        season = season,
        round = round,
        name = raceName,
        url = url,
        circuit = circuit?.toCircuit(),
        startTime = parseInstant(date, time)!!,
        qualifyingTime = parseInstant(qualifying?.date, qualifying?.time),
        sprintTime = parseInstant(sprint?.date, sprint?.time),
        firstPracticeTime = parseInstant(firstPractice?.date, firstPractice?.time),
        secondPracticeTime = parseInstant(secondPractice?.date, secondPractice?.time),
        thirdPracticeTime = parseInstant(thirdPractice?.date, thirdPractice?.time),
        sprintQualifyingTime = parseInstant(sprintQualifying?.date, sprintQualifying?.time),
        sprintShootoutTime = parseInstant(sprintShootout?.date, sprintShootout?.time),
        raceResults = raceResults?.map { it.toRaceResult() },
        qualifyingResults = qualifyingResults?.map { it.toQualifyingResult() },
        sprintResults = sprintResults?.map { it.toRaceResult() },
    )
}
