package hu.formula.facts.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hu.formula.facts.data.room.entities.ConstructorEntity
import hu.formula.facts.data.room.entities.ConstructorWithSeasons
import hu.formula.facts.data.room.entities.QualiResult
import hu.formula.facts.data.room.entities.RaceRes
import hu.formula.facts.data.util.RaceType
import kotlinx.coroutines.flow.Flow

@Dao
interface ConstructorDao {
    @Query("SELECT * FROM constructors WHERE constructorId = :id")
    fun getConstructorById(id: String): Flow<ConstructorEntity>

    @Upsert
    suspend fun upsertConstructor(constructor: ConstructorEntity)

    @Transaction
    @Query("SELECT * FROM constructors WHERE constructorId = :constructorId")
    fun getSeasonsByConstructor(constructorId: String): Flow<ConstructorWithSeasons>

    @Transaction
    @Query("SELECT * FROM race_results WHERE year = :year AND round = :round AND consId = :constructorId AND type = :type")
    fun getRaceResultOfConstructor(
        year: Int,
        round: Int,
        constructorId: String,
        type: RaceType
    ): Flow<RaceRes?>

    @Transaction
    @Query("SELECT * FROM qualifying_results WHERE year = :year AND round = :round AND consId = :constructorId")
    fun getQualifyingResultOfConstructor(
        year: Int,
        round: Int,
        constructorId: String
    ): Flow<QualiResult?>
}
