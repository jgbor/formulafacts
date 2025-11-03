package hu.formula.facts.feature.season

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.feature.base.ScreenBase
import hu.formula.facts.ui.components.FormulaAppBar
import hu.formula.facts.ui.components.NoData
import hu.formula.facts.ui.components.PagerSwitchButton
import hu.formula.facts.ui.components.StandingsPager
import hu.formula.facts.ui.components.cards.SeasonRaceCard
import kotlinx.coroutines.launch

@Composable
fun SeasonScreen(
    onNavigateBack: () -> Unit,
    onRaceClick: (GrandPrix) -> Unit,
    onConstructorClick: (Constructor) -> Unit,
    onDriverClick: (Driver) -> Unit,
    initialPage: Int = 0,
    viewModel: SeasonViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val pagerState =
        rememberPagerState(pageCount = { 2 }, initialPage = initialPage)

    ScreenBase(
        viewModel = viewModel,
        topBar = {
            FormulaAppBar(
                title = (state as? SeasonState.Success)?.season?.year?.toString(),
                url = (state as? SeasonState.Success)?.season?.url,
                successState = state is SeasonState.Success,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            if (state is SeasonState.Success) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    PagerSwitchButton(
                        onClick = { scope.launch { pagerState.scrollToPage(0) } },
                        text = stringResource(R.string.races),
                        active = pagerState.currentPage == 0,
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        border = false
                    )
                    PagerSwitchButton(
                        onClick = { scope.launch { pagerState.scrollToPage(1) } },
                        text = stringResource(R.string.standings),
                        active = pagerState.currentPage == 1,
                        modifier = Modifier.weight(1f),
                        shape = RectangleShape,
                        border = false
                    )
                }
            }
        },
    ) {
        when (val state = state) {
            is SeasonState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SeasonState.Success -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (state.season != null) {
                        HorizontalPager(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.page_padding)),
                            state = pagerState,
                            verticalAlignment = Alignment.Top,
                            pageSpacing = dimensionResource(R.dimen.pager_padding),
                            userScrollEnabled = false
                        ) { page ->
                            when (page) {
                                0 -> LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(
                                        dimensionResource(id = R.dimen.race_spacing),
                                    )
                                ) {
                                    if (state.races.isNullOrEmpty().not()) {
                                        items(state.races) { race ->
                                            SeasonRaceCard(
                                                race,
                                                onClick = {
                                                    onRaceClick(race)
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                            )
                                        }
                                    } else {
                                        item {
                                            NoData(
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                }

                                1 ->
                                    StandingsPager(
                                        driverStandings = state.driverStandings,
                                        constructorStandings = state.constructorStandings,
                                        modifier = Modifier.fillMaxSize(),
                                        onDriverClick = onDriverClick,
                                        onConstructorClick = onConstructorClick,
                                    )
                            }
                        }
                    } else {
                        NoData(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
