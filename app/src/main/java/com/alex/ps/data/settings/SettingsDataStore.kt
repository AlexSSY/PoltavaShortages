package com.alex.ps.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val THEME = stringPreferencesKey("theme")
        private val LANGUAGE = stringPreferencesKey("language")
        private val QUEUE_MAJOR = intPreferencesKey("queue_major")
        private val QUEUE_MINOR = intPreferencesKey("queue_minor")
    }

    val settingsFlow: Flow<Settings> =
        dataStore.data.map { prefs ->

            val theme = prefs[THEME]
                ?.let { runCatching { ThemeSetting.valueOf(it) }.getOrNull() }
                ?: ThemeSetting.SYSTEM

            val language = prefs[LANGUAGE]
                ?.let { runCatching { LanguageSetting.valueOf(it) }.getOrNull() }
                ?: LanguageSetting.SYSTEM

            val queue = prefs[QUEUE_MAJOR]?.let { major ->
                prefs[QUEUE_MINOR]?.let { minor ->
                    QueueKey(major, minor)
                }
            } ?: QueueKey(1, 1)

            Settings(
                theme = theme,
                language = language,
                selectedQueue = queue
            )
        }

    suspend fun setTheme(theme: ThemeSetting) {
        dataStore.edit { it[THEME] = theme.name }
    }

    suspend fun setLanguage(language: LanguageSetting) {
        dataStore.edit { it[LANGUAGE] = language.name }
    }

    suspend fun setQueue(queue: QueueKey) {
        dataStore.edit { prefs ->
            prefs[QUEUE_MAJOR] = queue.major
            prefs[QUEUE_MINOR] = queue.minor
        }
    }
}