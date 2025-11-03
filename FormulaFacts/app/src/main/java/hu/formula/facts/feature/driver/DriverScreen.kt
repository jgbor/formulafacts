package hu.formula.facts.feature.driver

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.formula.facts.R
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.feature.base.ScreenBase
import hu.formula.facts.ui.components.FormulaAppBar
import hu.formula.facts.ui.components.NoData
import hu.formula.facts.ui.components.cards.DriverInfoCard
import hu.formula.facts.ui.components.cards.ParticipantSeasonCard
import hu.formula.facts.ui.components.results.DriverRaceResultItem
import hu.formula.facts.ui.util.itemVisible

@Composable
fun DriverScreen(
    onNavigateBack: () -> Unit,
    onRaceClick: (GrandPrix) -> Unit,
    viewModel: DriverViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var expandedIndex by remember { mutableIntStateOf(-1) }

    ScreenBase(
        viewModel = viewModel,
        topBar = {
            FormulaAppBar(
                title = null,
                url = (state as? DriverState.Success)?.driver?.url,
                successState = state is DriverState.Success,
                onNavigateBack = onNavigateBack
            )
        }
    ) {
        when (val state = state) {
            is DriverState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DriverState.Success -> {
                val lazyListState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.page_padding)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.sessions_spacing)),
                    state = lazyListState
                ) {
                    stickyHeader {
                        DriverInfoCard(
                            driver = state.driver,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (state.seasons != null) {
                        itemsIndexed(state.seasons) { index, season ->
                            val itemVisible by lazyListState.itemVisible(index)
                            var loadingStandings by remember { mutableStateOf(false) }
                            var loadingRes by remember { mutableStateOf(false) }

                            LaunchedEffect(itemVisible) {
                                if (itemVisible) {
                                    loadingStandings = true
                                    viewModel.onEvent(DriverEvent.LoadStandings(season.year))
                                }
                            }

                            LaunchedEffect(Unit) {
                                viewModel.uiEvent.collect {
                                    if (it is DriverUiEvent.LoadedStanding && it.year == season.year) {
                                        loadingStandings = false
                                    } else if (it is DriverUiEvent.LoadedRaceResults && it.year == season.year) {
                                        loadingRes = false
                                    }
                                }
                            }

                            ParticipantSeasonCard(
                                season = season,
                                standing = state.standings[season.year],
                                expanded = expandedIndex == index,
                                modifier = Modifier.fillMaxWidth(),
                                loadingSeasonRes = loadingStandings,
                                onExpandClick = {
                                    loadingRes = true
                                    expandedIndex = if (expandedIndex == index) {
                                        -1
                                    } else {
                                        viewModel.onEvent(DriverEvent.ExpandCard(season.year))
                                        index
                                    }
                                }
                            ) {
                                LaunchedEffect(Unit) {
                                    viewModel.uiEvent.collect {
                                        if (it is DriverUiEvent.LoadedStanding && it.year == season.year) {
                                            loadingRes = false
                                        }
                                    }
                                }

                                if (loadingRes) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(dimensionResource(R.dimen.list_padding)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                } else if (state.raceResults.isNullOrEmpty().not()) {
                                    state.raceResults.forEach { race ->
                                        DriverRaceResultItem(
                                            race,
                                            onClick = {
                                                onRaceClick(race)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(dimensionResource(R.dimen.list_padding)),
                                        )
                                    }
                                } else {
                                    NoData()
                                }
                            }
                        }
                    } else item {
                        NoData(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
