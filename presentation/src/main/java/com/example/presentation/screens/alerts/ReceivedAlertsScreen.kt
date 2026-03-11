package com.example.presentation.screens.alerts

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.ui.theme.*
import com.example.domain.model.Alert
import com.example.presentation.components.SciFiStatusChip
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedAlertsScreen(
    viewModel: ReceivedAlertsViewModel = viewModel()
) {
    val alerts by viewModel.alerts.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var alertToDelete by remember { mutableStateOf<Alert?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBg, Color(0xFF1A1A2E))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header with title, clear all, and filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "INCOMING ALERTS",
                    color = SciFiBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Row {
                    // Clear all button (only show if there are alerts)
                    if (alerts.isNotEmpty()) {
                        IconButton(onClick = { showClearAllDialog = true }) {
                            Icon(
                                Icons.Default.DeleteSweep,
                                contentDescription = "Clear All",
                                tint = EmergencyRed.copy(alpha = 0.8f)
                            )
                        }
                    }
                    IconButton(onClick = { /* TODO: filter */ }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = SciFiBlue.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (alerts.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "NO ALERTS RECEIVED",
                            color = SciFiBlue.copy(alpha = 0.5f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "When someone triggers an SOS,\nit will appear here",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = alerts,
                        key = { alert -> alert.id ?: alert.hashCode() }
                    ) { alert ->
                        AnimatedAlertCard(
                            alert = alert,
                            onDelete = { alertToDelete = alert }
                        )
                    }
                }
            }
        }

        // Dialogs - placed at the end of the Box so they appear on top
        if (alertToDelete != null) {
            DeleteConfirmationDialog(
                alert = alertToDelete!!,
                onConfirm = {
                    coroutineScope.launch {
                        alertToDelete?.id?.let { id ->
                            viewModel.deleteAlert(id)
                        }
                        alertToDelete = null
                    }
                },
                onDismiss = { alertToDelete = null }
            )
        }

        if (showClearAllDialog) {
            ClearAllConfirmationDialog(
                onConfirm = {
                    coroutineScope.launch {
                        viewModel.clearAllAlerts()
                        showClearAllDialog = false
                    }
                },
                onDismiss = { showClearAllDialog = false }
            )
        }
    }
}



@Composable
fun AnimatedAlertCard(alert: Alert, onDelete: () -> Unit) {
    // Simple fade-in animation
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = tween(500)
        )
    ) {
        AlertCard(alert = alert, onDelete = onDelete)
    }
}

@Composable
fun AlertCard(alert: Alert, onDelete: () -> Unit) {
    val severityColor = when (alert.severity) {
        "DISASTER" -> EmergencyRed
        "MAJOR" -> Color(0xFFFF9800)
        else -> CyberGreen
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBg.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row: severity chip and online/offline indicator + delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Severity chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(severityColor.copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = alert.severity,
                        color = severityColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Online/offline indicator
                    SciFiStatusChip(
                        text = if (alert.is_offline) "P2P" else "ONLINE",
                        statusColor = if (alert.is_offline) SciFiPurple else CyberGreen,
                        modifier = Modifier.widthIn(min = 60.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = EmergencyRed.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // User ID (shortened)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = SciFiBlue.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "From: ${alert.user_id.take(8)}...",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = CyberGreen.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (alert.latitude != null && alert.longitude != null)
                        String.format(Locale.getDefault(), "%.4f, %.4f", alert.latitude, alert.longitude)
                    else
                        "Location unavailable",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timestamp
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = SciFiPurple.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatTimestamp(alert.timestamp),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}










@Composable
fun DeleteConfirmationDialog(
    alert: Alert,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Alert") },
        text = { Text("Are you sure you want to delete this alert from ${alert.user_id.take(8)}...?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = EmergencyRed
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ClearAllConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clear All Alerts") },
        text = { Text("Are you sure you want to delete all received alerts?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = EmergencyRed
                )
            ) {
                Text("Delete All")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}



// Helper to format timestamp (you can implement a proper formatter)
fun formatTimestamp(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return "Just now"
    // Simple example – you might want to parse and show relative time
    return timestamp.take(16) // just show first 16 chars for demo
}
