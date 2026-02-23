package com.alex.ps.data

import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesDataSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException

class PoeShortagesDataSource(
    val queueListParser: QueueListParser
): ShortagesDataSource {
    companion object {
        private const val SLOTS_URL = "https://www.poe.pl.ua/customs/dynamicgpv-info.php"
        private const val GAV_URL = "https://www.poe.pl.ua/customs/dynamic-unloading-info.php"
    }

    override suspend fun getShortages(): Shortages = withContext(Dispatchers.IO) {
        val unloadingJson = downloadUrl(GAV_URL)
        val gavHtml = downloadUrl(SLOTS_URL)

        val unloadingList = unloadingToDataClassList(unloadingJson)
        val isGav = unloadingList.any { it.unloadingtypename == "ГАВ" }
        val isSpecGav = unloadingList.any { it.unloadingtypename == "СГАВ" }

        val queues = queueListParser.parse(gavHtml)

        Shortages(isGav, isSpecGav, queues)
    }

    private fun unloadingToDataClassList(unloadingJson: String): List<Unloading> {
        val listType = object : TypeToken<List<Unloading>>() {}.type
        return Gson().fromJson(unloadingJson, listType)
    }

    private fun downloadUrl(url: String): String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
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