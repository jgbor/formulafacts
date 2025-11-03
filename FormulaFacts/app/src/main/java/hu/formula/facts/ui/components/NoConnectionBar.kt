package hu.formula.facts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import hu.formula.facts.R
import hu.formula.facts.connectivity.ConnectionState

@Composable
fun NoConnectionBar(
    connectionState: ConnectionState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
) {
    var connectionLost by remember { mutableStateOf(false) }
    var showRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(connectionState) {
        if (connectionState == ConnectionState.Available && connectionLost) {
            connectionLost = false
            showRefresh = true
        } else {
            showRefresh = false
        }
    }

    if (connectionState == ConnectionState.Unavailable) {
        LaunchedEffect(Unit) {
            connectionLost = true
        }

        Row (
            modifier = modifier.background(Color.Yellow),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.no_connection),
            )
        }
    } else if (showRefresh) {
        Row (
            modifier = modifier.background(Color.Green),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val annotatedString = buildAnnotatedString {
                append(stringResource(R.string.tap))

                withLink(LinkAnnotation.Clickable(tag = "refresh"){
                    showRefresh = false
                    onRefresh()
                }) {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(stringResource(R.string.here))
                    }
                }
                append(stringResource(R.string.to_refresh))
            }
            Text(
                annotatedString
            )
        }
    }
}
