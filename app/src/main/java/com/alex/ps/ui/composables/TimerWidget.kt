package com.alex.ps.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.theme.AppTheme

@Composable
fun TimerWidget(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,
    isOn: Boolean,
    time: String,
    date: String,
    total: Float,
    remaining: Float
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularSegmentProgressBar(
            percent = remaining * (100F / total),
            radius = radius,
            width = 15.dp,
            backgroundColor = AppTheme.colorScheme.surface,
            segmentColor = AppTheme.colorScheme.primary,
            segments = 38
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (isOn) "ON" else "OFF",
                color = if (isOn) AppTheme.colorScheme.primary else AppTheme.colorScheme.error,
                style = AppTheme.typography.titleSmall
            )
            Text(
                text = time,
                color = AppTheme.colorScheme.onSecondary,
                style = AppTheme.typography.titleLarge
            )
            Text(
                text = date,
                color = AppTheme.colorScheme.onSecondary,
                style = AppTheme.typography.labelSmall
            )
        }
    }
}