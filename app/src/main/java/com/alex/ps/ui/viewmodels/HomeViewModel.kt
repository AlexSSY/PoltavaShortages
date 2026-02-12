package com.alex.ps.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.TimePeriod
import com.alex.ps.ui.model.TimerModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class HomeViewModel(
    settingsDataStore: SettingsDataStore,
    shortagesRepository: ShortagesRepository
): ViewModel() {
    private val nowStateFlow: Flow<LocalDateTime> = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(1_000)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(1_000),
        LocalDateTime.now()
    )

    private val shortagesStateFlow: StateFlow<Shortages?> = shortagesRepository.shortagesFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            null
        )

    val timerModelFlow: StateFlow<TimerModel> =
        combine(nowStateFlow, shortagesStateFlow) { now, shortages ->
            shortages?.let {
                calculateCurrentTimerState(now, it.queues[1].happyPeriods)
            } ?: TimerModel.default()

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            TimerModel.default()
        )

    init {
        viewModelScope.launch {
            shortagesRepository.refresh()
        }
    }

    private fun calculateCurrentTimerState(
        now: LocalDateTime,
        periods: List<TimePeriod>
    ): TimerModel {
        for (period in periods) {
            if (period.contains(now)) {
                val totalSeconds = period.durationInMinutes * 60
                val remainingSeconds = Duration.between(now, period.end).toSeconds()

                val minutes = remainingSeconds / 60
                val hours = minutes / 60
                val seconds = remainingSeconds % 60

                val prefix = if (remainingSeconds >= 3600)
                        hours
                    else
                        minutes

                val suffix = if (remainingSeconds >= 3600)
                        minutes
                    else
                        seconds

                return TimerModel(
                    isOn = true,
                    time = "${prefix}:${suffix}",
                    date = "${now.dayOfMonth}-${now.month}-${now.year}",
                    total = totalSeconds.toFloat(),
                    remaining = remainingSeconds.toFloat()
                )
            }
        }
        val actualPeriods = periods.filter { it.start > now }
        val nearestPeriod = actualPeriods.minByOrNull { Duration.between(now, it.start) }

        return nearestPeriod?.let {
            TimerModel(
                isOn = false,
                time = "0:0",
                date = "1-1-2001",
                total = it.durationInMinutes.toFloat(),
                remaining = Duration.between(now, it.start).toMinutes().toFloat()
            )
        } ?: TimerModel.default()
    }
}