package hu.formula.facts.feature.season

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.feature.base.ViewModelBase
import hu.formula.facts.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel @Inject constructor(
    private val repository: FormulaOneRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelBase() {
    private val _state = MutableStateFlow<SeasonState>(SeasonState.Loading)
    val state = _state.asStateFlow()

    private val args = savedStateHandle.toRoute<Routes.SeasonRoute>()

    init {
        Firebase.analytics.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, args.year.toString())
                putString(FirebaseAnalytics.Param.ITEM_NAME, args.year.toString())
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "season")
            }
        )
        loadData()
    }

    override fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { SeasonState.Loading }

            val year = args.year
            val season = runCatching { repository.getAllSeasons() }.getOrNull()
                ?.firstOrNull { it.year == year }
            val results = awaitAll(
                async { runCatching { repository.getRacesOfSeason(year) }.getOrNull() },
                async { runCatching { repository.getRaceResultsOfSeason(year) }.getOrNull() },
                async { runCatching { repository.getDriverStandings(year) }.getOrNull() },
                async { runCatching { repository.getConstructorStandings(year) }.getOrNull() }
            )
            val raceResults = results[1]?.mapNotNull { it as? GrandPrix }
            val races = results[0]?.mapNotNull { it as? GrandPrix }?.map { race ->
                race.copy(
                    raceResults = raceResults?.find {
                        race.season == it.season && race.round == it.round
                    }?.raceResults
                )
            }
            val driverStandings = results[2]?.mapNotNull { it as? Standing }
            val constructorStandings = results[3]?.mapNotNull { it as? Standing }

            _state.update {
                SeasonState.Success(
                    season,
                    races.takeIf { it?.isNotEmpty() == true },
                    driverStandings.takeIf{ it?.isNotEmpty() == true },
                    constructorStandings.takeIf { it?.isNotEmpty() == true }
                )
            }
        }
    }
}
