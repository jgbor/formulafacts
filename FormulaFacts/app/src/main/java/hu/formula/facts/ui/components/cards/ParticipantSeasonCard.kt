package hu.formula.facts.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import hu.formula.facts.R
import hu.formula.facts.domain.model.Season
import hu.formula.facts.domain.model.Standing
import hu.formula.facts.ui.theme.Gold
import hu.formula.facts.ui.util.toFormattedString
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ParticipantSeasonCard(
    season: Season,
    standing: Standing?,
    expanded: Boolean,
    loadingSeasonRes: Boolean,
    modifier: Modifier = Modifier,
    onExpandClick: () -> Unit = {},
    expandedContent: @Composable () -> Unit = {},
) {
    Card(
        modifier = modifier.defaultMinSize(minHeight = dimensionResource(R.dimen.minimum_card_height)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.small_padding))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = season.year.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    if (loadingSeasonRes) {
                        LinearProgressIndicator()
                    } else if (standing != null) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.season_result,
                                if (standing.points == 1f) 1 else 2,
                                standing.positionText,
                                standing.points.toFormattedString()
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Spacer(Modifier.weight(1f))


                standing?.let {
                    val today = remember {
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    }
                    if (it.position == 1 && season.year != today.year) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(
                                    MaterialTheme.colorScheme.tertiary,
                                    CircleShape
                                ),
                            tint = Gold,
                        )
                    }
                }

                IconButton(
                    onClick = onExpandClick
                ) {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Rounded.ExpandLess,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.ExpandMore,
                            contentDescription = null
                        )
                    }
                }
            }

            if (expanded) {
                expandedContent()
            }
        }
    }
}
