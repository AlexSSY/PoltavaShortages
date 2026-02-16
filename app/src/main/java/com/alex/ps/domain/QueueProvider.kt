package com.alex.ps.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QueueProvider(
    val shortagesRepository: ShortagesRepository,
    val settingsRepository: SettingsRepository,
    scope: CoroutineScope
) {
    val queueFlow: StateFlow<Queue> =
        combine(
            shortagesRepository.shortagesFlow,
            settingsRepository.settingsFlow
        ) { shortages, settings ->
            val major = settings.selectedQueue.major
            val minor = settings.selectedQueue.minor
            shortages.queues.getBy(major, minor)
        }
            .stateIn(
                scope,
                SharingStarted.WhileSubscribed(1_000),
                Queue.default()
            )

}