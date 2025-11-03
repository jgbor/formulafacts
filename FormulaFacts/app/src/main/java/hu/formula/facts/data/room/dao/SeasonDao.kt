package hu.formula.facts.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hu.formula.facts.data.room.entities.ConstructorSeasonCrossRef
import hu.formula.facts.data.room.entities.DriverSeasonCrossRef
import hu.formula.facts.data.room.entities.RaceRes
import hu.formula.facts.data.room.entities.SeasonEntity
import hu.formula.facts.data.room.entities.SeasonWithConstructors
import hu.formula.facts.data.room.entities.SeasonWithDrivers
import hu.formula.facts.data.util.RaceType
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Upsert
    suspend fun upsertSeason(season: SeasonEntity)

    @Transaction
    @Query("SELECT * FROM seasons WHERE year = :season")
    fun getDriversBySeason(season: Int): Flow<SeasonWithDrivers>

    @Transaction
    @Query("SELECT * FROM seasons WHERE year = :season")
    fun getConstructorsBySeason(season: Int): Flow<SeasonWithConstructors>

    @Upsert
    suspend fun upsertDriverSeasonCrossRef(driverSeasonCrossRef: DriverSeasonCrossRef)

    @Upsert
    suspend fun upsertConstructorSeasonCrossRef(constructorSeasonCrossRef: ConstructorSeasonCrossRef)

    @Transaction
    @Query("SELECT * FROM race_results WHERE year = :year AND type = :type")
    fun getRaceResultsOfSeason(year: Int, type: RaceType): Flow<List<RaceRes>>

    @Transaction
    @Query("SELECT * FROM race_results WHERE year = :year AND type = :type AND driverId = :driverId")
    fun getRaceResultsOfDriverInSeason(
        year: Int,
        type: RaceType,
        driverId: String
    ): Flow<List<RaceRes>>

    @Transaction
    @Query("SELECT * FROM race_results WHERE year = :year AND type = :type AND consId = :constructorId")
    fun getRaceResultsOfConstructorInSeason(
        year: Int,
        type: RaceType,
        constructorId: String
    ): Flow<List<RaceRes>>
}
