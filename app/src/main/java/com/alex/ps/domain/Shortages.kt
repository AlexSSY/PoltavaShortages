package com.alex.ps.domain

import java.time.LocalDate

data class Shortages(
    val isGav: Boolean,
    val isSpecGav: Boolean,
    val queues: List<Queue>
)