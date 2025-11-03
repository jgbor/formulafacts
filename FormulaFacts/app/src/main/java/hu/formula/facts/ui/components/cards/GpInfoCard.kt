package hu.formula.facts.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.sp
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
fun GpInfoCard(
    gp: GrandPrix,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RectangleShape,
        modifier = modifier,
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
                text = gp.season.toString() + ", " + stringResource(R.string.round_number, gp.round),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            AutoResizedText(
                text = gp.name.replace("Grand Prix", "GP"),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            gp.circuit?.let { circuit ->
                AutoResizedText(
                    text = stringResource(R.string.circuit_name, circuit.name),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                circuit.city?.let { city ->
                    AutoResizedText(
                        text = stringResource(R.string.city, city + (circuit.country?.let { ", $it" } ?: "")),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
            val raceDay =
                remember { gp.startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date }
            Text(
                text = stringResource(R.string.date, raceDay.formatDateRange()),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

private fun LocalDate.formatDateRange(): String {
    val twoDaysBefore = this.minus(2, DateTimeUnit.DAY)
    val formatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
    }
    return "${twoDaysBefore.format(formatter)} - ${this.format(formatter)}"
}

@Preview
@Composable
private fun NextGpCardPreview() {
    FormulaFactsTheme {
        GpInfoCard(
            gp = GrandPrix(
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
