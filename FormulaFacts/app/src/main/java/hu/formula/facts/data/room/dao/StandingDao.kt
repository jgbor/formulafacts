package hu.formula.facts.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hu.formula.facts.data.room.entities.ConstructorInStandings
import hu.formula.facts.data.room.entities.ConstructorStandingEntity
import hu.formula.facts.data.room.entities.DriverInStandings
import hu.formula.facts.data.room.entities.DriverStandingConstructorCrossRef
import hu.formula.facts.data.room.entities.DriverStandingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingDao {
    @Transaction
    @Query("SELECT * FROM cons_standings WHERE year = :year")
    fun getConstructorStandings(year: Int): Flow<List<ConstructorInStandings>>

    @Upsert
    suspend fun upsertConstructorStanding(standing: ConstructorStandingEntity)

    @Transaction
    @Query("SELECT * FROM cons_standings WHERE year = :year AND consId = :constructorId")
    fun getConstructorStandingById(year: Int, constructorId: String): Flow<ConstructorInStandings>

    @Transaction
    @Query("SELECT * FROM driver_standings WHERE year = :year")
    fun getDriverStandings(year: Int): Flow<List<DriverInStandings>>

    @Upsert
    suspend fun upsertDriverStanding(standing: DriverStandingEntity)

    @Transaction
    @Query("SELECT * FROM driver_standings WHERE year = :year AND driverId = :driverId")
    fun getDriverStandingById(year: Int, driverId: String): Flow<DriverInStandings>

    @Upsert
    suspend fun upsertDriverStandingConstructorCrossRef(crossRef: DriverStandingConstructorCrossRef)
}