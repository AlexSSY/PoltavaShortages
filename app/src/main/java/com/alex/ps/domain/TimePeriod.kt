package com.alex.ps.domain

import java.time.LocalDateTime

data class TimePeriod(
    val start: LocalDateTime,
    val durationInMinutes: Long
) {
    private val to: LocalDateTime
        get() = start.plusMinutes(durationInMinutes)

    fun contains(localDateTime: LocalDateTime): Boolean {
        return localDateTime < to && start < localDateTime
    }
}
