package hu.formula.facts.network.f1

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.network.model.JolpicaAnswer
import hu.formula.facts.network.model.standings.StandingsDto

class JolpicaApi(
    private val retrofit: JolpicaApiRetrofit
) : FormulaService {
    override suspend fun getDriverStandings(year: Int?): List<Standing> {
        val response = getAllItems { offset ->
            year?.let { retrofit.getDriverStandings("$it", offset = offset) }
                ?: retrofit.getDriverStandings(offset = offset)
        }
        return getStandingsFromResponse(response, isDriverStandings = true)
    }

    override suspend fun getConstructorStandings(year: Int?): List<Standing> {
        val response = getAllItems { offset ->
            year?.let { retrofit.getConstructorStandings("$it", offset = offset) }
                ?: retrofit.getConstructorStandings(offset = offset)
        }
        return getStandingsFromResponse(response, isDriverStandings = false)
    }

    override suspend fun getDriverStandingOfDriver(
        year: Int,
        driverId: String
    ): Standing? {
        val response = retrofit.getDriverStandingsOfDriver("$year", driverId)
        return getStandingsFromResponse(listOf(response), isDriverStandings = true).firstOrNull()
    }

    override suspend fun getConstructorStandingOfConstructor(
        year: Int,
        constructorId: String
    ): Standing? {
        val response = retrofit.getConstructorStandingsOfConstructor("$year", constructorId)
        return getStandingsFromResponse(listOf(response), isDriverStandings = false).firstOrNull()
    }

    override suspend fun getRacesOfSeason(year: Int?): List<GrandPrix> {
        val response = getAllItems { offset ->
            year?.let { retrofit.getRaces("$it", offset = offset) }
                ?: retrofit.getRaces(offset = offset)
        }
        return getRacesFromResponse(response)
    }

    override suspend fun getSeasons(): List<Season> {
        val response = getAllItems { offset -> retrofit.getSeasons(offset = offset) }
        return getSeasonsFromResponse(response)
    }

    override suspend fun getDriverSeasons(driverId: String): List<Season> {
        val response =
            getAllItems { offset -> retrofit.getDriverSeasons(driverId, offset = offset) }
        return getSeasonsFromResponse(response)
    }

    override suspend fun getConstructorSeasons(constructorId: String): List<Season> {
        val response =
            getAllItems { offset -> retrofit.getConstructorSeasons(constructorId, offset = offset) }
        return getSeasonsFromResponse(response)
    }

    override suspend fun getDriversInSeason(year: Int?): List<Driver> {
        val response = getAllItems { offset ->
            year?.let { retrofit.getDrivers("$it", offset = offset) }
                ?: retrofit.getDrivers(offset = offset)
        }
        return response.flatMap {
            it.data.driverTable?.drivers?.map { it.toDriver() } ?: emptyList()
        }
    }

    override suspend fun getConstructorsInSeason(year: Int?): List<Constructor> {
        val response = getAllItems { offset ->
            year?.let { retrofit.getConstructors("$it", offset = offset) }
                ?: retrofit.getConstructors(offset = offset)
        }
        return response.flatMap {
            it.data.constructorTable?.constructors?.map { it.toConstructor() } ?: emptyList()
        }
    }

    override suspend fun getRaceResults(
        year: Int?,
        round: Int,
        driverId: String?,
        constructorId: String?
    ): GrandPrix? {
        val response = getAllItems { offset ->
            year?.let {
                if (driverId != null) {
                    retrofit.getDriverRaceResult("$year", "$round", driverId, offset = offset)
                } else if (constructorId != null) {
                    retrofit.getConstructorRaceResult(
                        "$year",
                        "$round",
                        constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getRaceResults("$year", "$round", offset = offset)
                }
            } ?: run {
                if (driverId != null) {
                    retrofit.getDriverRaceResult(
                        round = "$round",
                        driverId = driverId,
                        offset = offset
                    )
                } else if (constructorId != null) {
                    retrofit.getConstructorRaceResult(
                        round = "$round",
                        constructorId = constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getRaceResults(round = "$round", offset = offset)
                }
            }
        }
        return getRacesFromResponse(response).firstOrNull()
    }

    override suspend fun getRaceResultsOfSeason(
        year: Int?,
        driverId: String?,
        constructorId: String?
    ): List<GrandPrix> {
        val response = getAllItems { offset ->
            year?.let {
                if (driverId != null) {
                    retrofit.getDriverRaceResultOfSeason("$year", driverId, offset = offset)
                } else if (constructorId != null) {
                    retrofit.getConstructorRaceResultOfSeason(
                        "$year",
                        constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getRaceResultsOfSeason("$year", offset = offset)
                }
            } ?: run {
                if (driverId != null) {
                    retrofit.getDriverRaceResultOfSeason(
                        driverId = driverId,
                        offset = offset
                    )
                } else if (constructorId != null) {
                    retrofit.getConstructorRaceResultOfSeason(
                        constructorId = constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getRaceResultsOfSeason(offset = offset)
                }
            }
        }
        return getRacesFromResponse(response)
    }

    override suspend fun getQualifyingResults(
        year: Int?,
        round: Int,
        driverId: String?,
        constructorId: String?
    ): GrandPrix? {
        val response = getAllItems { offset ->
            year?.let {
                if (driverId != null) {
                    retrofit.getDriverQualifyingResult("$year", "$round", driverId, offset = offset)
                } else if (constructorId != null) {
                    retrofit.getConstructorQualifyingResult(
                        "$year",
                        "$round",
                        constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getQualifyingResults("$year", "$round", offset = offset)
                }
            } ?: run {
                if (driverId != null) {
                    retrofit.getDriverQualifyingResult(
                        round = "$round",
                        driverId = driverId,
                        offset = offset
                    )
                } else if (constructorId != null) {
                    retrofit.getConstructorQualifyingResult(
                        round = "$round",
                        constructorId = constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getQualifyingResults(round = "$round", offset = offset)
                }
            }
        }
        return getRacesFromResponse(response).firstOrNull()
    }

    override suspend fun getSprintResults(
        year: Int?,
        round: Int,
        driverId: String?,
        constructorId: String?
    ): GrandPrix? {
        val response = getAllItems { offset ->
            year?.let {
                if (driverId != null) {
                    retrofit.getDriverSprintResult("$year", "$round", driverId, offset = offset)
                } else if (constructorId != null) {
                    retrofit.getConstructorSprintResult(
                        "$year",
                        "$round",
                        constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getSprintResults("$year", "$round", offset = offset)
                }
            } ?: run {
                if (driverId != null) {
                    retrofit.getDriverSprintResult(
                        round = "$round",
                        driverId = driverId,
                        offset = offset
                    )
                } else if (constructorId != null) {
                    retrofit.getConstructorSprintResult(
                        round = "$round",
                        constructorId = constructorId,
                        offset = offset
                    )
                } else {
                    retrofit.getSprintResults(round = "$round", offset = offset)
                }
            }
        }
        return getRacesFromResponse(response).firstOrNull()
    }

    private suspend fun getAllItems(networkCall: suspend (offset: Int) -> JolpicaAnswer): List<JolpicaAnswer> {
        val responses = mutableListOf<JolpicaAnswer>()
        var offset = 0
        do {
            val response = networkCall(offset)
            responses.add(response)
            offset = response.data.offset + response.data.limit
        } while (response.data.total > offset)
        return responses
    }

    private fun getStandingsFromResponse(
        response: List<JolpicaAnswer>,
        isDriverStandings: Boolean
    ): List<Standing> {
        val standingsList = mutableListOf<Standing>()
        response.forEach { answer ->
            val standings = if (isDriverStandings) {
                answer.data.standingsTable?.standingsLists?.flatMap {
                    it.driverStandings ?: emptyList<StandingsDto>()
                }
            } else {
                answer.data.standingsTable?.standingsLists?.flatMap {
                    it.constructorStandings ?: emptyList<StandingsDto>()
                }
            }
            standings?.forEachIndexed { index, standing ->
                standingsList.add(standing.toStanding(index + 1))
            }
        }
        return standingsList
    }

    private fun getRacesFromResponse(
        response: List<JolpicaAnswer>,
    ): List<GrandPrix> {
        return response.flatMap {
            it.data.raceTable?.races?.map { it.toGrandPrix() } ?: emptyList()
        }
    }

    private fun getSeasonsFromResponse(
        response: List<JolpicaAnswer>,
    ): List<Season> {
        return response.flatMap {
            it.data.seasonTable?.seasons?.map { it.toSeason() } ?: emptyList()
        }
    }
}
