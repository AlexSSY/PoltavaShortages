package com.alex.ps.domain

data class Queue(
    val major: Int,
    val minor: Int,
    val slots: List<Slot>,

    // optional
    val happyPeriods: List<TimePeriod>
)
