package com.alex.ps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alex.ps.ui.composables.CircularSegmentProgressBar
import com.alex.ps.ui.composables.ElectricityAvailableWidget
import com.alex.ps.ui.composables.SummaryWidget
import com.alex.ps.ui.composables.TimePeriodPresentation
import com.alex.ps.ui.composables.TimePeriodPresentationState
import com.alex.ps.ui.composables.TimerWidget
import com.alex.ps.ui.composables.ToastWidget
import com.alex.ps.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val periods by remember { mutableStateOf(listOf(
                TimePeriodPresentation(
                    start = "0:30",
                    end = "2:30",
                    duration = "2 hours",
                    state = TimePeriodPresentationState.PAST
                ),
                TimePeriodPresentation(
                    start = "6:30",
                    end = "7:00",
                    duration = "1.5 hours",
                    state = TimePeriodPresentationState.ACTIVE
                ),
                TimePeriodPresentation(
                    start = "11:30",
                    end = "13:00",
                    duration = "1.5 hours"
                ),
                TimePeriodPresentation(
                    start = "17:30",
                    end = "18:30",
                    duration = "1 hour"
                ),
            )) }

            AppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Today Schedule",
                                    style = AppTheme.typography.titleMedium
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
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
                            isOn = true,
                            time = "1:54",
                            date = "02.02.2026",
                            total = 1000F,
                            remaining = 300F
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ToastWidget(text = "ГАВ")
                            ToastWidget(text = "СГАВ")
                        }
                        SummaryWidget(
                            redHours = "-20",
                            greenHours = "+4"
                        )
                        ElectricityAvailableWidget(
                            periods = periods
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            shape = AppTheme.shape.medium,
                            contentPadding = PaddingValues(
                                horizontal = 0.dp,
                                vertical = 6.dp
                            ),
                            onClick = {}
                        ) {
                            Text(
                                text = "Tomorrow Schedule",
                                style = AppTheme.typography.labelMedium,
                                color = AppTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}