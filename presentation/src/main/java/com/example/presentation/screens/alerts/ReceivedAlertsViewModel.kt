package com.example.presentation.screens.alerts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AlertRepository
import com.example.domain.model.Alert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReceivedAlertsViewModel(
    private val alertRepository: AlertRepository,
    private val currentUserId: String
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts.asStateFlow()

    init {
        startObserving()
    }

    private fun startObserving() {
        alertRepository.observeNewAlerts()
            .onEach { alert ->
                Log.d("ReceivedAlertsVM", "New Alert in VM: $alert (Current User: $currentUserId)")
                // Prevent duplicate display of own alerts
                if (alert.user_id != currentUserId) {
                    _alerts.value = (listOf(alert) + _alerts.value).take(50) // Keep newest first, limit 50
                }
            }
            .catch { e ->
                Log.e("ReceivedAlertsVM", "Flow error", e)
            }
            .launchIn(viewModelScope)
    }
}
