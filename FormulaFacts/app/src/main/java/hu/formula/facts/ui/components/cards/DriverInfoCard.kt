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
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.ui.theme.FormulaFactsTheme
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun DriverInfoCard(
    driver: Driver,
    modifier: Modifier = Modifier,
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
                text = driver.fullName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary,
            )

            driver.code?.let { code ->
                Text(
                    text = stringResource(R.string.driver_code, code),
                )
            }

            driver.nationality?.let { nationality ->
                Text(
                    text = stringResource(R.string.nationality, nationality),
                )
            }

            driver.dateOfBirth?.format(
                LocalDate.Format {
                    dayOfMonth()
                    char('.')
                    monthNumber()
                    char('.')
                    year()
                }
            )?.let { birthDay ->
                Text(
                    text = stringResource(R.string.born, birthDay),
                )
            }

            driver.permanentNumber?.let { number ->
                Text(
                    text = stringResource(R.string.fix_number, number),
                )
            }
        }
    }
}

@Preview
@Composable
private fun DriverInfoCardPreview() {
    FormulaFactsTheme {
        DriverInfoCard(
            driver = Driver(
                id = "msc",
                givenName = "Michael",
                familyName = "Schumacher",
                dateOfBirth = LocalDate.parse("1969-01-03"),
                nationality = "German",
                code = "MSC"
            )
        )
    }
}
