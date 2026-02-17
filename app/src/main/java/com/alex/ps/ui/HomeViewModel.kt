package com.alex.ps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.ps.data.SettingsRepositoryImpl
import com.alex.ps.domain.Queue
import com.alex.ps.domain.QueueProvider
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
import com.alex.ps.domain.TimePeriodsWithStateUseCase
import com.alex.ps.domain.TimeProvider
import com.alex.ps.domain.getBy
import com.alex.ps.ui.composables.TimePeriodPresentation
import com.alex.ps.ui.composables.TimePeriodPresentationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.max

class HomeViewModel(
    shortagesRepository: ShortagesRepository,
    timeProvider: TimeProvider,
    queueProvider: QueueProvider
): ViewModel() {
    val timerModelFlow: StateFlow<TimerModel> =
        combine(
            timeProvider.timeFlow,
            queueProvider.queueFlow
        ) { now, queue ->
            calcTimerState(now, queue.slots)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            TimerModel.default()
        )

    val todaySlots: StateFlow<List<Slot>> =
        combine(
            timeProvider.timeFlow,
            queueProvider.queueFlow
        ) { nowDate, queue ->
            queue.slots.filter { slot -> slot.date == nowDate.toLocalDate() }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = emptyList()
        )

    val summaryModelFlow: StateFlow<SummaryModel> =
        todaySlots.map { slots ->
            val redSlotsCount = slots.count { slot ->
                slot.state == SlotState.RED
            }
            val greenSlotsCount = slots.size - redSlotsCount
            SummaryModel(
                (redSlotsCount / 2F).toString(),
                (greenSlotsCount / 2F).toString()
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            SummaryModel.default()
        )

    private val timePeriodsWithStateUseCase = TimePeriodsWithStateUseCase()

    val periodsModelStateFlow: StateFlow<List<TimePeriodPresentation>> =
        combine(
            timeProvider.timeFlow,
            queueProvider.queueFlow
        ) { nowTime, queue ->
            calcPeriods(nowTime, queue)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            emptyList()
        )

    val extraInfoStateFlow: StateFlow<List<String>> =
        shortagesRepository.shortagesFlow.map { shortages ->
            val extra = mutableListOf<String>()

            if (shortages.isGav) {
                extra.add("GAV")
            }

            if (shortages.isSpecGav) {
                extra.add("SpecGAV")
            }

            extra
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            emptyList()
        )

    private fun calcPeriods(nowTime: LocalDateTime, queue: Queue): List<TimePeriodPresentation> {
        return queue.happyPeriods.filter {
            it.start.dayOfMonth == nowTime.dayOfMonth
        }.map { timePeriod ->
            val state = if (timePeriod.contains(nowTime))
                TimePeriodPresentationState.ACTIVE
            else if (timePeriod.end < nowTime)
                TimePeriodPresentationState.PAST
            else
                TimePeriodPresentationState.SOON

            TimePeriodPresentation(
                start = "${timePeriod.start.hour}:${timePeriod.start.minute}",
                end = "${timePeriod.end.hour}:${timePeriod.end.minute}",
                duration = "${timePeriod.durationInMinutes / 60F} hours",
                state = state
            )
        }
    }

    init {
        viewModelScope.launch {
            shortagesRepository.refresh()
        }
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

        for (i in 1 until slots.size) {
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

        val fromDateTime = prevSlot?.end ?: slots.first().start
        val toDateTime = nextSlot?.start ?: slots.last().end
        val remainingSeconds = max(0, Duration.between(now, toDateTime).seconds)

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

        val totalSeconds = max(1, Duration.between(fromDateTime, toDateTime).seconds)

        return TimerModel(
            isOn = isOn,
            time = "%02d:%02d".format(timePrefix, timeSuffix),
            date = "%02d.%02d.%04d".format(now.dayOfMonth, now.monthValue, now.year),
            total = totalSeconds.toFloat(),
            remaining = remainingSeconds.toFloat()
        )
    }
}
