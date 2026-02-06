package com.alex.ps.data.settings

data class Settings(
    val theme: AppTheme,
    val language: AppLanguage,
    val selectedQueue: QueueKey
) {
    companion object {
        fun default(): Settings  = Settings(
            AppTheme.SYSTEM,
            AppLanguage.EN,
            QueueKey(1, 1)
        )
    }
}