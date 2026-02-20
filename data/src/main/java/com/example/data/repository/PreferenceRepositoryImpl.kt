package com.yourorg.beacon.data.repository

import com.example.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PreferencesRepositoryImpl : PreferencesRepository {
    private val _silentMode = MutableStateFlow(false)
    override val silentMode: Flow<Boolean> = _silentMode
    override suspend fun toggleSilentMode(enabled: Boolean) {
        _silentMode.value = enabled
    }
}