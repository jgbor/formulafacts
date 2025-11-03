package hu.formula.facts.network.model

import hu.formula.facts.network.model.constructor.ConstructorTable
import hu.formula.facts.network.model.driver.DriverTable
import hu.formula.facts.network.model.grandPrix.RaceTable
import hu.formula.facts.network.model.season.SeasonTable
import hu.formula.facts.network.model.standings.StandingsTable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JolpicaAnswer(
    @SerialName("MRData")
    val data: MRData
) {
    companion object {
        @Serializable
        data class MRData(
            val xmlns: String? = null,
            val series: String = "f1",
            val url: String,
            val limit: Int,
            val offset: Int,
            val total: Int,
            @SerialName("DriverTable")
            val driverTable: DriverTable? = null,
            @SerialName("ConstructorTable")
            val constructorTable: ConstructorTable? = null,
            @SerialName("RaceTable")
            val raceTable: RaceTable? = null,
            @SerialName("StandingsTable")
            val standingsTable: StandingsTable? = null,
            @SerialName("SeasonTable")
            val seasonTable: SeasonTable? = null,
        )
    }
}
