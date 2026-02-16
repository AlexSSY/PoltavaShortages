package com.alex.ps.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveQueueUseCase(
    val shortagesRepository: ShortagesRepository,
    val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Queue> =
        combine(
            shortagesRepository.shortagesFlow,
            settingsRepository.settingsFlow
        ) { shortages, settings ->
            shortages.queues.getBy(
                settings.selectedQueue.major,
                settings.selectedQueue.minor
            )
        }
}