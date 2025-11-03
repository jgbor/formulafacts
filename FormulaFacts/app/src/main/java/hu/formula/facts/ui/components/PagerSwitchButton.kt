package hu.formula.facts.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import hu.formula.facts.R

@Composable
fun PagerSwitchButton(
    onClick: () -> Unit,
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_shape)),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary
    ),
    border: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = colors,
        shape = shape,
        modifier = modifier,
        border = if (border) BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.secondary,
        ) else null,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
