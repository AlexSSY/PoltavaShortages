package com.alex.ps.data.settings

data class Settings(
    val theme: ThemeSetting,
    val language: LanguageSetting,
    val selectedQueue: QueueKey
) {
    companion object {
        fun default(): Settings  = Settings(
            ThemeSetting.SYSTEM,
            LanguageSetting.EN,
            QueueKey(1, 1)
        )
    }
}