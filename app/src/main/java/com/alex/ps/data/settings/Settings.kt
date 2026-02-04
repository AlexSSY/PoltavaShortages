package com.alex.ps.data.settings

data class Settings(
    val theme: AppTheme,
    val language: AppLanguage,
    val selectedQueue: QueueKey
)
