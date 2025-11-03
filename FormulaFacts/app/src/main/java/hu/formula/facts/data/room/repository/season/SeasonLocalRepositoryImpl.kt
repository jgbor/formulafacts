package hu.formula.facts.data.room.repository.season

import hu.formula.facts.data.room.dao.ConstructorDao
import hu.formula.facts.data.room.dao.DriverDao
import hu.formula.facts.data.room.dao.RaceDao
import hu.formula.facts.data.room.dao.SeasonDao
import hu.formula.facts.data.room.dao.StandingDao
import hu.formula.facts.data.room.entities.ConstructorSeasonCrossRef
import hu.formula.facts.data.room.entities.DriverSeasonCrossRef
import hu.formula.facts.data.room.entities.DriverStandingConstructorCrossRef
import hu.formula.facts.data.room.entities.RaceRes
import hu.formula.facts.data.room.entities.toConstructorStandingEntity
import hu.formula.facts.data.room.entities.toDriverStandingEntity
import hu.formula.facts.data.room.entities.toEntity
import hu.formula.facts.data.room.entities.toRaceResultEntity
import hu.formula.facts.data.util.RaceType
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeasonLocalRepositoryImpl @Inject constructor(
    private val seasonDao: SeasonDao,
    private val driverDao: DriverDao,
    private val constructorDao: ConstructorDao,
    private val standingDao: StandingDao,
    private val raceDao: RaceDao
) : SeasonLocalRepository {
    private suspend fun List<RaceRes>.combineToGrandPrixList(): List<GrandPrix> {
        val gps = mutableMapOf<String, GrandPrix>()
        this.forEach { res ->
            val gp =
                gps.values.find { res.result.year == it.season && res.result.round == it.round }
            if (gp == null) {
                val addGp = raceDao.getRaceById(res.result.year, res.result.round).first()
                gps[addGp.name] = addGp.toGrandPrix().copy(
                    raceResults = listOf(res.toRaceResult())
                )
            } else {
                val raceResults = gp.raceResults?.toMutableList() ?: mutableListOf()
                raceResults.add(res.toRaceResult())
                gps[gp.name] = gp.copy(raceResults = raceResults)
            }
        }

        return gps.values.toList()
    }

    override fun getAllSeasons(): Flow<List<Season>> {
        return seasonDao.getAllSeasons().map { it.map { it.toSeason() } }
    }

    override suspend fun saveSeasons(seasons: List<Season>) {
        seasons.forEach { season ->
            seasonDao.upsertSeason(season.toEntity())
        }
    }

    override fun getDriversBySeason(season: Int): Flow<List<Driver>> {
        return seasonDao.getDriversBySeason(season).map { it.drivers.map { it.toDriver() } }
    }

    override fun getConstructorsBySeason(season: Int): Flow<List<Constructor>> {
        return seasonDao.getConstructorsBySeason(season)
            .map { it.constructors.map { it.toConstructor() } }
    }

    override suspend fun saveParticipantsForSeason(
        season: Season,
        drivers: List<Driver>?,
        constructors: List<Constructor>?
    ) {
        seasonDao.upsertSeason(season.toEntity())

        drivers?.forEach { driver ->
            driverDao.upsertDriver(driver.toEntity())
            seasonDao.upsertDriverSeasonCrossRef(
                DriverSeasonCrossRef(
                    driverId = driver.id,
                    year = season.year
                )
            )
        }

        constructors?.forEach { constructor ->
            constructorDao.upsertConstructor(constructor.toEntity())
            seasonDao.upsertConstructorSeasonCrossRef(
                ConstructorSeasonCrossRef(
                    constructorId = constructor.id,
                    year = season.year
                )
            )
        }
    }

    override fun getConstructorStandings(year: Int): Flow<List<Standing>> {
        return standingDao.getConstructorStandings(year).map { it.map { it.toStanding() } }
    }

    override suspend fun saveConstructorStandings(year: Int, standings: List<Standing>) {
        standings.forEach { standing ->
            standing.constructor?.toEntity()?.let { constructorDao.upsertConstructor(it) }
            standingDao.upsertConstructorStanding(standing.toConstructorStandingEntity(year))
        }
    }

    override fun getDriverStandings(year: Int): Flow<List<Standing>> {
        return standingDao.getDriverStandings(year).map { it.map { it.toStanding() } }
    }

    override suspend fun saveDriverStandings(year: Int, standings: List<Standing>) {
        standings.forEach { standing ->
            standing.driver?.toEntity()?.let { driverDao.upsertDriver(it) }
            standingDao.upsertDriverStanding(standing.toDriverStandingEntity(year))
            standing.constructors?.forEach { constructor ->
                constructorDao.upsertConstructor(constructor.toEntity())
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

    override fun getResultsOfSeason(
        year: Int,
        driverId: String?,
        constructorId: String?
    ): Flow<List<GrandPrix>> {
        return when {
            driverId != null -> seasonDao.getRaceResultsOfDriverInSeason(
                year,
                RaceType.GP,
                driverId
            )
                .map { results ->
                    results.combineToGrandPrixList()
                }

            constructorId != null -> seasonDao.getRaceResultsOfConstructorInSeason(
                year,
                RaceType.GP,
                constructorId
            )
                .map { results ->
                    results.combineToGrandPrixList()
                }

            else -> seasonDao.getRaceResultsOfSeason(year, RaceType.GP).map { results ->
                results.combineToGrandPrixList()
            }
        }
    }

    override suspend fun saveResultsOfSeason(
        year: Int,
        results: List<GrandPrix>?
    ) {
        results?.forEach { gp ->
            raceDao.upsertRace(gp.toEntity())
            raceDao.upsertRace(gp.toEntity())
            gp.raceResults?.forEach {
                driverDao.upsertDriver(it.driver.toEntity())
                it.constructor?.let { constructorDao.upsertConstructor(it.toEntity()) }
                raceDao.upsertRaceResult(
                    it.toRaceResultEntity(gp.season, gp.round, RaceType.GP)
                )
            }
        }
    }
}