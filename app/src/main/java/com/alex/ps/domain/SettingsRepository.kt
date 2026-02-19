package com.alex.ps.domain

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settingsFlow: StateFlow<Settings>
    suspend fun setTheme(theme: ThemeSetting)
    suspend fun setLanguage(language: LanguageSetting)
    suspend fun setQueue(queue: QueueKey)
}