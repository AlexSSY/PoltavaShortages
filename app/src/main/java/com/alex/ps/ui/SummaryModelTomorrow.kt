package com.alex.ps.ui

data class SummaryModelTomorrow(
    val redHours: Float,
    var greenHours: Float
) {
    companion object {
        fun default(): SummaryModelTomorrow {
            return SummaryModelTomorrow(0F, 24F)
        }
    }
}