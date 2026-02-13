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
        val end = LocalDateTime.of(
            date,
            LocalTime.ofSecondOfDay(0)
        ).plusMinutes(30L * i)
        val start = end.minusMinutes(30L)
        return localDateTime <= end && localDateTime > start
    }
}
