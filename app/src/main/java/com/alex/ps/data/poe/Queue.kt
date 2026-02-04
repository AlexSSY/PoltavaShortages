package com.alex.ps.data.poe

data class Queue(
    val major: Int,
    val minor: Int,
    val schedules: List<Schedule>
)
