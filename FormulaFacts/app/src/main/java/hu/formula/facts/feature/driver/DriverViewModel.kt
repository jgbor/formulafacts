package hu.formula.facts.feature.driver

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.feature.base.ViewModelBase
import hu.formula.facts.navigation.CustomNavType
import hu.formula.facts.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val repository: FormulaOneRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelBase() {
    private val _state = MutableStateFlow<DriverState>(DriverState.Loading)
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DriverUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val args = savedStateHandle.toRoute<Routes.DriverRoute>(
        typeMap = mapOf(typeOf<Driver>() to CustomNavType.DriverType)
    )

    init {
        Firebase.analytics.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, args.driver.id)
                putString(FirebaseAnalytics.Param.ITEM_NAME, args.driver.fullName)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "driver")
            }
        )
        loadData()
    }

    fun onEvent(event: DriverEvent) {
        when (event) {
            is DriverEvent.ExpandCard -> loadRacesOfSeason(event.season)
            is DriverEvent.LoadStandings -> loadStandings(event.season)
        }
    }

    override fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { DriverState.Loading }
            val driver = args.driver
            val seasons =
                runCatching { repository.getSeasonsOfDriver(driver) }.getOrNull()?.reversed()

            _state.update { DriverState.Success(driver, seasons) }
        }
    }

    private fun loadStandings(year: Int) {
        if (state.value !is DriverState.Success) return
        val currentState = state.value as DriverState.Success
        viewModelScope.launch(Dispatchers.IO) {
            if (currentState.standings[year] != null) {
                _uiEvent.emit(DriverUiEvent.LoadedStanding(year))
                return@launch
            }

            val standingInYear = runCatching {
                repository.getDriverStandings(year).firstOrNull {
                    it.driver?.id == currentState.driver.id
                }
            }.getOrNull()

            _state.update {
                (it as DriverState.Success).copy(
                    standings = it.standings.toMutableMap().apply {
                        set(year, standingInYear)
                    }
                )
            }
            _uiEvent.emit(DriverUiEvent.LoadedStanding(year))
        }
    }

    private fun loadRacesOfSeason(year: Int) {
        if (state.value !is DriverState.Success) return
        val currentState = state.value as DriverState.Success
        viewModelScope.launch(Dispatchers.IO) {
            val results = awaitAll(
                async { runCatching { repository.getRacesOfSeason(year) }.getOrNull() },
                async {
                    runCatching {
                        repository.getRaceResultsOfSeason(
                            year,
                            driverId = currentState.driver.id
                        )
                    }.getOrNull()
                },
            )
            val resultsOfYear = results[0]?.map { race ->
                race.copy(
                    raceResults = results[1]?.find {
                        race.season == it.season && race.round == it.round
                    }?.raceResults
                )
            }

            _state.update {
                (it as DriverState.Success).copy(
                    raceResults = resultsOfYear
                )
            }
            _uiEvent.emit(DriverUiEvent.LoadedRaceResults(year))
        }
    }
}
