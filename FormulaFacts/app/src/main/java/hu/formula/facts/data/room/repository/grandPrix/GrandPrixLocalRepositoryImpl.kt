package hu.formula.facts.data.room.repository.grandPrix

import hu.formula.facts.data.room.dao.ConstructorDao
import hu.formula.facts.data.room.dao.DriverDao
import hu.formula.facts.data.room.dao.RaceDao
import hu.formula.facts.data.room.entities.toEntity
import hu.formula.facts.data.room.entities.toQualifyingResultEntity
import hu.formula.facts.data.room.entities.toRaceResultEntity
import hu.formula.facts.data.util.RaceType
import hu.formula.facts.domain.model.GrandPrix
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GrandPrixLocalRepositoryImpl @Inject constructor(
    private val raceDao: RaceDao,
    private val driverDao: DriverDao,
    private val constructorDao: ConstructorDao,
) : GrandPrixLocalRepository {
    override fun getRace(season: Int, round: Int): Flow<GrandPrix> {
        return raceDao.getRaceById(season, round).map { it.toGrandPrix() }
    }

    override fun getRacesOfSeason(season: Int): Flow<List<GrandPrix>> {
        return raceDao.getRacesBySeason(season).map { it.map { it.toGrandPrix() } }
    }

    override suspend fun saveRace(race: GrandPrix) {
        raceDao.upsertRace(race.toEntity())
    }

    override suspend fun saveRacesOfSeason(season: List<GrandPrix>) {
        season.forEach { race ->
            raceDao.upsertRace(race.toEntity())
        }
    }

    override fun getResultsOfGP(
        year: Int,
        round: Int
    ): Flow<GrandPrix> {
        return getRace(year, round).map { gp ->
            val qualifying = raceDao.getQualifyingResultsOfRace(year, round).first()
            val sprint = raceDao.getRaceResultsOfRace(year, round, RaceType.SPRINT).first()
            val race = raceDao.getRaceResultsOfRace(year, round, RaceType.GP).first()

            gp.copy(
                qualifyingResults = qualifying.map { it.toQualifyingResult() },
                sprintResults = sprint.map{ it.toRaceResult() },
                raceResults = race.map { it.toRaceResult() }
            )
        }
    }

    override suspend fun saveResultsOfGrandPrix(gp: GrandPrix) {
        raceDao.upsertRace(gp.toEntity())
        gp.raceResults?.forEach {
            driverDao.upsertDriver(it.driver.toEntity())
            it.constructor?.let { constructorDao.upsertConstructor(it.toEntity()) }
            raceDao.upsertRaceResult(
                it.toRaceResultEntity(gp.season, gp.round, RaceType.GP)
            )
        }
        gp.qualifyingResults?.forEach {
            driverDao.upsertDriver(it.driver.toEntity())
            it.constructor?.let { constructorDao.upsertConstructor(it.toEntity()) }
            raceDao.upsertQualifyingResult(
                it.toQualifyingResultEntity(gp.season, gp.round)
            )
        }
        gp.sprintResults?.forEach {
            driverDao.upsertDriver(it.driver.toEntity())
            it.constructor?.let { constructorDao.upsertConstructor(it.toEntity()) }
            raceDao.upsertRaceResult(
                it.toRaceResultEntity(gp.season, gp.round, RaceType.SPRINT)
            )
        }
    }

    override fun getGpResultOfDriver(
        year: Int,
        round: Int,
        driverId: String
    ): Flow<GrandPrix> {
        return raceDao.getRaceById(year, round).map { gp ->
            val race = driverDao.getRaceResultOfDriver(year, round, driverId, RaceType.GP).first()
            val qualifying = driverDao.getQualifyingResultOfDriver(year, round, driverId).first()
            val sprint =
                driverDao.getRaceResultOfDriver(year, round, driverId, RaceType.SPRINT).first()

            gp.toGrandPrix().copy(
                raceResults = race?.let { listOf(it.toRaceResult()) },
                qualifyingResults = qualifying?.let { listOf(it.toQualifyingResult()) },
                sprintResults = sprint?.let { listOf(it.toRaceResult()) }
            )
        }
    }

    override fun getGpResultOfConstructor(
        year: Int,
        round: Int,
        constructorId: String
    ): Flow<GrandPrix> {
        return raceDao.getRaceById(year, round).map { gp ->
            val race =
                constructorDao.getRaceResultOfConstructor(year, round, constructorId, RaceType.GP)
                    .first()
            val qualifying =
                constructorDao.getQualifyingResultOfConstructor(year, round, constructorId).first()
            val sprint = constructorDao.getRaceResultOfConstructor(
                year,
                round,
                constructorId,
                RaceType.SPRINT
            ).first()

            gp.toGrandPrix().copy(
                raceResults = race?.let { listOf(it.toRaceResult()) },
                qualifyingResults = qualifying?.let { listOf(it.toQualifyingResult()) },
                sprintResults = sprint?.let { listOf(it.toRaceResult()) }
            )
        }
    }
}
