package com.alex.ps

import com.alex.ps.data.poe.QueueListParser
import com.alex.ps.domain.Schedule
import com.alex.ps.domain.getOrNull
import org.junit.Assert.assertEquals
import org.junit.Test

class QueueListParserTest {

    private val queueListParser = QueueListParser()

    @Test
    fun `parses 48 bricks correctly`() {
        val html = loadTestHtml("slots_1.html")

        val result = queueListParser.parse(html)

        assertEquals(12, result.size)

        val schedule: Schedule? = result.getOrNull(1, 2)?.schedules.first()

        assertEquals(
            expected = 48,
            actual = ?.schedules.first().slots.size ?: 0
        )
    }

    private fun loadTestHtml(name: String): String {
        return javaClass.classLoader!!
            .getResource(name)!!
            .readText()
    }
}