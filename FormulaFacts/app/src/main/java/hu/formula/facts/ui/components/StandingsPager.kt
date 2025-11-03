package hu.formula.facts.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.ui.components.results.StandingListItem
import kotlinx.coroutines.launch

@Composable
fun StandingsPager(
    driverStandings: List<Standing>?,
    constructorStandings: List<Standing>?,
    modifier: Modifier = Modifier,
    onDriverClick: (Driver) -> Unit = {},
    onConstructorClick: (Constructor) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            PagerSwitchButton(
                onClick = { scope.launch { pagerState.scrollToPage(0) } },
                text = stringResource(R.string.drivers),
                active = pagerState.currentPage == 0
            )
            PagerSwitchButton(
                onClick = { scope.launch { pagerState.scrollToPage(1) } },
                text = stringResource(R.string.constructors),
                active = pagerState.currentPage == 1
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.pager_padding)))

        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
            verticalAlignment = Alignment.Top,
            pageSpacing = dimensionResource(R.dimen.pager_padding),
        ) { page ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (page) {
                    0 -> {
                        driverStandings?.let { driverStandings ->
                            items(driverStandings.size) { index ->
                                val standing = driverStandings[index]
                                StandingListItem(
                                    driver = standing.driver?.fullName,
                                    constructor = standing.constructors?.joinToString(", ") { it.name }
                                        ?: "",
                                    position = standing.position,
                                    positionText = standing.positionText,
                                    points = standing.points,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        standing.driver?.let { onDriverClick(it) }
                                    }
                                )
                            }
                        } ?: item {
                            Text(
                                text = stringResource(R.string.no_data_currently),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onTertiary,
                            )
                        }
                    }

                    1 -> {
                        constructorStandings?.let { constructorStandings ->
                            items(constructorStandings.size) { index ->
                                val standing = constructorStandings[index]
                                StandingListItem(
                                    constructor = standing.constructor?.name
                                        ?: "",
                                    position = standing.position,
                                    positionText = standing.positionText,
                                    points = standing.points,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        standing.constructor?.let { onConstructorClick(it) }
                                    }
                                )
                            }
                        } ?: item {
                            Text(
                                text = stringResource(R.string.no_data_currently),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onTertiary,
                            )
                        }
                    }
                }
            }
        }
    }
}