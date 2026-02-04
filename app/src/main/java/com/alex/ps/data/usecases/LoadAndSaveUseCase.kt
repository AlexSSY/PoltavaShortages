package com.alex.ps.data.usecases

import com.alex.ps.data.poe.LocalRepository
import com.alex.ps.data.poe.Shortages

class LoadAndSaveUseCase(
    val localRepository: LocalRepository,
    val shortagesProvider: suspend () -> Shortages
) {
    suspend operator fun invoke() {
        val downloaded = shortagesProvider()
        localRepository.save(downloaded)
    }
}