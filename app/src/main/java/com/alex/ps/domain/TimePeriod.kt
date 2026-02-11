package com.alex.ps.domain

import java.time.LocalDateTime

data class TimePeriod(
    val start: LocalDateTime,
    val durationInMinutes: Long
)
