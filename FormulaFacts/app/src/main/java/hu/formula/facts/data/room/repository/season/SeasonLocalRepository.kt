package hu.formula.facts.data.room.repository.season

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow

interface SeasonLocalRepository {
    fun getAllSeasons(): Flow<List<Season>>

    suspend fun saveSeasons(seasons: List<Season>)

    fun getDriversBySeason(season: Int): Flow<List<Driver>>

    fun getConstructorsBySeason(season: Int): Flow<List<Constructor>>

    suspend fun saveParticipantsForSeason(season: Season, drivers: List<Driver>? = null, constructors: List<Constructor>? = null)

    fun getConstructorStandings(year: Int): Flow<List<Standing>>

    suspend fun saveConstructorStandings(year: Int, standings: List<Standing>)

    fun getDriverStandings(year: Int): Flow<List<Standing>>

    suspend fun saveDriverStandings(year: Int, standings: List<Standing>)

    fun getResultsOfSeason(
        year: Int,
        driverId: String? = null,
        constructorId: String? = null,
    ): Flow<List<GrandPrix>>

    suspend fun saveResultsOfSeason(year: Int, results: List<GrandPrix>?)
}