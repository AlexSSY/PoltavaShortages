package com.alex.ps.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.TimerModel
import com.alex.ps.ui.theme.AppTheme
import com.alex.ps.R

@Composable
fun TimerWidget(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,
    timerModel: TimerModel
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularSegmentProgressBar(
            percent = timerModel.remaining * (100F / timerModel.total),
            radius = radius,
            width = 15.dp,
            backgroundColor = AppTheme.colorScheme.surface,
            segmentColor = if (timerModel.isOn)
                AppTheme.colorScheme.primary
            else
                AppTheme.colorScheme.error,
            segments = 38
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (timerModel.isOn)
                    stringResource(id = R.string.timer_on)
                else
                    stringResource(id = R.string.timer_off),
                color = if (timerModel.isOn) AppTheme.colorScheme.primary
                    else AppTheme.colorScheme.error,
                style = AppTheme.typography.titleSmall
            )
            Text(
                text = timerModel.time,
                color = AppTheme.colorScheme.onSecondary,
                style = AppTheme.typography.titleLarge
            )
            Text(
                text = timerModel.date,
                color = AppTheme.colorScheme.onSecondary,
                style = AppTheme.typography.bodyMedium
            )
        }
    }
}