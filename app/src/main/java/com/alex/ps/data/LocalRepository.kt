package com.alex.ps.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalRepository {
    val scheduleFlow: Flow<List<Schedule>> = flow {}

    init {
        loadFromDB()
    }

    private fun loadFromDB() {

    }
}