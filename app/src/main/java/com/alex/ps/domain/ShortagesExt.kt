package com.alex.ps.domain

import java.time.LocalDate

fun List<Schedule>.today(): Schedule? {
    val now = LocalDate.now()
    return firstOrNull { schedule -> schedule.date == now }
}

fun List<Schedule>.tomorrow(): Schedule? {
    val tomorrow = LocalDate.now().plusDays(1)
    return firstOrNull { schedule -> schedule.date == tomorrow }
}

fun List<Queue>.getBy(major: Int, minor: Int): Queue =
    first { it.major == major && it.minor == minor }