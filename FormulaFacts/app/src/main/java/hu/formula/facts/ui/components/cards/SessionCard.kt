package hu.formula.facts.ui.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.formula.facts.R
import hu.formula.facts.domain.model.QualifyingResult
import hu.formula.facts.domain.model.RaceResult
import hu.formula.facts.ui.components.results.QualifyingResultItem
import hu.formula.facts.ui.components.results.RaceResultItem
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SessionCard(
    sessionName: String,
    startTime: Instant,
    modifier: Modifier = Modifier,
    raceRes: List<RaceResult>? = null,
    qualiRes: List<QualifyingResult>? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_shape)),
        modifier = modifier.defaultMinSize(minHeight = 64.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.small_padding))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = sessionName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = startTime.toDayAndTime(),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Spacer(Modifier.weight(1f))
                if (raceRes.isNullOrEmpty().not() || qualiRes.isNullOrEmpty().not()) {
                    IconButton(
                        onClick = {
                            expanded = !expanded
                        }
                    ) {
                        if (expanded) {
                            Icon(
                                imageVector = Icons.Rounded.ExpandLess,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.ExpandMore,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            if (expanded) {
                raceRes?.forEach {
                    RaceResultItem(it, Modifier.fillMaxWidth())
                }
                qualiRes?.forEach {
                    QualifyingResultItem(it, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

private fun Instant.toDayAndTime(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    val dayMonthFormatter = LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
    }
    val date = localDateTime.date.format(dayMonthFormatter)
    val dayOfWeek = localDateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("en"))
    return "$date  ${dayOfWeek}, ${localDateTime.time}"
}

@Preview
@Composable
private fun SessionCardPreview() {
    SessionCard(
        sessionName = "Qualifying",
        startTime = Instant.parse("2023-10-01T14:00:00Z"),
    )
}
