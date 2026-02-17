package com.alex.ps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.ps.domain.Queue
import com.alex.ps.domain.QueueProvider
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
import com.alex.ps.domain.TimeProvider
import com.alex.ps.domain.getBy
import com.alex.ps.ui.composables.TimePeriodPresentation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class TomorrowViewModel(
    queueProvider: QueueProvider,
    timeProvider: TimeProvider
): ViewModel() {
    private val sharing
        get() = SharingStarted.WhileSubscribed(1_000)

    private val tomorrowSlots: StateFlow<List<Slot>> =
        combine(
            timeProvider.timeFlow,
            queueProvider.queueFlow
        ) { time, queue ->
            queue.slots.filter { it.date == time.toLocalDate() }
        }.stateIn(
            viewModelScope, sharing, emptyList()
        )

    val summaryModel: StateFlow<SummaryModelTomorrow> =
        tomorrowSlots.map { slots ->
            val redSlotsCount = slots.count { slot ->
                slot.state == SlotState.RED
            }
            val greenSlotsCount = slots.size - redSlotsCount
            SummaryModelTomorrow(
                redSlotsCount / 2F,
                greenSlotsCount / 2F
            )
        }.stateIn(
            viewModelScope, sharing, SummaryModelTomorrow.default()
        )

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
}