package com.alex.ps.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    suspend fun setTheme(theme: ThemeSetting)
    suspend fun setLanguage(language: LanguageSetting)
    suspend fun setQueue(queue: QueueKey)
}