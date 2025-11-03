package hu.formula.facts.feature.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hu.formula.facts.connectivity.rememberConnectivityState
import hu.formula.facts.ui.components.NoConnectionBar

@Composable
fun ScreenBase(
    viewModel: ViewModelBase,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val connectionState by rememberConnectivityState()

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoConnectionBar(
                connectionState = connectionState,
                modifier = Modifier.fillMaxWidth(),
                onRefresh = { viewModel.onEvent(BaseEvent.Refresh) }
            )

            content()
        }
    }
}
