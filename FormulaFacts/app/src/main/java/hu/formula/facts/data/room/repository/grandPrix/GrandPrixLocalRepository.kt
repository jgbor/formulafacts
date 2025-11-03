package hu.formula.facts.data.room.repository.grandPrix

import hu.formula.facts.domain.model.GrandPrix
import kotlinx.coroutines.flow.Flow

interface GrandPrixLocalRepository {
    fun getRace(season: Int, round: Int): Flow<GrandPrix>

    fun getRacesOfSeason(season: Int): Flow<List<GrandPrix>>

    suspend fun saveRace(race: GrandPrix)

    suspend fun saveRacesOfSeason(season: List<GrandPrix>)

    fun getResultsOfGP(year: Int, round: Int): Flow<GrandPrix?>

    suspend fun saveResultsOfGrandPrix(gp: GrandPrix)

    fun getGpResultOfDriver(year: Int, round: Int, driverId: String): Flow<GrandPrix>

    fun getGpResultOfConstructor(year: Int, round: Int, constructorId: String): Flow<GrandPrix>
}
