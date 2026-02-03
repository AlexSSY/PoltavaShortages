package com.alex.ps.data

import java.time.LocalDate

data class Shortages(
    val extra: List<String>,
    val schedules: List<Schedule>
)

fun List<Schedule>.today(): Schedule? {
    val now = LocalDate.now()
    return firstOrNull { schedule -> schedule.date == now }
}

fun List<Schedule>.tomorrow(): Schedule? {
    val tomorrow = LocalDate.now().plusDays(1)
    return firstOrNull { schedule -> schedule.date == tomorrow }
}