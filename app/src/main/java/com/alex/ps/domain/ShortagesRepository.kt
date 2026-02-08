package com.alex.ps.domain

import kotlinx.coroutines.flow.Flow

interface ShortagesRepository {
    /**
     * Источник истинны
     */
    val shortagesFlow: Flow<Shortages?>

    /**
     * Обновить граффик и получить изменения
     * @return Разница между полученными данными из сети и локальными
     */
    suspend fun refresh(): ShortagesDiff
}