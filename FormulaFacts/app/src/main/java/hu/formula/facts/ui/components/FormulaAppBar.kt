package hu.formula.facts.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.OpenInNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaAppBar(
    title: String?,
    url: String?,
    successState: Boolean,
    onNavigateBack: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    TopAppBar(
        title = {
            if (successState) {
                AutoResizedText(
                    text = title ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            if (successState && url != null) {
                IconButton(
                    onClick = {
                        uriHandler.openUri(url)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Sharp.OpenInNew,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
    )
}
