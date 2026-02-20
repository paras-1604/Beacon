package com.example.domain.repository


import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val silentMode: Flow<Boolean>
    suspend fun toggleSilentMode(enabled: Boolean)
}