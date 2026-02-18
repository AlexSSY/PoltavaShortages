package com.alex.ps.domain

import com.alex.ps.domain.ThemeSetting

data class Settings(
    val theme: ThemeSetting,
    val language: LanguageSetting,
    val selectedQueue: QueueKey
) {
    companion object {
        fun default(): Settings = Settings(
            ThemeSetting.SYSTEM,
            LanguageSetting.UA,
            QueueKey.default()
        )
    }
}