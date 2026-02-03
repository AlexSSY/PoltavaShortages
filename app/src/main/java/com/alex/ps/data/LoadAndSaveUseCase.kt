package com.alex.ps.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadAndSaveUseCase(
    val localRepository: LocalRepository,
    val shortagesLoader: suspend () -> Shortages
) {
    suspend operator fun invoke() {
        val downloaded = shortagesLoader()
        localRepository.save(downloaded)
    }
}