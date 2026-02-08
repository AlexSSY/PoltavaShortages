package com.alex.ps.domain

import com.alex.ps.data.poe.ShortagesRepositoryImpl

class LoadAndSaveUseCase(
    val shortagesRepositoryImpl: ShortagesRepositoryImpl,
    val shortagesProvider: suspend () -> Shortages
) {
    suspend operator fun invoke() {
        val downloaded = shortagesProvider()
        shortagesRepositoryImpl.save(downloaded)
    }
}