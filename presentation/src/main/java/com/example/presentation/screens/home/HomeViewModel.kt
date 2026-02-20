package com.example.presentation.screens.home



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.PreferencesRepository
import com.example.domain.model.SeverityLevel
import com.yourorg.beacon.data.repository.PreferencesRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(preferencesRepository1: PreferencesRepositoryImpl) : ViewModel() {

    private val preferencesRepository: PreferencesRepository
        get() {
            TODO()
        }


    private val _showCountdown = MutableStateFlow(false)
    val showCountdown: StateFlow<Boolean> = _showCountdown.asStateFlow()

    private val _countdownValue = MutableStateFlow(5)
    val countdownValue: StateFlow<Int> = _countdownValue.asStateFlow()

    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast.asStateFlow()

    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet.asStateFlow()

    private val _selectedSeverity = MutableStateFlow<SeverityLevel?>(null)
    val selectedSeverity: StateFlow<SeverityLevel?> = _selectedSeverity.asStateFlow()

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
                // TODO: Send alert with severity (default MAJOR or selected)
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
        // TODO: Send alert with _selectedSeverity.value
    }

    fun onDismissSheet() {
        _showSheet.value = false
        _selectedSeverity.value = null
    }

    fun onDismissToast() {
        _showToast.value = false
    }
}