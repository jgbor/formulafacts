package hu.formula.facts.ui.components.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import hu.formula.facts.ui.components.AutoResizedText
import hu.formula.facts.ui.theme.Bronze
import hu.formula.facts.ui.theme.FormulaFactsTheme
import hu.formula.facts.ui.theme.Gold
import hu.formula.facts.ui.theme.Silver
import hu.formula.facts.ui.util.toFormattedString

@Composable
fun StandingListItem(
    position: Int,
    points: Float,
    constructor: String,
    modifier: Modifier = Modifier,
    positionText: String? = null,
    driver: String? = null,
    onClick: () -> Unit = {},
) {
    ListItem(
        leadingContent = {
            val size = 48.dp
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
                    text = positionText ?: position.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (size.value / 2).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                )
            }
        },
        headlineContent = {
            AutoResizedText(
                text = driver ?: constructor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
            )
        },
        supportingContent = {
            if (driver != null) {
                AutoResizedText(
                    text = constructor,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        trailingContent = {
            Text(
                text = pluralStringResource(R.plurals.points, if (points == 1.0f) 1 else 2, points.toFormattedString()),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier = modifier.clickable { onClick() },
    )
}

@Preview
@Composable
private fun PreviewGoldStandingListItem() {
    FormulaFactsTheme {
        StandingListItem(
            position = 1,
            points = 435.0f,
            constructor = "Red Bull Racing",
            driver = "Max Verstappen",
        )
    }
}

@Preview
@Composable
private fun PreviewSilverStandingListItem() {
    FormulaFactsTheme {
        StandingListItem(
            position = 2,
            points = 234.0f,
            constructor = "Ferrari",
        )
    }
}

@Preview
@Composable
private fun PreviewBronzeStandingListItem() {
    FormulaFactsTheme {
        StandingListItem(
            position = 3,
            points = 124.0f,
            constructor = "Mercedes",
        )
    }
}

@Preview
@Composable
private fun PreviewOtherStandingListItem() {
    FormulaFactsTheme {
        StandingListItem(
            position = 4,
            points = 12.0f,
            constructor = "Alpine",
            driver = "Esteban Ocon",
        )
    }
}
