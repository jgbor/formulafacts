package hu.formula.facts.feature.grand_prix

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
import hu.formula.facts.feature.base.ViewModelBase
import hu.formula.facts.navigation.CustomNavType
import hu.formula.facts.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class GrandPrixViewModel @Inject constructor(
    private val repository: FormulaOneRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelBase() {
    private val _state = MutableStateFlow<GrandPrixState>(GrandPrixState.Loading)
    val state = _state.asStateFlow()

    private val args = savedStateHandle.toRoute<Routes.GrandPrixRoute>(
        typeMap = mapOf(typeOf<GrandPrix>() to CustomNavType.GrandPrixType)
    )

    init {
        Firebase.analytics.logEvent(
            FirebaseAnalytics.Event.VIEW_ITEM,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, args.gp.name)
                putString(FirebaseAnalytics.Param.ITEM_NAME, args.gp.name)
                putString(FirebaseAnalytics.Param.CONTENT_TYPE, "grand_prix")
            }
        )
        loadData()
    }

    override fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { GrandPrixState.Loading }
            val gp = args.gp

            val results =
                runCatching { repository.getResultsOfGrandPrix(gp.season, gp.round) }.getOrNull()

            _state.update {
                GrandPrixState.Success(
                    gp.copy(
                        raceResults = results?.raceResults,
                        qualifyingResults = results?.qualifyingResults,
                        sprintResults = results?.sprintResults,
                    )
                )
            }
        }
    }
}
