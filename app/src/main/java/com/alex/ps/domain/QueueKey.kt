package com.alex.ps.domain

data class QueueKey(
    val major: Int,
    val minor: Int
) {
    companion object {
        fun default(): QueueKey {
            return QueueKey(1, 2)
        }
    }
}