package hu.formula.facts.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hu.formula.facts.data.room.entities.DriverEntity
import hu.formula.facts.data.room.entities.DriverWithSeasons
import hu.formula.facts.data.room.entities.QualiResult
import hu.formula.facts.data.room.entities.RaceRes
import hu.formula.facts.data.util.RaceType
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {
    @Query("SELECT * FROM drivers WHERE driverId = :id")
    fun getDriverById(id: String): Flow<DriverEntity>

    @Upsert
    suspend fun upsertDriver(driver: DriverEntity)

    @Transaction
    @Query("SELECT * FROM drivers WHERE driverId = :driverId")
    fun getSeasonsByDriver(driverId: String): Flow<DriverWithSeasons>

    @Transaction
    @Query("SELECT * FROM race_results WHERE year = :year AND round = :round AND driverId = :driverId AND type = :type")
    fun getRaceResultOfDriver(
        year: Int,
        round: Int,
        driverId: String,
        type: RaceType
    ): Flow<RaceRes?>

    @Transaction
    @Query("SELECT * FROM qualifying_results WHERE year = :year AND round = :round AND driverId = :driverId")
    fun getQualifyingResultOfDriver(
        year: Int,
        round: Int,
        driverId: String
    ): Flow<QualiResult?>
}
