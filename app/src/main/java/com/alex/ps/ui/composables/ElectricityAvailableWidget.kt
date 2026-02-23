package com.alex.ps.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.alex.ps.R
import com.alex.ps.ui.theme.AppTheme
import java.text.DecimalFormat
import kotlin.math.round

enum class TimePeriodPresentationState{
    SOON,
    ACTIVE,
    PAST
}

class TimePeriodPresentation(
    val start: String,
    val end: String,
    val duration: Float,
    val state: TimePeriodPresentationState = TimePeriodPresentationState.SOON
)

@Composable
fun ElectricityAvailableWidget(
    modifier: Modifier = Modifier,
    periods: List<TimePeriodPresentation>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.electricity_available_periods),
            style = AppTheme.typography.titleSmall
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { period ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val backgroundColor =
                        when (period.state) {
                            TimePeriodPresentationState.ACTIVE -> AppTheme.colorScheme.surface
                            TimePeriodPresentationState.SOON -> AppTheme.colorScheme.surface
                            TimePeriodPresentationState.PAST -> AppTheme.colorScheme.surface
                                .copy(alpha = .5F)
                        }

                    val textColor =
                        when (period.state) {
                            TimePeriodPresentationState.ACTIVE -> AppTheme.colorScheme.onBackground
                            TimePeriodPresentationState.SOON -> AppTheme.colorScheme.onBackground
                            TimePeriodPresentationState.PAST -> AppTheme.colorScheme.onBackground
                                .copy(alpha = .25F)
                        }

                    val rowModifier =
                        when (period.state) {
                            TimePeriodPresentationState.ACTIVE -> Modifier.border(
                                width = 1.dp,
                                shape = AppTheme.shape.medium,
                                color = AppTheme.colorScheme.primary
                            )
                            else -> Modifier
                        }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = backgroundColor,
                                shape = AppTheme.shape.medium
                            )
                            .then(rowModifier)
                            .padding(
                                horizontal = 14.dp,
                                vertical =  10.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = period.start,
                            style = AppTheme.typography.bodyMedium,
                            color = textColor
                        )
                        val hours = round(period.duration)
                        val formatter = DecimalFormat("#.##")

                        Text(
                            text = pluralStringResource(
                                id = R.plurals.hours,
                                count = hours.toInt(),
                                formatter.format(period.duration)
                            ),
                            style = AppTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = textColor
                        )
                        Text(
                            text = period.end,
                            style = AppTheme.typography.bodyMedium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}