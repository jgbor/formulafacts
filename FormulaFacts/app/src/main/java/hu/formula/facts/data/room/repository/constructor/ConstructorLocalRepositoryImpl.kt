package hu.formula.facts.data.room.repository.constructor

import hu.formula.facts.data.room.dao.ConstructorDao
import hu.formula.facts.data.room.dao.SeasonDao
import hu.formula.facts.data.room.dao.StandingDao
import hu.formula.facts.data.room.entities.ConstructorSeasonCrossRef
import hu.formula.facts.data.room.entities.toConstructorStandingEntity
import hu.formula.facts.data.room.entities.toEntity
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ConstructorLocalRepositoryImpl @Inject constructor(
    private val constructorDao: ConstructorDao,
    private val seasonDao: SeasonDao,
    private val standingDao: StandingDao,
) : ConstructorLocalRepository {
    override fun getConstructor(id: String): Flow<Constructor> {
        return constructorDao.getConstructorById(id).map { it.toConstructor() }
    }

    override suspend fun saveConstructor(constructor: Constructor) {
        constructorDao.upsertConstructor(constructor.toEntity())
    }

    override fun getSeasonsOfConstructor(id: String): Flow<List<Season>> {
        return constructorDao.getSeasonsByConstructor(id)
            .map { it.seasons.map { it.toSeason() } }
    }

    override suspend fun saveSeasonsOfConstructor(constructor: Constructor, seasons: List<Season>) {
        constructorDao.upsertConstructor(constructor.toEntity())
        seasons.forEach { season ->
            seasonDao.upsertSeason(season.toEntity())
            seasonDao.upsertConstructorSeasonCrossRef(
                ConstructorSeasonCrossRef(
                    constructorId = constructor.id,
                    year = season.year
                )
            )
        }
    }

    override fun getStandingOfConstructor(
        year: Int,
        id: String
    ): Flow<Standing> {
        return standingDao.getConstructorStandingById(year, id).map { it.toStanding() }
    }

    override suspend fun saveStandingOfConstructor(
        year: Int,
        standing: Standing
    ) {
        standing.constructor?.toEntity()?.let {
            constructorDao.upsertConstructor(it)
            standingDao.upsertConstructorStanding(standing.toConstructorStandingEntity(year))
        }
    }
}
