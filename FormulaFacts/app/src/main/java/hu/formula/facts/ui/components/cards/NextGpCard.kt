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
import androidx.compose.runtime.remember
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@Composable
fun NextGpCard(
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
                .background(MaterialTheme.colorScheme.primary)
                .border(color = Color.Black, width = 1.dp)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.next_gp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            grandPrix?.let { gp ->
                AutoResizedText(
                    text = gp.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                gp.circuit?.let {
                    AutoResizedText(
                        text = it.name + (it.city?.let { city -> ", $city" } ?: ""),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                val raceDay =
                    remember { gp.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
                Text(
                    text = raceDay.formatDateRange(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            if (grandPrix == null) {
                Text(
                    text = stringResource(R.string.no_data_currently),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

private fun LocalDate.formatDateRange(): String {
    val twoDaysBefore = this.minus(2, DateTimeUnit.DAY)
    val dayMonthFormatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
    }
    val fullFormatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
        year()
    }
    return "${twoDaysBefore.format(dayMonthFormatter)} - ${this.format(fullFormatter)}"
}

@Preview
@Composable
private fun NextGpCardPreview() {
    FormulaFactsTheme {
        NextGpCard(
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
