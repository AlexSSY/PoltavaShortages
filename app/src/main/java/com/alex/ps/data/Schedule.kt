package com.alex.ps.data

import java.time.LocalDate

data class Schedule(
    val date: LocalDate,
    val slots: List<Slot>
)
