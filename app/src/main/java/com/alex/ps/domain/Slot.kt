package com.alex.ps.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Slot(
    val state: SlotState,
    val i: Int,
    val date: LocalDate
) {
    fun intersect(localDateTime: LocalDateTime): Boolean {
        return localDateTime <= end && localDateTime > start
    }

    val start: LocalDateTime
        get() {
            return date.atStartOfDay().plusMinutes(30L * i)
        }

    val end: LocalDateTime
        get() {
            return date.atStartOfDay().plusMinutes(30L * i + 30L)
        }
}
