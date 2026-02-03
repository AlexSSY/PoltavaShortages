package com.alex.ps.data

import java.time.LocalDate

data class Schedule(
    val queue: Float,
    val date: LocalDate,
    val slots: List<Slot>
)
