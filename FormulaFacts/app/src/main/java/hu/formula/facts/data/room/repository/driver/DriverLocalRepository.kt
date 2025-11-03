package hu.formula.facts.data.room.repository.driver

import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow

interface DriverLocalRepository {
    fun getDriver(id: String): Flow<Driver>

    suspend fun saveDriver(driver: Driver)

    fun getSeasonsOfDriver(id: String): Flow<List<Season>>

    suspend fun saveSeasonsOfDriver(driver: Driver, seasons: List<Season>)

    suspend fun getStandingOfDriver(year: Int, id: String): Flow<Standing>

    suspend fun saveStandingOfDriver(year: Int, standing: Standing)
}
