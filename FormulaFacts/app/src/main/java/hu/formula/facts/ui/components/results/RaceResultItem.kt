package hu.formula.facts.ui.components.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.RaceResult
import hu.formula.facts.ui.theme.Bronze
import hu.formula.facts.ui.theme.FormulaFactsTheme
import hu.formula.facts.ui.theme.Gold
import hu.formula.facts.ui.theme.Silver
import hu.formula.facts.ui.util.toFormattedString

@Composable
fun RaceResultItem(
    result: RaceResult,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            val size = 28.dp
            val position = result.position
            Box(
                modifier = Modifier
                    .size(size)
                    .background(
                        color = when (position) {
                            1 -> Gold
                            2 -> Silver
                            3 -> Bronze
                            else -> Color.White
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = if (position in 1..3) 0.dp else 2.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = result.positionText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (size.value / 2).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                )
            }
        },
        headlineContent = {
            Text(
                text = result.driver.fullName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
            )
        },
        supportingContent = {
            Text(
                text = result.constructor?.name ?: "",
                style = MaterialTheme.typography.bodySmall,
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = result.raceTime ?: "",
                    style = MaterialTheme.typography.bodySmall,
                )
                result.points?.takeIf { it > 0 }?.let {
                    Text(
                        pluralStringResource(
                            R.plurals.points,
                            if (it == 1.0f) 1 else 2,
                            it.toFormattedString()
                        )
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun ItemPreview() {
    FormulaFactsTheme {
        RaceResultItem(
            result = RaceResult(
                position = 1,
                positionText = "1",
                driver = Driver(
                    id = "msc",
                    code = "MSC",
                    givenName = "Michael",
                    familyName = "Schumacher",
                ),
                constructor = Constructor(
                    id = "ferrari",
                    name = "Ferrari",
                ),
                raceTime = "1:30:00",
                points = 10.3f,
            )
        )
    }
}