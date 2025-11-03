package hu.formula.facts.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.formula.facts.R
import hu.formula.facts.domain.model.Circuit
import hu.formula.facts.domain.model.GrandPrix
import hu.formula.facts.ui.components.AutoResizedText
import hu.formula.facts.ui.theme.FormulaFactsTheme
import kotlinx.datetime.Instant

@Composable
fun PreviousGpCard(
    grandPrix: GrandPrix?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RectangleShape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.tertiary)
                .border(color = Color.Black, width = 1.dp)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.previous_gp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary,
            )
            grandPrix?.let { gp ->
                AutoResizedText(
                    text = gp.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
                gp.circuit?.let {
                    AutoResizedText(
                        text = it.name + (it.city?.let { city -> ", $city" } ?: ""),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                }
            }
            if (grandPrix == null) {
                Text(
                    text = stringResource(R.string.no_data_currently),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun NextGpCardPreview() {
    FormulaFactsTheme {
        PreviousGpCard(
            grandPrix = GrandPrix(
                season = 2025,
                round = 15,
                name = "Hungarian GP",
                circuit = Circuit(
                    id = "hungaroring",
                    name = "Hungaroring",
                    country = "Hungary",
                    city = "Budapest",
                ),
                startTime = Instant.parse("2025-07-30T14:00:00Z"),
            )
        )
    }
}
