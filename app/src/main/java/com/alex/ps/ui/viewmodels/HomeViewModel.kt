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
import com.alex.ps.domain.getBy
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
                calcTimerState(now, it.queues.getBy(1, 2).slots)
//                TimerModel.default()
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
        slots: List<Slot>
    ): TimerModel {
        val currentSlot = slots.find { it.intersect(now) }
        val currentSlotIndex = slots.indexOf(currentSlot)

        if (currentSlot == null) {
            return TimerModel.default()
        }

        val isOn = currentSlot.state != SlotState.RED
        var prevSlot: Slot? = null
        var nextSlot: Slot? = null

        val prevSlotStateToFind = when(currentSlot.state) {
            SlotState.RED -> SlotState.GREEN
            SlotState.GREEN -> SlotState.RED
            SlotState.YELLOW -> SlotState.RED
        }

        val nextSlotStateToFind = when(currentSlot.state) {
            SlotState.RED -> SlotState.YELLOW
            SlotState.GREEN -> SlotState.RED
            SlotState.YELLOW -> SlotState.RED
        }

        for (i in (1..slots.size / 2)) {
            val prevIdx = currentSlotIndex - i
            if (prevSlot == null && prevIdx >= 0) {
                if (slots[prevIdx].state == prevSlotStateToFind) {
                    prevSlot = slots[prevIdx]
                }
            }

            val nextIdx = currentSlotIndex + i
            if (nextSlot == null && nextIdx < slots.size) {
                if (slots[nextIdx].state == nextSlotStateToFind) {
                    nextSlot = slots[nextIdx]
                }
            }

            if (prevSlot != null && nextSlot != null) {
                break
            }
        }

        val fromDateTime = prevSlot?.end ?: slots.first().end
        val toDateTime = nextSlot?.start ?: slots.last().end
        val remainingSeconds = Duration.between(now, toDateTime).seconds

        val hours = remainingSeconds / 3600
        val minutes = (remainingSeconds % 3600) / 60
        val seconds = remainingSeconds % 60

        val timePrefix = if (hours > 0)
            hours
        else
            minutes

        val timeSuffix = if (hours > 0)
            minutes
        else
            seconds

        val totalSeconds = Duration.between(fromDateTime, toDateTime).seconds

        return TimerModel(
            isOn = isOn,
            time = "%02d:%02d".format(timePrefix, timeSuffix),
            date = "${now.dayOfMonth}.${now.month}.${now.year}",
            total = totalSeconds.toFloat(),
            remaining = remainingSeconds.toFloat()
        )
    }
}

private fun LocalDateTime.atStartOfDay(): LocalDateTime {
    return LocalDateTime.of(
        year, month, dayOfMonth, 0, 0
    )
}
