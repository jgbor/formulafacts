package hu.formula.facts.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import hu.formula.facts.R
import hu.formula.facts.domain.model.Circuit
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.domain.model.RaceResult
import hu.formula.facts.ui.components.AutoResizedText
import hu.formula.facts.ui.theme.FormulaFactsTheme
import kotlinx.datetime.Instant

@Composable
fun SeasonRaceCard(
    gp: GrandPrix,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ListItem(
        modifier = modifier
            .defaultMinSize(minHeight = dimensionResource(R.dimen.minimum_card_height))
            .clickable { onClick() },
        leadingContent = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.round_number, gp.round),
                    style = MaterialTheme.typography.bodySmall
                )
                AutoResizedText(
                    text = gp.name.replace("Grand Prix", "GP"),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                gp.raceResults?.first {
                    it.position == 1
                }?.driver?.fullName?.let {
                    Text(
                        text = stringResource(R.string.winner, it),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        headlineContent = {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                AutoResizedText(
                    text = gp.circuit?.city ?: "",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        },
        trailingContent = {
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary,
            headlineColor = MaterialTheme.colorScheme.onPrimary,
            leadingIconColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Preview
@Composable
private fun CardPreview() {
    FormulaFactsTheme {
        SeasonRaceCard(
            gp = GrandPrix(
                season = 2023,
                round = 1,
                name = "Australian Grand Prix",
                circuit = Circuit(
                    id = "albert_park",
                    name = "Albert Park Circuit",
                    city = "Melbourne"
                ),
                startTime = Instant.parse("2023-03-31T14:00:00Z"),
                raceResults = listOf(
                    RaceResult(
                        position = 1,
                        positionText = "1",
                        driver = Driver(
                            id = "msc",
                            code = "MSC",
                            givenName = "Michael",
                            familyName = "Schumacher",
                        )
                    )
                )
            )
        )
    }
}
