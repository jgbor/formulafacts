package hu.formula.facts.feature.home

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.data.settings.Settings
import hu.formula.facts.domain.util.SessionType
import hu.formula.facts.feature.base.ViewModelBase
import hu.formula.facts.notification.scheduler.AlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FormulaOneRepository,
    private val settings: Settings,
    private val notificationAlarmScheduler: AlarmScheduler
) : ViewModelBase() {
    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state = _state.asStateFlow()

    init {
        setNewSeasonAlarms()
        loadData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeNotification -> {
                if (event.enabled) {
                    setAlarms(event.type)
                } else {
                    cancelAlarms(event.type)
                }
            }

        }
    }

    override fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { HomeState.Loading }
            val nextGP = runCatching { repository.getNextGrandPrix() }.getOrNull()
            val previousGP = runCatching { repository.getPreviousGrandPrix() }.getOrNull()
            val driverStandings = runCatching { repository.getDriverStandings() }.getOrNull()
            val constructorStandings =
                runCatching { repository.getConstructorStandings() }.getOrNull()
            val raceNotification = settings.raceEnabled.first()
            val practiceNotification = settings.practiceEnabled.first()
            val qualifyingNotification = settings.qualifyingEnabled.first()
            _state.update {
                HomeState.Success(
                    nextGp = nextGP,
                    previousGp = previousGP,
                    driverStandings = driverStandings,
                    constructorStandings = constructorStandings,
                    raceNotification = raceNotification,
                    practiceNotification = practiceNotification,
                    qualifyingNotification = qualifyingNotification
                )
            }
        }
    }

    private fun setNewSeasonAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            val isNotificationEnabled = settings.isNotificationEnabled.first()
            val year = settings.yearOfNotifications.first()
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            if (isNotificationEnabled.not() || year == today.year) {
                return@launch
            }
            runCatching {
                val season = repository.getRacesOfSeason()
                settings.saveYear(today.year)
                for (race in season) {
                    if (settings.raceEnabled.first()) {
                        notificationAlarmScheduler.setRaceAlarm(race)
                    }
                    if (settings.qualifyingEnabled.first()) {
                        notificationAlarmScheduler.setQualifyingAlarm(race)
                    }
                    if (settings.practiceEnabled.first()) {
                        notificationAlarmScheduler.setPracticeAlarm(race)
                    }
                }
            }
        }
    }

    private fun cancelAlarms(type: SessionType) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val season = repository.getRacesOfSeason()
                for (race in season) {
                    when (type) {
                        SessionType.PRACTICE -> {
                            notificationAlarmScheduler.cancelPracticeAlarm(race)
                            settings.savePractice(false)
                            _state.update {
                                (it as HomeState.Success).copy(
                                    practiceNotification = false
                                )
                            }
                        }

                        SessionType.QUALIFYING -> {
                            notificationAlarmScheduler.cancelQualifyingAlarm(race)
                            settings.saveQualifying(false)
                            _state.update {
                                (it as HomeState.Success).copy(
                                    qualifyingNotification = false
                                )
                            }
                        }

                        SessionType.RACE -> {
                            notificationAlarmScheduler.cancelRaceAlarm(race)
                            settings.saveRace(false)
                            _state.update {
                                (it as HomeState.Success).copy(
                                    raceNotification = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setAlarms(type: SessionType) {
        if (state.value is HomeState.Loading) return
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.analytics.logEvent(
                "SetNotifications",
                Bundle().apply {
                    putString("SessionType", type.name)
                }
            )

            val season = repository.getRacesOfSeason()
            settings.saveYear(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
            )
            for (race in season) {
                when (type) {
                    SessionType.PRACTICE -> {
                        notificationAlarmScheduler.setPracticeAlarm(race)
                        settings.savePractice(true)
                        _state.update {
                            (it as HomeState.Success).copy(
                                practiceNotification = true
                            )
                        }
                    }

                    SessionType.QUALIFYING -> {
                        notificationAlarmScheduler.setQualifyingAlarm(race)
                        settings.saveQualifying(true)
                        _state.update {
                            (it as HomeState.Success).copy(
                                qualifyingNotification = true
                            )
                        }
                    }

                    SessionType.RACE -> {
                        notificationAlarmScheduler.setRaceAlarm(race)
                        settings.saveRace(true)
                        _state.update {
                            (it as HomeState.Success).copy(
                                raceNotification = true
                            )
                        }
                    }
                }
            }
        }
    }
}
