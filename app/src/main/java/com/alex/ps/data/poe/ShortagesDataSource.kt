package com.alex.ps.data.poe

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException
import java.time.LocalDate
import kotlin.math.min

class ShortagesDataSource {
    companion object {
        private const val SLOTS_URL = "https://www.poe.pl.ua/customs/dynamicgpv-info.php"
        private const val GAV_URL = "https://www.poe.pl.ua/customs/dynamic-unloading-info.php"
    }

    suspend fun invoke(): Shortages = withContext(Dispatchers.IO) {
        val rawExtra = downloadJson()

        Shortages(
            isGav = rawExtra.contains("ГАВ"),
            isSpecGav = rawExtra.contains("СГАВ"),
            queues = fetchQueues()
        )
    }

    private fun fetchQueues(): List<Queue> {
        val document = getDocumentFromUrl()
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

    fun slotStateFromNumber(number: Int): SlotState =
        when(number) {
            1 -> SlotState.GREEN
            2 -> SlotState.RED
            3 -> SlotState.YELLOW
            else -> SlotState.GREEN
        }

    private suspend fun downloadSchedules(): List<Schedule> {
        val schedules: List<Schedule> = mutableListOf()



        return schedules
    }

    private fun getDocumentFromUrl(): Document {
        repeat(3) {
            try {
                val document = Jsoup.connect(SLOTS_URL).get()
                return document
            } catch (_: SocketTimeoutException) {
                // retry
                Log.w("getDocumentFromUrl", "Timeout")
            }
        }

        throw SocketTimeoutException()
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

    private fun downloadJson(): String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(GAV_URL)
            .get()
            .build()

        repeat(3) {
            try {
                return client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        throw Exception("HTTP error ${response.code}")
                    }
                    response.body?.string() ?: ""
                }
            } catch (e: Exception) {
                // retry
                println(e.message)
            }
        }

        throw Exception(
            "Failed to connect after 3 retries",
            SocketTimeoutException()
        )
    }
}