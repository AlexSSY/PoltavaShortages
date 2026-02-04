package com.alex.ps.data.poe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Хранилище которое использует DataStore под капотом
 */
class LocalRepository(
    val dataStore: DataStore<Preferences>
) {
    private val SHORTAGES_KEY = stringPreferencesKey("shortages")

    /**
     * Источник истины
     */
    val shortagesFlow: Flow<Shortages?> =
        dataStore.data.map { prefs ->
            prefs[SHORTAGES_KEY]?.let(::decode)
        }

    /**
     * Просто загружает данные из DataStore
     */
    suspend fun load(): Shortages? {
        TODO("implement!!!")
    }

    /**
     * Сохраняет данные полученные из сети в DataStore, и пушит их в shortagesFlow
     * как новые (актуальные) данные
     */
    suspend fun save(shortages: Shortages) = withContext(Dispatchers.IO) {
        dataStore.edit { prefs ->
            prefs[SHORTAGES_KEY] = encode(shortages)
        }
    }

    private fun encode(shortages: Shortages): String {
        TODO("JSON encode (kotlinx.serialization / Gson)")
    }

    private fun decode(json: String): Shortages {
        TODO("JSON decode")
    }
}