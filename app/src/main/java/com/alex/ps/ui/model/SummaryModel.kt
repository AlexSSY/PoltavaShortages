package com.alex.ps.ui.model

data class SummaryModel(
    val redHours: String,
    var greenHours: String
) {
    companion object {
        fun default(): SummaryModel {
            return SummaryModel("0", "24")
        }
    }
}