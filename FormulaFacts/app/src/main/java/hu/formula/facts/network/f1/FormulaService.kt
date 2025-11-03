package hu.formula.facts.network.f1

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing

interface FormulaService {

    suspend fun getDriverStandings(year: Int? = null): List<Standing>

    suspend fun getConstructorStandings(year: Int? = null): List<Standing>

    suspend fun getDriverStandingOfDriver(year: Int, driverId: String): Standing?

    suspend fun getConstructorStandingOfConstructor(year: Int, constructorId: String): Standing?

    suspend fun getRacesOfSeason(year: Int? = null): List<GrandPrix>

    suspend fun getSeasons(): List<Season>

    suspend fun getDriverSeasons(driverId: String): List<Season>

    suspend fun getConstructorSeasons(constructorId: String): List<Season>

    suspend fun getDriversInSeason(year: Int? = null): List<Driver>

    suspend fun getConstructorsInSeason(year: Int? = null): List<Constructor>

    suspend fun getRaceResults(
        year: Int? = null,
        round: Int,
        driverId: String? = null,
        constructorId: String? = null
    ): GrandPrix?

    suspend fun getRaceResultsOfSeason(
        year: Int? = null,
        driverId: String? = null,
        constructorId: String? = null
    ): List<GrandPrix>

    suspend fun getQualifyingResults(
        year: Int? = null,
        round: Int,
        driverId: String? = null,
        constructorId: String? = null
    ): GrandPrix?

    suspend fun getSprintResults(
        year: Int? = null,
        round: Int,
        driverId: String? = null,
        constructorId: String? = null
    ): GrandPrix?
}
