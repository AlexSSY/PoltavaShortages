package com.alex.ps.domain

import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface TimeProvider {
    val timeFlow: StateFlow<LocalDateTime>
}