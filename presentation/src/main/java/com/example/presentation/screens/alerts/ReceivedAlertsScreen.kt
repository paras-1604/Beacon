package com.example.presentation.screens.alerts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.Alert

@Composable
fun ReceivedAlertsScreen(
    viewModel: ReceivedAlertsViewModel = viewModel()
) {
    // Using collectAsStateWithLifecycle for better lifecycle awareness
    val alerts by viewModel.alerts.collectAsStateWithLifecycle()

    if (alerts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No alerts received yet")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = alerts,
                key = { it.timestamp ?: it.hashCode() } // Provide keys for stable list updates
            ) { alert ->
                AlertCard(alert)
            }
        }
    }
}

@Composable
fun AlertCard(alert: Alert) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "⚠️ ${alert.severity} ALERT",
                style = MaterialTheme.typography.titleMedium,
                color = when (alert.severity) {
                    "DISASTER" -> MaterialTheme.colorScheme.error
                    "MAJOR" -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "From: ${alert.user_id.take(8)}...",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Location: ${alert.latitude ?: "unknown"}, ${alert.longitude ?: "unknown"}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Time: ${alert.timestamp ?: "unknown"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
