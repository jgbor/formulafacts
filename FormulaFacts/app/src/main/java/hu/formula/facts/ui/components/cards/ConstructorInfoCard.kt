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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.ui.theme.FormulaFactsTheme

@Composable
fun ConstructorInfoCard(
    constructor: Constructor,
    modifier: Modifier = Modifier,
    firstSeason: Int? = null,
) {
    Card(
        modifier = modifier,
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
                text = constructor.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary,
            )

            constructor.nationality?.let { nationality ->
                Text(
                    text = stringResource(R.string.nationality, nationality),
                )
            }

            firstSeason?.let {
                Text(
                    text = stringResource(R.string.first_season, it),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConstructorInfoCardPreview() {
    FormulaFactsTheme {
        ConstructorInfoCard(
            constructor = Constructor(
                id = "ferrari",
                name = "Ferrari",
                nationality = "Italian",
            ),
            firstSeason = 1950
        )
    }
}
