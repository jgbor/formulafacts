package hu.formula.facts.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hu.formula.facts.R

fun Float.toFormattedString(): String {
    return this.toBigDecimal().stripTrailingZeros().toPlainString()
}

@Composable
fun String.toQualifyingTime(): String {
    return this.takeIf { it.isNotEmpty() } ?: stringResource(R.string.no_time)
}
