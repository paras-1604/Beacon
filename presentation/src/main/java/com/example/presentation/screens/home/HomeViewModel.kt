package com.example.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Location
import com.example.domain.model.SeverityLevel
import com.example.domain.repository.LocationRepository
import com.example.domain.repository.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    // Silent mode state
    private val _silentMode = MutableStateFlow(false)
    val silentMode: StateFlow<Boolean> = _silentMode.asStateFlow()

    // Location state
    private val _locationState = MutableStateFlow<LocationUiState>(LocationUiState.Loading)
    val locationState: StateFlow<LocationUiState> = _locationState.asStateFlow()

    // Countdown state
    private val _showCountdown = MutableStateFlow(false)
    val showCountdown: StateFlow<Boolean> = _showCountdown.asStateFlow()

    private val _countdownValue = MutableStateFlow(5)
    val countdownValue: StateFlow<Int> = _countdownValue.asStateFlow()

    // Toast state
    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()

    // Severity sheet state
    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet.asStateFlow()

    private val _selectedSeverity = MutableStateFlow<SeverityLevel?>(null)
    val selectedSeverity: StateFlow<SeverityLevel?> = _selectedSeverity.asStateFlow()

    init {
        // Collect silent mode preference
        viewModelScope.launch {
            preferencesRepository.silentMode.collect { isSilent ->
                _silentMode.value = isSilent
            }
        }

        // Collect location updates
        viewModelScope.launch {
            // First try last known location
            val lastKnown = locationRepository.getLastKnownLocation()

            Log.d("HomeViewModel", "Last known location: $lastKnown")

            if (lastKnown != null) {
                updateLocationState(lastKnown)
            } else {
                _locationState.value = LocationUiState.Unavailable
            }

            // Then live updates
            locationRepository.getLocationUpdates()
                .catch { e ->
                    Log.e("HomeViewModel", "Location error", e)
                    _locationState.value = LocationUiState.Unavailable

                }
                .collect { location ->

                    Log.d("HomeViewModel", "Live location: $location")
                    updateLocationState(location)
                }
        }
    }

    private fun updateLocationState(location: Location) {
        val accuracyLevel = when {
            location.accuracy < 10f -> AccuracyLevel.HIGH
            location.accuracy < 50f -> AccuracyLevel.MEDIUM
            else -> AccuracyLevel.LOW
        }
        _locationState.value = LocationUiState.Available(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracyMeters = location.accuracy,
            accuracyLevel = accuracyLevel
        )
    }

    fun onSosClick() {
        _showCountdown.value = true
        _countdownValue.value = 5
        startCountdown()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_countdownValue.value > 0 && _showCountdown.value) {
                delay(1000)
                _countdownValue.value -= 1
            }
            if (_countdownValue.value == 0) {
                _showCountdown.value = false
                _countdownValue.value = 5
                _showToast.value = true
                // TODO: Send alert
                delay(3000)
                _showToast.value = false
            }
        }
    }

    fun onCancelCountdown() {
        _showCountdown.value = false
        _countdownValue.value = 5
    }

    fun onSeverityClick(severity: SeverityLevel) {
        _selectedSeverity.value = severity
        _showSheet.value = true
    }

    fun onConfirmSheet() {
        _showSheet.value = false
        // TODO: Send alert with severity
    }

    fun onDismissSheet() {
        _showSheet.value = false
        _selectedSeverity.value = null
    }

    fun onDismissToast() {
        _showToast.value = false
    }

    fun toggleSilentMode() {
        viewModelScope.launch {
            val newValue = !_silentMode.value
            preferencesRepository.toggleSilentMode(newValue)
        }
    }
}

sealed class LocationUiState {
    object Unavailable : LocationUiState()
    object Loading : LocationUiState()
    data class Available(
        val latitude: Double,
        val longitude: Double,
        val accuracyMeters: Float,
        val accuracyLevel: AccuracyLevel
    ) : LocationUiState()
}

enum class AccuracyLevel {
    HIGH, MEDIUM, LOW
}