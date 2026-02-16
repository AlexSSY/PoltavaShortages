package com.alex.ps.data.poe

import com.alex.ps.domain.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime

class TimeProviderImpl(
    private val scope: CoroutineScope
) : TimeProvider {

    override val timeFlow: StateFlow<LocalDateTime> =
        flow {
            while (true) {
                emit(LocalDateTime.now())
                delay(1000)
            }
        }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LocalDateTime.now()
            )
}