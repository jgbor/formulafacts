package hu.formula.facts.ui.components.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import hu.formula.facts.R
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.ui.components.AutoResizedText
import hu.formula.facts.ui.util.toFormattedString

@Composable
fun ConstructorRaceResultItem(
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
                val combinedPoints =
                    gp.raceResults?.sumOf { it.points?.toDouble() ?: 0.0 }
                combinedPoints?.let { points ->
                    Text(
                        text = pluralStringResource(
                            R.plurals.points,
                            if (points == 1.0) 1 else 2,
                            points.toFloat().toFormattedString()
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        headlineContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                gp.raceResults?.forEach { res ->
                    val positionText = res.positionText.toIntOrNull()?.let { "P$it" }
                        ?: res.positionText
                    AutoResizedText(
                        text = stringResource(
                            R.string.driver_pos,
                            res.driver.fullName,
                            positionText
                        ),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                    )
                }
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
