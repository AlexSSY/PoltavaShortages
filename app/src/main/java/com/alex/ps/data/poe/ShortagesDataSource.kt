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

class ShortagesDataSource {
    companion object {
        private val SLOTS_URL = "https://www.poe.pl.ua/customs/dynamicgpv-info.php"
        private val GAV_URL = "https://www.poe.pl.ua/customs/dynamic-unloading-info.php"
    }

    suspend fun load(): Shortages = withContext(Dispatchers.IO) {
        val rawExtra = fetchExtraInfo()

        Shortages(
            isGav = rawExtra.contains("ГАВ"),
            isSpecGav = rawExtra.contains("СГАВ"),
            queues = fetchQueues()
        )
    }

    private suspend fun fetchQueues(): List<Queue> {
        val document = getDocumentFromUrl()

        val allQueues: List<Queue> = listOf(
            Queue(1, 1, emptyList()),
            Queue(1, 2, emptyList()),
            Queue(2, 1, emptyList()),
            Queue(2, 2, emptyList()),
            Queue(3, 1, emptyList()),
            Queue(3, 2, emptyList()),
            Queue(4, 1, emptyList()),
            Queue(4, 2, emptyList()),
            Queue(5, 1, emptyList()),
            Queue(5, 2, emptyList()),
            Queue(6, 1, emptyList()),
            Queue(6, 2, emptyList()),
        )

        return allQueues
    }

    private suspend fun parseQueue(, major: Int, minor: Int) {

    }

    private suspend fun downloadSchedules(): List<Schedule> {
        val schedules: List<Schedule> = mutableListOf()



        return schedules
    }

    private suspend fun fetchExtraInfo(): List<String> {
        val rawJson = downloadJson()
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
            } catch (_: Exception) {
                // retry
            }
        }

        throw Exception(
            "Failed to connect after 3 retries",
            SocketTimeoutException()
        )
    }
}