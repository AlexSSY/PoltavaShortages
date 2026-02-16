package com.alex.ps.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.time.LocalDateTime

class ObserveTimeUseCase(
    val emitRate: Long = 1_000L
) {
    operator fun invoke(): Flow<LocalDateTime> =
        flow {
            while (currentCoroutineContext().isActive) {
                emit(LocalDateTime.now())
                delay(emitRate)
            }
        }
}