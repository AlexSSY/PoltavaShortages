package com.alex.ps.domain

import kotlinx.coroutines.flow.StateFlow

interface ShortagesRepository {
    /**
     * Источник истинны
     */
    val shortagesFlow: StateFlow<Shortages>

    /**
     * Обновить граффик и получить изменения
     * @return Разница между полученными данными из сети и локальными
     */
    suspend fun refresh(): ShortagesDiff
}