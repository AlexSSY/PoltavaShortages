package com.alex.ps.data

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Хранилище которое использует DataStore для хранения
 */
class ShortagesRepositoryImpl(
    val shortagesDataSource: ShortagesDataSource,
    val dataStore: DataStore<Preferences>,
    scope: CoroutineScope
): ShortagesRepository {
    companion object{
        private val SHORTAGES_KEY = stringPreferencesKey("shortages")
    }

    override val shortagesFlow: StateFlow<Shortages> =
        dataStore.data.map { prefs ->
            prefs[SHORTAGES_KEY]?.let(::decode) ?: Shortages.default()
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(1_000),
            Shortages.default()
        )

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
        val freshShortages = try {
            shortagesDataSource.getShortages()
        } catch (e: Exception) {
            Log.e("Poe Repositry", "Failed to refresh", e)
            null
        }

        freshShortages?.let {
            save(it)
        }

        return ShortagesDiff()
    }

    private fun encode(shortages: Shortages): String {
        return gson.toJson(shortages)
    }

    private fun decode(json: String): Shortages {
        return gson.fromJson(json, Shortages::class.java)
    }
}