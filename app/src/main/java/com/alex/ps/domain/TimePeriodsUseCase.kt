package com.alex.ps.domain

import java.time.LocalDateTime

class TimePeriodsWithStateUseCase(
    val queue: Queue,
    val nowTime: LocalDateTime
) {
    operator fun invoke(): List<TimePeriodWithState> {
        return queue.happyPeriods
            .filter { it.start.dayOfMonth == nowTime.dayOfMonth }
            .map { period ->

                val state = when {
                    period.contains(nowTime) -> TimePeriodState.ACTIVE
                    period.end < nowTime -> TimePeriodState.PAST
                    else -> TimePeriodState.SOON
                }

                TimePeriodWithState(
                    period = period,
                    state = state
                )
            }
    }
}