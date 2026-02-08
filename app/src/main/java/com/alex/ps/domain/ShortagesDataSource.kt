package com.alex.ps.domain

interface ShortagesDataSource {
    suspend fun getShortages(): Shortages
}