package com.alex.ps.ui

data class PeriodModel(
    val from: String,
    val duration: String,
    val to: String,
    val isPast: Boolean,
    val isActive: Boolean
) {
    companion object {
        fun default() = PeriodModel(
            from = "0:00",
            duration = "24 hours",
            to = "0:00",
            isPast = false,
            isActive = true
        )
    }
}
