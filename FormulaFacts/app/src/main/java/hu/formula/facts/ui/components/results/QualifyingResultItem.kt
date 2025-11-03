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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.formula.facts.R
import hu.formula.facts.domain.model.Constructor
import hu.formula.facts.domain.model.Driver
import hu.formula.facts.domain.model.QualifyingResult
import hu.formula.facts.ui.theme.FormulaFactsTheme
import hu.formula.facts.ui.util.toQualifyingTime

@Composable
fun QualifyingResultItem(
    result: QualifyingResult,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            val size = 28.dp
            Box(
                modifier = Modifier
                    .size(size)
                    .background(
                        color = if (result.q2Time == null) {
                            MaterialTheme.colorScheme.tertiary
                        } else if (result.q3Time == null) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = result.position?.toString() ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = (size.value / 2).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (result.q2Time == null) {
                            MaterialTheme.colorScheme.onTertiary
                        } else if (result.q3Time == null) {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
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
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                result.q3Time?.let {
                    Text(
                        text = stringResource(R.string.q3_time, it.toQualifyingTime()),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
                result.q2Time?.let {
                    Text(
                        text = stringResource(R.string.q2_time, it.toQualifyingTime()),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (result.q3Time == null) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        ),
                    )
                }
                result.q1Time?.let {
                    Text(
                        text = stringResource(R.string.q1_time, it.toQualifyingTime()),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (result.q2Time == null) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            }
                        ),
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun Q3ItemPreview() {
    FormulaFactsTheme {
        QualifyingResultItem(
            result = QualifyingResult(
                position = 1,
                number = 1,
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
                q1Time = "1:30.123",
                q2Time = "1:29.456",
                q3Time = "1:28.789",
            ),
        )
    }
}

@Preview
@Composable
private fun Q2ItemPreview() {
    FormulaFactsTheme {
        QualifyingResultItem(
            result = QualifyingResult(
                position = 13,
                number = 12,
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
                q1Time = "1:30.123",
                q2Time = "1:29.456",
            ),
        )
    }
}

@Preview
@Composable
private fun Q1ItemPreview() {
    FormulaFactsTheme {
        QualifyingResultItem(
            result = QualifyingResult(
                position = 17,
                number = 1,
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
                q1Time = "1:30.123",
            ),
        )
    }
}
