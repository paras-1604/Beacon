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
import kotlinx.coroutines.launch

class ReceivedAlertsViewModel(
    private val alertRepository: AlertRepository,
    private val currentUserId: String
) : ViewModel() {

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts.asStateFlow()

    init {
        // Observe alerts from the repository (Room)
        alertRepository.getReceivedAlerts()
            .onEach { alerts ->
                val filtered = alerts
                    .filter { it.user_id != currentUserId }
                    .take(50)
                _alerts.value = filtered
                Log.d("ReceivedAlertsVM", "Received ${alerts.size} alerts, filtered to ${filtered.size}")
            }
            .catch { e ->
                Log.e("ReceivedAlertsVM", "Error observing alerts", e)
            }
            .launchIn(viewModelScope)

        // Also observe live alerts to keep Room updated (if needed)
        alertRepository.observeNewAlerts()
            .launchIn(viewModelScope)
    }

    fun deleteAlert(id: Long) {
        viewModelScope.launch {
            alertRepository.deleteAlert(id)
        }
    }

    fun clearAllAlerts() {
        viewModelScope.launch {
            alertRepository.clearAllAlerts()
        }
    }
}