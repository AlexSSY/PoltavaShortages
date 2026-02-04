package com.alex.ps.data.poe

import java.time.LocalDate

data class Schedule(
    val date: LocalDate,
    val slots: List<Slot>
)
