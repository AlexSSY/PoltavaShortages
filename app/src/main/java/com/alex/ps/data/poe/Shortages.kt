package com.alex.ps.data.poe

import java.time.LocalDate

data class Shortages(
    val isGav: Boolean,
    val isSpecGav: Boolean,
    val queues: List<Queue>
)

fun List<Schedule>.today(): Schedule? {
    val now = LocalDate.now()
    return firstOrNull { schedule -> schedule.date == now }
}

fun List<Schedule>.tomorrow(): Schedule? {
    val tomorrow = LocalDate.now().plusDays(1)
    return firstOrNull { schedule -> schedule.date == tomorrow }
}

fun List<Queue>.getOrNull(major: Int, minor: Int): Queue? =
    firstOrNull { it.major == major && it.minor == minor }