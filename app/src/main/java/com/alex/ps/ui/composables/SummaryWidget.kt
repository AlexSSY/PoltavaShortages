package com.alex.ps.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.SummaryModel
import com.alex.ps.ui.theme.AppTheme

@Composable
fun SummaryWidget(
    modifier: Modifier = Modifier,
    summaryModel: SummaryModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = AppTheme.colorScheme.surface,
                shape = AppTheme.shape.medium
            )
            .padding(
                horizontal = 14.dp,
                vertical =  10.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Summary",
            style = AppTheme.typography.bodyMedium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "-" + summaryModel.redHours,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.error
            )
            Text(
                text = "+" + summaryModel.greenHours,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.primary
            )
        }
    }
}