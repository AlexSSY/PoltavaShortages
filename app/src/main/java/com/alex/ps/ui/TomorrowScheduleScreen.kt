package com.alex.ps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.alex.ps.ui.composables.ElectricityAvailableWidget
import com.alex.ps.ui.composables.SummaryDonutChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun TomorrowScheduleScreen(
    onBack: () -> Unit
) {
    val tomorrowViewModel: TomorrowViewModel = koinViewModel()
    val summaryModelState = tomorrowViewModel.summaryModel.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SummaryDonutChart(
            negativeValue = summaryModelState.value.redHours,
            positiveValue = summaryModelState.value.greenHours,
            date = "30.01.2026"
        )
        ElectricityAvailableWidget(
            periods = periods.value
        )
    }
}