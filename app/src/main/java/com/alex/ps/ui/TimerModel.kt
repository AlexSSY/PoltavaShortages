package com.alex.ps.ui

data class TimerModel(
    val isOn: Boolean,
    val time: String,
    val date: String,
    val total: Float,
    val remaining: Float
) {
    companion object {
        fun default(): TimerModel {
            return TimerModel(true, "FUCK", "OFF", 1F, 1F)
        }
    }
}