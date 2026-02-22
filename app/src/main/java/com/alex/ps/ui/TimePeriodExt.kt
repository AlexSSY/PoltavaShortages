package com.alex.ps.ui

import com.alex.ps.domain.TimePeriod
import com.alex.ps.ui.composables.TimePeriodPresentation
import com.alex.ps.ui.composables.TimePeriodPresentationState
import java.time.LocalDateTime

fun List<TimePeriod>.toPresentation(nowTime: LocalDateTime): List<TimePeriodPresentation> {
    return filter {
        it.start.dayOfMonth == nowTime.dayOfMonth
    }.map { timePeriod ->
        val state = if (timePeriod.contains(nowTime))
            TimePeriodPresentationState.ACTIVE
        else if (timePeriod.end < nowTime)
            TimePeriodPresentationState.PAST
        else
            TimePeriodPresentationState.SOON

        TimePeriodPresentation(
            start = formatTime(timePeriod.start.hour, timePeriod.start.minute),
            end = formatTime(timePeriod.end.hour, timePeriod.end.minute),
            duration = timePeriod.durationInMinutes / 60F,
            state = state
        )
    }
}