package hu.formula.facts.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.util.SessionType
import hu.formula.facts.feature.base.ScreenBase
import hu.formula.facts.feature.drawer.F1Drawer
import hu.formula.facts.ui.components.NotificationDropdownMenu
import hu.formula.facts.ui.components.StandingsPager
import hu.formula.facts.ui.components.cards.NextGpCard
import hu.formula.facts.ui.components.cards.PreviousGpCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeasonClick: (year: Int, initialPage: Int) -> Unit,
    onDriverClick: (Driver) -> Unit,
    onConstructorClick: (Constructor) -> Unit,
    onGpClick: (GrandPrix) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ScreenBase(
        viewModel = viewModel,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    if (state is HomeState.Success) {
                        if (drawerState.isClosed) {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Icon(imageVector = Icons.Rounded.Menu, null)
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.close() }
                                }
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Rounded.MenuOpen, null)
                            }
                        }
                    }
                },
                actions = {
                    if (state is HomeState.Success) {
                        var expanded by remember { mutableStateOf(false) }
                        val currentState = state as HomeState.Success
                        IconButton(
                            onClick = { expanded = !expanded },
                        ) {
                            Icon(imageVector = Icons.Rounded.Notifications, null)
                        }
                        NotificationDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            isRaceEnabled = currentState.raceNotification,
                            onRaceChange = {
                                viewModel.onEvent(
                                    HomeEvent.ChangeNotification(
                                        SessionType.RACE,
                                        it
                                    )
                                )
                            },
                            isQualifyingEnabled = currentState.qualifyingNotification,
                            onQualifyingChange = {
                                viewModel.onEvent(
                                    HomeEvent.ChangeNotification(
                                        SessionType.QUALIFYING,
                                        it
                                    )
                                )
                            },
                            isPracticeEnabled = currentState.practiceNotification,
                            onPracticeChange = {
                                viewModel.onEvent(
                                    HomeEvent.ChangeNotification(
                                        SessionType.PRACTICE,
                                        it
                                    )
                                )
                            }
                        )
                    }
                }
            )
        }
    ) {
        when (val currentState = state) {
            is HomeState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeState.Success -> {
                F1Drawer(
                    drawerState = drawerState,
                    onSeasonClick = { year, initialPage ->
                        scope.launch { drawerState.close() }
                        onSeasonClick(year, initialPage)
                    },
                    onDriverClick = { driver ->
                        scope.launch { drawerState.close() }
                        onDriverClick(driver)
                    },
                    onTeamClick = { constructor ->
                        scope.launch { drawerState.close() }
                        onConstructorClick(constructor)
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = dimensionResource(R.dimen.page_padding),
                                start = dimensionResource(R.dimen.page_padding),
                                end = dimensionResource(R.dimen.page_padding),
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NextGpCard(
                            grandPrix = currentState.nextGp,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                currentState.nextGp?.let { gp ->
                                    onGpClick(gp)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_padding)))

                        PreviousGpCard(
                            grandPrix = currentState.previousGp,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                currentState.previousGp?.let { gp ->
                                    onGpClick(gp)
                                }
                            }
                        )

                        if (currentState.nextGp != null || currentState.previousGp != null) {
                            Button(
                                onClick = {
                                    currentState.nextGp?.let { gp ->
                                        onSeasonClick(gp.season, 0)
                                    } ?: currentState.previousGp?.let { gp ->
                                        onSeasonClick(gp.season, 0)
                                    }
                                },
                                modifier = Modifier.padding(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_shape)),
                            ) {
                                Text(stringResource(R.string.current_season))
                            }
                        }

                        StandingsPager(
                            driverStandings = currentState.driverStandings,
                            constructorStandings = currentState.constructorStandings,
                            modifier = Modifier.fillMaxWidth(),
                            onDriverClick = onDriverClick,
                            onConstructorClick = onConstructorClick,
                        )
                    }
                }
            }
        }
    }
}
