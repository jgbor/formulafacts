package hu.formula.facts.feature.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.ui.components.NoData

@Composable
fun F1Drawer(
    drawerState: DrawerState,
    viewModel: DrawerViewModel = hiltViewModel(),
    onDriverClick: (Driver) -> Unit,
    onTeamClick: (Constructor) -> Unit,
    onSeasonClick: (year: Int, initialPage: Int) -> Unit,
    content: @Composable () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var loaded by remember { mutableStateOf(false) }

    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.isOpen && !loaded) {
            loaded = true
            viewModel.onEvent(F1DrawerEvent.OpenDrawer)
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                when (val currentState = state.value) {
                    is F1DrawerState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is F1DrawerState.Closed -> {}
                    is F1DrawerState.Opened -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = dimensionResource(R.dimen.page_padding))
                        ) {
                            val labelStyle = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                            val labelModifier =
                                Modifier.padding(top = dimensionResource(R.dimen.drawer_label_padding))

                            // Seasons
                            Text(
                                stringResource(R.string.seasons),
                                style = labelStyle,
                                modifier = labelModifier
                            )
                            var expandedIndex by remember { mutableIntStateOf(-1) }
                            LazyColumn(
                                modifier = Modifier.weight(3f)
                            ) {
                                currentState.seasons?.let {
                                    itemsIndexed(it.reversed()) { index, season ->
                                        NavigationDrawerItem(
                                            label = {
                                                Text(
                                                    text = season.year.toString(),
                                                    style = MaterialTheme.typography.labelLarge,
                                                )
                                            },
                                            onClick = {
                                                expandedIndex = if (expandedIndex == index) {
                                                    -1
                                                } else {
                                                    index
                                                }
                                            },
                                            badge = {
                                                Icon(
                                                    imageVector = if (expandedIndex == index) {
                                                        Icons.Default.ExpandLess
                                                    } else {
                                                        Icons.Default.ExpandMore
                                                    },
                                                    contentDescription = null
                                                )
                                            },
                                            selected = expandedIndex == index,
                                            shape = RectangleShape
                                        )

                                        if (expandedIndex == index) {
                                            DrawerItem(
                                                text = stringResource(R.string.races),
                                                onClick = {
                                                    onSeasonClick(season.year, 0)
                                                },
                                            )
                                            DrawerItem(
                                                text = stringResource(R.string.standings),
                                                onClick = {
                                                    onSeasonClick(season.year, 1)
                                                },
                                            )
                                        }

                                        if (index != it.lastIndex) {
                                            ItemDivider()
                                        }
                                    }
                                } ?: run {
                                    item {
                                        NoData(Modifier.fillMaxWidth())
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = Color.Black,
                                thickness = dimensionResource(R.dimen.drawer_divider_thickness)
                            )

                            // Teams
                            Text(
                                stringResource(R.string.current_teams),
                                style = labelStyle,
                                modifier = labelModifier
                            )
                            LazyColumn(
                                modifier = Modifier.weight(2f)
                            ) {
                                currentState.teams?.let {
                                    items(it) { constructor ->
                                        DrawerItem(
                                            text = constructor.name,
                                            onClick = {
                                                onTeamClick(constructor)
                                            },
                                        )
                                        if (constructor != it.last()) {
                                            ItemDivider()
                                        }
                                    }
                                } ?: run {
                                    item {
                                        NoData(Modifier.fillMaxWidth())
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = Color.Black,
                                thickness = dimensionResource(R.dimen.drawer_divider_thickness)
                            )

                            // Drivers
                            Text(
                                stringResource(R.string.current_drivers),
                                style = labelStyle,
                                modifier = labelModifier
                            )
                            LazyColumn(
                                modifier = Modifier.weight(2f)
                            ) {
                                currentState.drivers?.let {
                                    items(it) { driver ->
                                        DrawerItem(
                                            text = driver.fullName,
                                            onClick = {
                                                onDriverClick(driver)
                                            },
                                        )
                                        if (driver != it.last()) {
                                            ItemDivider()
                                        }
                                    }
                                } ?: run {
                                    item {
                                        NoData(Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
    ) {
        content()
    }
}

@Composable
private fun ItemDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(
            horizontal = dimensionResource(
                R.dimen.small_padding
            )
        ),
        thickness = Dp.Hairline,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun DrawerItem(
    text: String,
    onClick: () -> Unit,
) {
    NavigationDrawerItem(
        label = {
            Text(
                text,
                style = MaterialTheme.typography.labelSmall,
            )
        },
        onClick = onClick,
        badge = {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null
            )
        },
        selected = false,
        shape = RectangleShape,
        modifier = Modifier.height(dimensionResource(R.dimen.nav_drawer_item_height)),
    )
}
