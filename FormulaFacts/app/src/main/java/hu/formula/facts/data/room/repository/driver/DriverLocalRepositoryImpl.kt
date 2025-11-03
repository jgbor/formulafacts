package hu.formula.facts.data.room.repository.driver

import hu.formula.facts.data.room.dao.DriverDao
import hu.formula.facts.data.room.dao.SeasonDao
import hu.formula.facts.data.room.dao.StandingDao
import hu.formula.facts.data.room.entities.DriverSeasonCrossRef
import hu.formula.facts.data.room.entities.DriverStandingConstructorCrossRef
import hu.formula.facts.data.room.entities.toDriverStandingEntity
import hu.formula.facts.data.room.entities.toEntity
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DriverLocalRepositoryImpl @Inject constructor(
    private val driverDao: DriverDao,
    private val seasonDao: SeasonDao,
    private val standingDao: StandingDao,
) : DriverLocalRepository {
    override fun getDriver(id: String): Flow<Driver> {
        return driverDao.getDriverById(id).map { it.toDriver() }
    }

    override suspend fun saveDriver(driver: Driver) {
        driverDao.upsertDriver(driver.toEntity())
    }

    override fun getSeasonsOfDriver(id: String): Flow<List<Season>> {
        return driverDao.getSeasonsByDriver(id).map { it.seasons.map { it.toSeason() } }
    }

    override suspend fun saveSeasonsOfDriver(driver: Driver, seasons: List<Season>) {
        driverDao.upsertDriver(driver.toEntity())
        seasons.forEach { season ->
            seasonDao.upsertSeason(season.toEntity())
            seasonDao.upsertDriverSeasonCrossRef(
                DriverSeasonCrossRef(
                    driverId = driver.id,
                    year = season.year
                )
            )
        }
    }

    override suspend fun getStandingOfDriver(
        year: Int,
        id: String
    ): Flow<Standing> {
        return standingDao.getDriverStandingById(year, id).map { it.toStanding() }
    }

    override suspend fun saveStandingOfDriver(
        year: Int,
        standing: Standing
    ) {
        standing.driver?.toEntity()?.let {
            driverDao.upsertDriver(it)
            standingDao.upsertDriverStanding(standing.toDriverStandingEntity(year))
            standing.constructors?.forEach { constructor ->
                standingDao.upsertDriverStandingConstructorCrossRef(
                    DriverStandingConstructorCrossRef(
                        year = year,
                        position = standing.position,
                        constructorId = constructor.id
                    )
                )
            }
        }
    }
}
