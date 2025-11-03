package hu.formula.facts.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import hu.formula.facts.R
import hu.formula.facts.domain.util.SessionType

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    isRaceEnabled: Boolean,
    onRaceChange: (Boolean) -> Unit,
    isQualifyingEnabled: Boolean,
    onQualifyingChange: (Boolean) -> Unit,
    isPracticeEnabled: Boolean,
    onPracticeChange: (Boolean) -> Unit,
) {
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        rememberPermissionState(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
    }
    var typeThatChanged by remember { mutableStateOf<SessionType?>(null) }

    @ExperimentalPermissionsApi
    fun checkPermission(type: SessionType, value: Boolean, function: (Boolean) -> Unit) {
        if (permissionState.status.isGranted) {
            function(value)
        } else {
            typeThatChanged = type
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            when (typeThatChanged) {
                SessionType.RACE -> onRaceChange(!isRaceEnabled)
                SessionType.PRACTICE -> onPracticeChange(!isPracticeEnabled)
                SessionType.QUALIFYING -> onQualifyingChange(!isQualifyingEnabled)
                else -> {}
            }
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.races))
                    Switch(
                        checked = isRaceEnabled,
                        onCheckedChange = { checkPermission(SessionType.RACE, it, onRaceChange) },
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.switch_padding))
                    )
                }
            },
            onClick = { checkPermission(SessionType.RACE, !isRaceEnabled, onRaceChange) }
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.qualifying))
                    Switch(
                        checked = isQualifyingEnabled,
                        onCheckedChange = {
                            checkPermission(
                                SessionType.QUALIFYING,
                                it,
                                onQualifyingChange
                            )
                        },
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.switch_padding))
                    )
                }
            },
            onClick = {
                checkPermission(
                    SessionType.QUALIFYING,
                    !isQualifyingEnabled,
                    onQualifyingChange
                )
            }
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.practice))
                    Switch(
                        checked = isPracticeEnabled,
                        onCheckedChange = {
                            checkPermission(
                                SessionType.PRACTICE,
                                it,
                                onPracticeChange
                            )
                        },
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.switch_padding))
                    )
                }
            },
            onClick = { checkPermission(SessionType.PRACTICE, !isPracticeEnabled, onPracticeChange) }
        )
    }
}
