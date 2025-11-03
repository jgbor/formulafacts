package hu.formula.facts.data.room.repository.constructor

import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import kotlinx.coroutines.flow.Flow

interface ConstructorLocalRepository {
    fun getConstructor(id: String): Flow<Constructor>

    suspend fun saveConstructor(constructor: Constructor)

    fun getSeasonsOfConstructor(id: String): Flow<List<Season>>

    suspend fun saveSeasonsOfConstructor(constructor: Constructor, seasons: List<Season>)

    fun getStandingOfConstructor(year: Int, id: String): Flow<Standing>

    suspend fun saveStandingOfConstructor(year: Int, standing: Standing)
}
