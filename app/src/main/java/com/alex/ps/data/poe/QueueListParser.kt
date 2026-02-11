package com.alex.ps.data.poe

import com.alex.ps.domain.Queue
import com.alex.ps.domain.Schedule
import com.alex.ps.domain.Slot
import com.alex.ps.domain.SlotState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.time.LocalDate

class QueueListParser {
    fun parse(html: String): List<Queue>{
        val document = Jsoup.parse(html)
        val allQueues = mutableListOf<Queue>()

        (1..6).forEach { major ->
            (1..2).forEach { minor ->
                allQueues.add(
                    Queue(major, minor, parseSchedules(document, major, minor))
                )
            }
        }

        return allQueues
    }

    private fun parseSchedules(document: Document, major: Int, minor: Int): List<Schedule> {
        val schedules = mutableListOf<Schedule>()
        val gpvDivs = document.select(".gpvinfodetail")
        val dateNumbers = mutableMapOf<LocalDate, List<Int>>()

        for (gpvDiv in gpvDivs) {

            // parse date
            val dateElement = gpvDiv.selectFirst("b")
                ?: throw Exception("Failed to parse dateString")
            val dateString = dateElement.text()
            val date = parseUaDate(dateString)

            // parse line of slots [red, green, yellow]
            val trIndex = (major -1) * 2 + minor
            val tr = gpvDiv.selectFirst(
                ".turnoff-scheduleui-table > tbody:nth-child(2) > tr:nth-child($trIndex)"
            ) ?: throw Exception("Failed to parse tr")

            val numbers = try {
                tr.select("""td[class^="light_"]""").map { td ->
                    val firstClass = td.className().split(" ").first()
                    firstClass.split("light_").last().toInt()
                }
            } catch (e: NoSuchElementException) {
                throw Exception("Failed to parse numbers", e)
            }
            dateNumbers[date] = numbers
        }

        dateNumbers.forEach { (date, numbers) ->
            val slots = mutableListOf<Slot>()
            numbers.forEachIndexed { idx, number ->
                slots.add(
                    Slot(slotStateFromNumber(number), idx)
                )
            }
            schedules.add(
                Schedule(date, slots)
            )
        }

        return schedules
    }

    private fun parseUaDate(uaDateString: String): LocalDate {
        val months = hashMapOf(
            "січня" to 1,
            "лютого" to 2,
            "березня" to 3,
            "квітня" to 4,
            "травня" to 5,
            "червня" to 6,
            "липня" to 7,
            "серпня" to 8,
            "вересня" to 9,
            "жовтня" to 10,
            "листопада" to 11,
            "грудня" to 12,
        )

        val (dayString, monthName, yearString, _) = uaDateString.split(" ")
        val day = dayString.toInt()
        val month = months[monthName] ?: 1
        val year = yearString.toInt()
        return LocalDate.of(year, month, day)
    }

    fun slotStateFromNumber(number: Int): SlotState =
        when(number) {
            1 -> SlotState.GREEN
            2 -> SlotState.RED
            3 -> SlotState.YELLOW
            else -> SlotState.GREEN
        }
}