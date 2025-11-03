package hu.formula.facts.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsImpl(
    private val context: Context
) : Settings {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val RACE = booleanPreferencesKey("race")
        private val QUALIFYING = booleanPreferencesKey("qualifying")
        private val PRACTICE = booleanPreferencesKey("practice")
        private val YEAR = intPreferencesKey("year")
    }

    override val raceEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[RACE] == true
    }

    override val qualifyingEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[QUALIFYING] == true
    }

    override val practiceEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PRACTICE] == true
    }

    override val isNotificationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[RACE] == true || preferences[QUALIFYING] == true || preferences[PRACTICE] == true
    }
    override val yearOfNotifications: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[YEAR]
    }

    override suspend fun saveRace(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RACE] = value
        }
    }

    override suspend fun saveQualifying(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[QUALIFYING] = value
        }
    }

    override suspend fun savePractice(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PRACTICE] = value
        }
    }

    override suspend fun saveYear(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[YEAR] = value
        }
    }
}
