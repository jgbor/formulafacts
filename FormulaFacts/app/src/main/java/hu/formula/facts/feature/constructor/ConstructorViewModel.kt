package hu.formula.facts.feature.constructor

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.domain.model.Constructor
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
class ConstructorViewModel @Inject constructor(
    private val repository: FormulaOneRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelBase() {

    private val _state = MutableStateFlow<ConstructorState>(ConstructorState.Loading)
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ConstructorUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val args = savedStateHandle.toRoute<Routes.ConstructorRoute>(
        typeMap = mapOf(typeOf<Constructor>() to CustomNavType.ConstructorType)
    )

    init {
        Firebase.analytics.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, args.constructor.id)
                putString(FirebaseAnalytics.Param.ITEM_NAME, args.constructor.name)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "constructor")
            }
        )
        loadData()
    }

    fun onEvent(event: ConstructorEvent) {
        when (event) {
            is ConstructorEvent.ExpandCard -> loadRacesOfSeason(event.season)
            is ConstructorEvent.LoadStandings -> loadStandings(event.season)
        }
    }

    override fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { ConstructorState.Loading }
            val constructor = args.constructor
            val seasons =
                runCatching { repository.getSeasonsOfConstructor(constructor) }.getOrNull()
                    ?.reversed()

            _state.update { ConstructorState.Success(constructor, seasons) }
        }
    }

    private fun loadStandings(year: Int) {
        if (state.value !is ConstructorState.Success) return
        val currentState = state.value as ConstructorState.Success
        viewModelScope.launch(Dispatchers.IO) {
            if (currentState.standings[year] != null) {
                _uiEvent.emit(ConstructorUiEvent.LoadedStanding(year))
                return@launch
            }

            val standingInYear = runCatching {
                repository.getConstructorStandings(year).firstOrNull {
                    it.constructor?.id == currentState.constructor.id
                }
            }.getOrNull()

            _state.update {
                (it as ConstructorState.Success).copy(
                    standings = it.standings.toMutableMap().apply {
                        set(year, standingInYear)
                    }
                )
            }
            _uiEvent.emit(ConstructorUiEvent.LoadedStanding(year))
        }
    }

    private fun loadRacesOfSeason(year: Int) {
        if (state.value !is ConstructorState.Success) return
        val currentState = state.value as ConstructorState.Success
        viewModelScope.launch(Dispatchers.IO) {

            val results = awaitAll(
                async { runCatching { repository.getRacesOfSeason(year) }.getOrNull() },
                async {
                    runCatching {
                        repository.getRaceResultsOfSeason(
                            year,
                            constructorId = currentState.constructor.id
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
                (it as ConstructorState.Success).copy(
                    raceResults = resultsOfYear
                )
            }
            _uiEvent.emit(ConstructorUiEvent.LoadedRaceResults(year))
        }
    }
}
