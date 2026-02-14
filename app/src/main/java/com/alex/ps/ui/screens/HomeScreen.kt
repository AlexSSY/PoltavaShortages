package com.alex.ps.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.ui.composables.ElectricityAvailableWidget
import com.alex.ps.ui.composables.ExtraInfoWidget
import com.alex.ps.ui.composables.SummaryWidget
import com.alex.ps.ui.composables.TimePeriodPresentation
import com.alex.ps.ui.composables.TimePeriodPresentationState
import com.alex.ps.ui.composables.TimerWidget
import com.alex.ps.ui.composables.ToastWidget
import com.alex.ps.ui.theme.AppTheme
import com.alex.ps.ui.viewmodels.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onClickTomorrowScheduleLink: () -> Unit
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val timerModelState = homeViewModel.timerModelFlow.collectAsState()
    val summaryModelState = homeViewModel.summaryModelFlow.collectAsState()
    val periods = homeViewModel.periodsModelStateFlow.collectAsState()
    val extraState = homeViewModel.extraInfoStateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 58.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(27.dp)
    ) {
        TimerWidget(
            radius = 96.dp,
            timerModel = timerModelState.value
        )
        ExtraInfoWidget(
            extra = extraState.value
        )
        SummaryWidget(
            summaryModel = summaryModelState.value
        )
        ElectricityAvailableWidget(
            periods = periods.value
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = AppTheme.shape.medium,
            contentPadding = PaddingValues(
                horizontal = 0.dp,
                vertical = 6.dp
            ),
            onClick = onClickTomorrowScheduleLink
        ) {
            Text(
                text = "Tomorrow Schedule",
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colorScheme.onPrimary
            )
        }
    }
}