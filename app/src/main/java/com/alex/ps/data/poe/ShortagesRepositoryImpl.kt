package com.alex.ps.data.poe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alex.ps.domain.Shortages
import com.alex.ps.domain.ShortagesDataSource
import com.alex.ps.domain.ShortagesDiff
import com.alex.ps.domain.ShortagesRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Хранилище которое использует DataStore под капотом
 */
class ShortagesRepositoryImpl(
    val shortagesDataSource: ShortagesDataSource,
    val dataStore: DataStore<Preferences>
): ShortagesRepository {
    companion object{
        private val SHORTAGES_KEY = stringPreferencesKey("shortages")
    }

    override val shortagesFlow: Flow<Shortages?> =
        dataStore.data.map { prefs ->
            prefs[SHORTAGES_KEY]?.let(::decode)
        }

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private suspend fun save(shortages: Shortages) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            prefs[SHORTAGES_KEY] = encode(shortages)
        }
    }

    override suspend fun refresh(): ShortagesDiff {
        val freshShortages = shortagesDataSource.getShortages()
        save(freshShortages)
        return ShortagesDiff()
    }

    private fun encode(shortages: Shortages): String {
        return gson.toJson(shortages)
    }

    private fun decode(json: String): Shortages {
        return gson.fromJson(json, Shortages::class.java)
    }
}