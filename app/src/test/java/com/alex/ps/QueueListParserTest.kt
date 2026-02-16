package com.alex.ps

import com.alex.ps.data.QueueListParser
import com.alex.ps.domain.Schedule
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
import com.alex.ps.domain.TimePeriod
import com.alex.ps.domain.getBy
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class QueueListParserTest {

    private val queueListParser = QueueListParser()

    @Test
    fun `parses 48 slots correctly`() {
        val html = loadTestHtml("slots_1.html")

        val result = queueListParser.parse(html)
        assertEquals(12, result.size)

        val queue12 = result.getBy(1, 2)
        val schedule: Schedule = queue12.schedules.first()
        assertEquals(LocalDate.of(2026, 2, 11), schedule.date)
        assertEquals(48, schedule.slots.size)

        val schedule_slots = schedule.slots

        val schedule1Map = """
            R Y G G G R R R R R R R R R R Y G G G R R R R R R R R R Y G G R R R R R R R R R Y G G R R R R R
        """.trimIndent()

        val schedule2Map = """
            R R R Y G G G G R R R R R R R R R R Y G G G R R R R R R R R R Y G G G R R R R R Y G G R R R R R
        """.trimIndent()

        val expectedSlots = schedule1Map
            .split(" ")
            .mapIndexed { idx, s ->
                Slot(
                    state = when (s) {
                        "R" -> SlotState.RED
                        "Y" -> SlotState.YELLOW
                        "G" -> SlotState.GREEN
                        else -> error("Unknown state")
                    },
                    i = idx
                )
            }

        assertEquals(
            expectedSlots, schedule_slots)

        val expectedTimePeriods = listOf(
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 11, 0, 30),
                durationInMinutes = 120L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 11, 7, 30),
                durationInMinutes = 120L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 11, 14, 0),
                durationInMinutes = 90L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 11, 20, 0),
                durationInMinutes = 90L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 12, 1, 30),
                durationInMinutes = 120L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 12, 9, 0),
                durationInMinutes = 90L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 12, 15, 0),
                durationInMinutes = 90L
            ),
            TimePeriod(
                start = LocalDateTime.of(2026, 2, 12, 21, 0),
                durationInMinutes = 90L
            ),
        )

        assertEquals(expectedTimePeriods, queue12.happyPeriods)
    }

    private fun loadTestHtml(name: String): String {
        return javaClass.classLoader!!
            .getResource(name)!!
            .readText()
    }
}