package hu.formula.facts.feature.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.formula.facts.data.datasource.FormulaOneRepository
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.Season
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(
    private val repository: FormulaOneRepository
) : ViewModel() {
    private val _state = MutableStateFlow<F1DrawerState>(F1DrawerState.Closed)
    val state = _state.asStateFlow()

    fun onEvent(event: F1DrawerEvent) {
        when (event) {
            is F1DrawerEvent.OpenDrawer -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { F1DrawerState.Loading }
            val seasonCall = async { runCatching { repository.getAllSeasons() }.getOrNull() }
            val driverCall = async { runCatching {  repository.getDriversOfSeason() }.getOrNull() }
            val teamCall = async { runCatching { repository.getConstructorsOfSeason() }.getOrNull() }
            val results = awaitAll(seasonCall, driverCall, teamCall)

            val seasons = results[0]?.mapNotNull { it as? Season }
            val drivers = results[1]?.mapNotNull { it as? Driver }
            val teams   = results[2]?.mapNotNull { it as? Constructor }

            _state.update { F1DrawerState.Opened(seasons, drivers, teams) }
        }
    }
}
