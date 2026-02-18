package com.alex.ps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.composables.ElectricityAvailableWidget
import com.alex.ps.ui.composables.SummaryDonutChart
import org.koin.androidx.compose.koinViewModel

@Composable
fun TomorrowScheduleScreen(
    onBack: () -> Unit
) {
    val tomorrowViewModel: TomorrowViewModel = koinViewModel()
    val summaryModelState = tomorrowViewModel.summaryModel.collectAsState()
    val periods = tomorrowViewModel.periodsModelStateFlow.collectAsState()
    val tomorrowDate = tomorrowViewModel.tomorrowDateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 32.dp,
                horizontal = 58.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        SummaryDonutChart(
            negativeValue = summaryModelState.value.redHours,
            positiveValue = summaryModelState.value.greenHours,
            date = "%02d.%02d.%04d".format(
                tomorrowDate.value.dayOfMonth,
                tomorrowDate.value.monthValue,
                tomorrowDate.value.year
            ),
            redColor = MaterialTheme.colorScheme.error,
            greenColor = MaterialTheme.colorScheme.primary
        )
        ElectricityAvailableWidget(
            periods = periods.value
        )
    }
}