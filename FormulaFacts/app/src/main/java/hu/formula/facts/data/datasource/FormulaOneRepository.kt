package hu.formula.facts.data.datasource

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing

interface FormulaOneRepository {
    suspend fun getNextGrandPrix(): GrandPrix?

    suspend fun getPreviousGrandPrix(): GrandPrix

    suspend fun getDriverStandings(year: Int? = null): List<Standing>

    suspend fun getConstructorStandings(year: Int? = null): List<Standing>

    suspend fun getRacesOfSeason(year: Int? = null): List<GrandPrix>

    suspend fun getRaceResultsOfSeason(
        year: Int? = null,
        driverId: String? = null,
        constructorId: String? = null
    ): List<GrandPrix>?

    suspend fun getAllSeasons(): List<Season>

    suspend fun getDriversOfSeason(year: Int? = null): List<Driver>

    suspend fun getConstructorsOfSeason(year: Int? = null): List<Constructor>

    suspend fun getResultsOfGrandPrix(
        year: Int,
        round: Int,
        driverId: String? = null,
        constructorId: String? = null
    ): GrandPrix?

    suspend fun getSeasonsOfDriver(driver: Driver): List<Season>

    suspend fun getSeasonsOfConstructor(constructor: Constructor): List<Season>
}
