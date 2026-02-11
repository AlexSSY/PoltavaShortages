package com.alex.ps.domain

data class Queue(
    val major: Int,
    val minor: Int,
    val schedules: List<Schedule>,
    val happyPeriods: List<TimePeriod>
)
