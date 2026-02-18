package com.alex.ps.ui

import java.time.LocalDate

fun formatDate(localDate: LocalDate) = "%02d.%02d.%04d".format(
    localDate.dayOfMonth, localDate.monthValue, localDate.year
)

fun formatDate(dayOfMonth: Int, month: Int, year: Int) =
    formatDate(LocalDate.of(year, month, dayOfMonth))

fun formatTime(hour: Int, minute: Int) = "%02d:%02d".format(
    hour, minute
)

fun formatTime(hour: Long, minute: Long) =
    formatTime(hour.toInt(), minute.toInt())

fun formatTimeVerbose(hoursAndMinutes: Float) {

}