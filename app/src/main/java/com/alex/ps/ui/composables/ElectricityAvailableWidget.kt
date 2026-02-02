package com.alex.ps.ui.composables

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.theme.AppTheme

enum class TimePeriodPresentationState{
    SOON,
    ACTIVE,
    PAST
}

class TimePeriodPresentation(
    val start: String,
    val end: String,
    val duration: String,
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
            text = "Electricity available periods",
            style = AppTheme.typography.labelMedium
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEach { period ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val backgroundColor =
                        if (period.state == TimePeriodPresentationState.ACTIVE)
                            AppTheme.colorScheme.primary
                        else
                            AppTheme.colorScheme.surface

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = backgroundColor,
                                shape = AppTheme.shape.medium
                            )
                            .padding(
                                horizontal = 14.dp,
                                vertical =  10.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = period.start,
                            style = AppTheme.typography.labelMedium
                        )
                        Text(
                            text = period.duration,
                            style = AppTheme.typography.labelMedium
                        )
                        Text(
                            text = period.end,
                            style = AppTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}