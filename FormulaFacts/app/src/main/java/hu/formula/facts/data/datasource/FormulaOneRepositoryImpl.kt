package hu.formula.facts.data.datasource

import android.content.Context
import hu.formula.facts.connectivity.ConnectionState
import hu.formula.facts.connectivity.observeConnectivityAsFlow
import hu.formula.facts.data.room.repository.constructor.ConstructorLocalRepository
import hu.formula.facts.data.room.repository.driver.DriverLocalRepository
import hu.formula.facts.data.room.repository.grandPrix.GrandPrixLocalRepository
import hu.formula.facts.data.room.repository.season.SeasonLocalRepository
import hu.formula.facts.data.util.NoDataException
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.network.f1.FormulaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import java.time.LocalDate

class FormulaOneRepositoryImpl(
    private val driverLocalRepository: DriverLocalRepository,
    private val constructorLocalRepository: ConstructorLocalRepository,
    private val grandPrixLocalRepository: GrandPrixLocalRepository,
    private val seasonLocalRepository: SeasonLocalRepository,
    private val formulaService: FormulaService,
    private val context: Context
) : FormulaOneRepository {
    private suspend inline fun <reified T> getData(
        networkCall: suspend () -> T,
        localCall: suspend () -> Flow<T>,
        saveLocally: suspend (T) -> Unit,
    ): T {
        val connected = context.observeConnectivityAsFlow().first() == ConnectionState.Available
        try {
            if (connected) {
                val data = try {
                    val networkResponse = networkCall()
                    saveLocally(networkResponse)
                    networkResponse
                } catch (e: Exception) {
                    Timber.e(e, "Network call or caching failed: ${e.message}")
                    localCall().first()
                }
                return data
            }
            return localCall().first()
        } catch (e: NullPointerException) {
            Timber.w("NullPointerException: ${e.message}")
            throw NoDataException(T::class)
        }
    }

    private fun GrandPrix.sortResults(): GrandPrix {
        return this.copy(
            raceResults = this.raceResults?.sortedBy { it.position },
            qualifyingResults = this.qualifyingResults?.sortedBy { it.position },
            sprintResults = this.sprintResults?.sortedBy { it.position }
        )
    }

    private fun List<GrandPrix>.sortSeason(): List<GrandPrix> {
        return this.sortedBy { it.round }.map { it.sortResults() }
    }

    override suspend fun getNextGrandPrix(): GrandPrix? {
        val today = LocalDate.now().toKotlinLocalDate()
        val thisSeason = getData(
            networkCall = { formulaService.getRacesOfSeason() },
            localCall = { grandPrixLocalRepository.getRacesOfSeason(today.year) },
            saveLocally = { currentSeason ->
                grandPrixLocalRepository.saveRacesOfSeason(
                    currentSeason
                )
            }
        )
        return thisSeason.sortedBy { it.startTime }.firstOrNull { grandPrix ->
            grandPrix.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date >= today
        }?.sortResults()
    }

    override suspend fun getPreviousGrandPrix(): GrandPrix {
        val today = LocalDate.now().toKotlinLocalDate()
        val thisSeason = getData(
            networkCall = { formulaService.getRacesOfSeason() },
            localCall = { grandPrixLocalRepository.getRacesOfSeason(today.year) },
            saveLocally = { currentSeason ->
                grandPrixLocalRepository.saveRacesOfSeason(
                    currentSeason
                )
            }
        )
        return thisSeason.sortedByDescending { it.startTime }.firstOrNull { grandPrix ->
            grandPrix.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date < today
        }?.sortResults() ?: run {
            val previousSeason = getData(
                networkCall = { formulaService.getRacesOfSeason(today.year - 1) },
                localCall = { grandPrixLocalRepository.getRacesOfSeason(today.year - 1) },
                saveLocally = { previousSeason ->
                    grandPrixLocalRepository.saveRacesOfSeason(
                        previousSeason
                    )
                }
            )
            previousSeason.maxBy { it.startTime }.sortResults()
        }
    }

    override suspend fun getDriverStandings(year: Int?): List<Standing> {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getDriverStandings(year) },
            localCall = {
                seasonLocalRepository.getDriverStandings(year ?: today.year).map {
                    it.sortedBy { it.position }
                }
            },
            saveLocally = { standings ->
                seasonLocalRepository.saveDriverStandings(
                    year = year ?: today.year, standings
                )
            }
        )
    }

    override suspend fun getConstructorStandings(year: Int?): List<Standing> {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getConstructorStandings(year) },
            localCall = {
                seasonLocalRepository.getConstructorStandings(year ?: today.year).map {
                    it.sortedBy { it.position }
                }
            },
            saveLocally = { standings ->
                seasonLocalRepository.saveConstructorStandings(
                    year = year ?: today.year, standings
                )
            }
        )
    }

    override suspend fun getRacesOfSeason(year: Int?): List<GrandPrix> {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getRacesOfSeason(year) },
            localCall = {
                grandPrixLocalRepository.getRacesOfSeason(year ?: today.year).map {
                    it.sortSeason()
                }
            },
            saveLocally = { races ->
                grandPrixLocalRepository.saveRacesOfSeason(
                    races
                )
            }
        )
    }

    override suspend fun getRaceResultsOfSeason(
        year: Int?,
        driverId: String?,
        constructorId: String?
    ): List<GrandPrix>? {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getRaceResultsOfSeason(year, driverId, constructorId) },
            localCall = {
                seasonLocalRepository.getResultsOfSeason(
                    year ?: today.year,
                    driverId,
                    constructorId
                ).map {
                    it.sortSeason()
                }
            },
            saveLocally = { results ->
                seasonLocalRepository.saveResultsOfSeason(
                    year = year ?: today.year,
                    results
                )
            }
        )
    }

    override suspend fun getAllSeasons(): List<Season> {
        return getData(
            networkCall = { formulaService.getSeasons() },
            localCall = { seasonLocalRepository.getAllSeasons() },
            saveLocally = { seasons ->
                seasonLocalRepository.saveSeasons(seasons)
            }
        )
    }

    override suspend fun getDriversOfSeason(year: Int?): List<Driver> {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getDriversInSeason(year) },
            localCall = { seasonLocalRepository.getDriversBySeason(year ?: today.year) },
            saveLocally = { drivers ->
                val season = formulaService.getSeasons().first { it.year == (year ?: today.year) }
                seasonLocalRepository.saveParticipantsForSeason(season, drivers = drivers)
            }
        )
    }

    override suspend fun getConstructorsOfSeason(year: Int?): List<Constructor> {
        val today = LocalDate.now().toKotlinLocalDate()
        return getData(
            networkCall = { formulaService.getConstructorsInSeason(year) },
            localCall = { seasonLocalRepository.getConstructorsBySeason(year ?: today.year) },
            saveLocally = { constructors ->
                val season = formulaService.getSeasons().first { it.year == (year ?: today.year) }
                seasonLocalRepository.saveParticipantsForSeason(season, constructors = constructors)
            }
        )
    }

    override suspend fun getResultsOfGrandPrix(
        year: Int,
        round: Int,
        driverId: String?,
        constructorId: String?,
    ): GrandPrix? {
        return getData(
            networkCall = {
                val race = formulaService.getRaceResults(year, round, driverId, constructorId)
                val sprint = formulaService.getSprintResults(year, round, driverId, constructorId)
                val qualifying =
                    formulaService.getQualifyingResults(year, round, driverId, constructorId)
                race?.copy(
                    sprintResults = sprint?.sprintResults,
                    qualifyingResults = qualifying?.qualifyingResults
                ) ?: sprint?.copy(
                    qualifyingResults = qualifying?.qualifyingResults
                ) ?: qualifying
            },
            localCall = {
                if (driverId != null) {
                    grandPrixLocalRepository.getGpResultOfDriver(year, round, driverId).map {
                        it.sortResults()
                    }
                } else if (constructorId != null) {
                    grandPrixLocalRepository.getGpResultOfConstructor(year, round, constructorId)
                        .map {
                            it.sortResults()
                        }
                } else {
                    grandPrixLocalRepository.getResultsOfGP(year, round).map {
                        it?.sortResults()
                    }
                }
            },
            saveLocally = { grandPrix ->
                grandPrix?.let { grandPrixLocalRepository.saveResultsOfGrandPrix(it) }
            }
        )
    }

    override suspend fun getSeasonsOfDriver(driver: Driver): List<Season> {
        return getData(
            networkCall = { formulaService.getDriverSeasons(driver.id) },
            localCall = { driverLocalRepository.getSeasonsOfDriver(driver.id) },
            saveLocally = { seasons ->
                driverLocalRepository.saveSeasonsOfDriver(driver, seasons)
            }
        )
    }

    override suspend fun getSeasonsOfConstructor(constructor: Constructor): List<Season> {
        return getData(
            networkCall = { formulaService.getConstructorSeasons(constructor.id) },
            localCall = { constructorLocalRepository.getSeasonsOfConstructor(constructor.id) },
            saveLocally = { seasons ->
                constructorLocalRepository.saveSeasonsOfConstructor(constructor, seasons)
            }
        )
    }
}
