package hu.formula.facts.data.settings

import kotlinx.coroutines.flow.Flow

interface Settings {
    val raceEnabled: Flow<Boolean>
    val qualifyingEnabled: Flow<Boolean>
    val practiceEnabled: Flow<Boolean>
    val isNotificationEnabled: Flow<Boolean>
    val yearOfNotifications: Flow<Int?>

    suspend fun saveRace(value: Boolean)
    suspend fun saveQualifying(value: Boolean)
    suspend fun savePractice(value: Boolean)
    suspend fun saveYear(value: Int)
}
