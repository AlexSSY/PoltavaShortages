package com.alex.ps.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.ps.domain.Queue
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesRepository
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
import com.alex.ps.domain.getBy
import com.alex.ps.ui.model.SummaryModel
import com.alex.ps.ui.model.SummaryModelTomorrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class TomorrowViewModel(
    shortagesRepository: ShortagesRepository
): ViewModel() {
    private val sharing
        get() = SharingStarted.WhileSubscribed(1_000)

    private val shortages: StateFlow<Shortages> =
        shortagesRepository.shortagesFlow.stateIn(
            viewModelScope, sharing, Shortages.default()
        )

    private val activeQueue: StateFlow<Queue> =
        shortages.map { it.queues.getBy(1,2) }
            .stateIn(viewModelScope, sharing, Queue.default())

    private val todayDate: StateFlow<LocalDate> = flow {
        while (true) {
            emit(LocalDate.now())
            delay(1_000)
        }
    }.stateIn(
        viewModelScope, sharing, LocalDate.now()
    )

    private val tomorrowSlots: StateFlow<List<Slot>> =
        combine(todayDate, activeQueue) { date, queue ->
            queue.slots.filter { it.date == todayDate }
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
}