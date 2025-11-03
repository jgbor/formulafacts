package hu.formula.facts.feature.grand_prix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.formula.facts.R
import hu.formula.facts.feature.base.ScreenBase
import hu.formula.facts.ui.components.FormulaAppBar
import hu.formula.facts.ui.components.cards.GpInfoCard
import hu.formula.facts.ui.components.cards.SessionCard

@Composable
fun GrandPrixScreen(
    onNavigateBack: () -> Unit,
    viewModel: GrandPrixViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenBase(
        viewModel = viewModel,
        topBar = {
            FormulaAppBar(
                title = (state as? GrandPrixState.Success)?.grandPrix?.name,
                url = (state as? GrandPrixState.Success)?.grandPrix?.url,
                successState = state is GrandPrixState.Success,
                onNavigateBack = onNavigateBack
            )
        }
    ) {
        when (val state = state) {
            is GrandPrixState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is GrandPrixState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.page_padding)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.sessions_spacing))
                ) {
                    item {
                        GpInfoCard(state.grandPrix)
                    }

                    state.grandPrix.firstPracticeTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.fp1),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    state.grandPrix.secondPracticeTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.fp2),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    state.grandPrix.thirdPracticeTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.fp3),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    state.grandPrix.sprintQualifyingTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.sprint_qualifying),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    state.grandPrix.sprintShootoutTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.sprint_qualifying),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    state.grandPrix.sprintTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.sprint_race),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                                raceRes = state.grandPrix.sprintResults
                            )
                        }
                    }

                    state.grandPrix.qualifyingTime?.let {
                        item {
                            SessionCard(
                                sessionName = stringResource(R.string.qualifying),
                                startTime = it,
                                modifier = Modifier.fillMaxWidth(),
                                qualiRes = state.grandPrix.qualifyingResults
                            )
                        }
                    }

                    item {
                        SessionCard(
                            sessionName = stringResource(R.string.grand_prix),
                            startTime = state.grandPrix.startTime,
                            modifier = Modifier.fillMaxWidth(),
                            raceRes = state.grandPrix.raceResults
                        )
                    }
                }
            }
        }
    }
}
