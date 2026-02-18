package com.alex.ps.ui

import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val shortagesRepository: ShortagesRepository,
    timeProvider: TimeProvider,
    queueProvider: QueueProvider
) : ViewModel() {
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

    val slotsAvailable: StateFlow<Boolean> =
        queueProvider.queueFlow.map { queue ->
            queue.slots.size == 48
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            false
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

    val periodsModelStateFlow: StateFlow<List<TimePeriodPresentation>> =
        combine(
            timeProvider.timeFlow,
            queueProvider.queueFlow
        ) { nowTime, queue ->
            queue.happyPeriods.toPresentation(nowTime)
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

    val tomorrowSlotsAvailableFlow: StateFlow<Boolean> =
        combine(
            queueProvider.queueFlow,
            timeProvider.timeFlow
        ) { queue, time ->
            queue.slots.any { it.date == time.plusDays(1).toLocalDate() }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(1_000),
            false
        )

    private val isRefreshing = MutableStateFlow(false)
    val isRefreshingFlow = isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            shortagesRepository.refresh()
            isRefreshing.value = false
        }
    }

    init {
        refresh()
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

        val prevSlotStateToFind = when (currentSlot.state) {
            SlotState.RED -> SlotState.GREEN
            SlotState.GREEN -> SlotState.YELLOW
            SlotState.YELLOW -> SlotState.RED
        }

        val nextSlotStateToFind = when (currentSlot.state) {
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

//        val fromDateTime = prevSlot?.end ?: slots.first().start
        val fromDateTime = prevSlot?.let {
            if (it.state == SlotState.YELLOW) it.start else it.end
        } ?: slots.first().start
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
            time = formatTime(timePrefix, timeSuffix),
            date = formatDate(now.dayOfMonth, now.monthValue, now.year),
            total = totalSeconds.toFloat(),
            remaining = remainingSeconds.toFloat()
        )
    }
}
