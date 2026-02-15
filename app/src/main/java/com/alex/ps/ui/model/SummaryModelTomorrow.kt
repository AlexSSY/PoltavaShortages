package com.alex.ps.ui.model

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