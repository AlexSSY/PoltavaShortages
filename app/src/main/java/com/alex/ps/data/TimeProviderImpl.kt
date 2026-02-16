package com.alex.ps.data

import com.alex.ps.domain.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import java.time.LocalDateTime

class TimeProviderImpl(
    private val scope: CoroutineScope
) : TimeProvider {

    override val timeFlow: StateFlow<LocalDateTime> =
        flow {
            while (currentCoroutineContext().isActive) {
                emit(LocalDateTime.now())
                delay(1_000)
            }
        }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LocalDateTime.now()
            )
}