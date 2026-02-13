package com.alex.ps.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.ps.data.settings.SettingsDataStore
import com.alex.ps.domain.Queue
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
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
//                calculateCurrentTimerState(now, it.queues[1].happyPeriods)
                calcTimerState(now, it.queues[1])
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

        val date = "${now.dayOfMonth}-${now.month}-${now.year}"
        val currentPeriod = periods.find { it.contains(now) }
        val isOn = currentPeriod != null

        var totalSeconds = 0L
        var remainingSeconds = 0L

        if (currentPeriod == null) {
            val nearestPeriod = periods.filter { it.start > now }
                .minByOrNull { Duration.between(now, it.start) }
            if (nearestPeriod == null) {
                return TimerModel.default()
            }
            val nearestPreviousPeriod = periods.filter { it.end < now }
                .minByOrNull { Duration.between(it.end, now) }
            val end = nearestPreviousPeriod?.end ?: now.atStartOfDay()
            totalSeconds = Duration.between(end, nearestPeriod.start).seconds
            remainingSeconds = Duration.between(now, nearestPeriod.start).seconds
        } else {
            totalSeconds = currentPeriod.durationInMinutes * 60
            remainingSeconds = Duration.between(now, currentPeriod.end).seconds
        }

        val totalRemainingMinutes = remainingSeconds / 60
        val hours = totalRemainingMinutes / 60
        val minutes = totalRemainingMinutes - (hours * 60)
        val seconds = (hours * 60 + minutes) % 60

        val prefix = if (remainingSeconds >= 3600)
            hours
        else
            minutes

        val suffix = if (remainingSeconds >= 3600)
            minutes
        else
            seconds

        return TimerModel(
            isOn = isOn,
            time = "${prefix}:${suffix}",
            date = date,
            total = totalSeconds.toFloat(),
            remaining = remainingSeconds.toFloat()
        )
    }

    private fun calcTimerState(
        now: LocalDateTime,
        queue: Queue
    ): TimerModel {
        val currentSlot = queue.slots.find { it.intersect(now) }

        if (currentSlot == null) {
            return TimerModel.default()
        }

        val isOn = currentSlot.state != SlotState.RED

        val slotStateToFind = when(currentSlot.state) {
            SlotState.RED -> SlotState.YELLOW
            SlotState.GREEN -> SlotState.RED
            SlotState.YELLOW -> SlotState.RED
        }

        val nextSlot = queue.slots.filter { it.i > currentSlot.i }
            .find { it.state == slotStateToFind }

        val endTime
    }
}

private fun LocalDateTime.atStartOfDay(): LocalDateTime {
    return LocalDateTime.of(
        year, month, dayOfMonth, 0, 0
    )
}
