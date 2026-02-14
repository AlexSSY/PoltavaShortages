package com.alex.ps.domain

import java.time.LocalDate

data class Shortages(
    val isGav: Boolean,
    val isSpecGav: Boolean,
    val queues: List<Queue>
) {
    companion object {
        fun default(): Shortages {
            val queues = mutableListOf<Queue>()

            (1..6).forEach { major ->
                (1..2).forEach { minor ->
                    queues.add(
                        Queue(major, minor, emptyList(), emptyList())
                    )
                }
            }

            return Shortages(
                false,
                false,
                queues
            )
        }
    }
}