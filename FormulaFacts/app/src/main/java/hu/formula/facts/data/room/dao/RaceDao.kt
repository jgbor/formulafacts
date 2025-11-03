package hu.formula.facts.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import hu.formula.facts.data.room.entities.QualiResult
import hu.formula.facts.data.room.entities.QualifyingResultEntity
import hu.formula.facts.data.room.entities.RaceEntity
import hu.formula.facts.data.room.entities.RaceRes
import hu.formula.facts.data.room.entities.RaceResultEntity
import hu.formula.facts.data.util.RaceType
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Query("SELECT * from races WHERE season = :season AND round = :round")
    fun getRaceById(season: Int, round: Int): Flow<RaceEntity>

    @Upsert
    suspend fun upsertRace(race: RaceEntity)

    @Query("SELECT * from races WHERE season = :season")
    fun getRacesBySeason(season: Int): Flow<List<RaceEntity>>

    @Transaction
    @Query("SELECT * from race_results WHERE year = :year AND round = :round AND type = :type")
    fun getRaceResultsOfRace(year: Int, round: Int, type: RaceType): Flow<List<RaceRes>>

    @Transaction
    @Query("SELECT * from qualifying_results WHERE year = :year AND round = :round")
    fun getQualifyingResultsOfRace(year: Int, round: Int): Flow<List<QualiResult>>

    @Upsert
    fun upsertRaceResult(result: RaceResultEntity)

    @Upsert
    fun upsertQualifyingResult(result: QualifyingResultEntity)
}
