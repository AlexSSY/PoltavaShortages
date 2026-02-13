package com.alex.ps.domain

import java.time.LocalDate

fun List<Slot>.slotsAt(localDate: LocalDate): List<Slot> {
    return filter { slot ->
        slot.date == localDate
    }
}