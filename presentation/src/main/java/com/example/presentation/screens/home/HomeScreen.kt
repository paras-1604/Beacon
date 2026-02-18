package com.example.presentation.screens.home





import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.ui.theme.DarkBg
import com.example.core.ui.theme.SciFiBlue
import com.example.core.ui.theme.CyberGreen
import com.example.core.ui.theme.EmergencyRed
import  com.example.presentation.components.SciFiStatusChip
import  com.example.presentation.components.SciFiSOSButton
import  com.example.presentation.components.SciFiSeverityButton
import  com.example.presentation.components.SciFiQuickAction
import  com.example.presentation.components.NetworkNode
import  com.example.presentation.components.NetworkVisualization
import  com.example.presentation.sheets.SciFiCountdownOverlay
import  com.example.presentation.sheets.SciFiAlertToast
import  com.example.presentation.sheets.SciFiConfirmationSheet
import com.example.domain.model.SeverityLevel




@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val showCountdown by viewModel.showCountdown.collectAsState()
    val countdownValue by viewModel.countdownValue.collectAsState()
    val showToast by viewModel.showToast.collectAsState()
    val showSheet by viewModel.showSheet.collectAsState()
    val selectedSeverity by viewModel.selectedSeverity.collectAsState()

    // Animated background particles (if you want to keep them)
    val infiniteTransition = rememberInfiniteTransition()
    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBg, Color(0xFF1A1A2E))
                )
            )
    ) {
        // Background particles (optional)
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(20) { i ->
                val x = (size.width * (i / 20f) + particleOffset) % size.width
                val y = size.height * (i / 20f)
                drawCircle(
                    color = SciFiBlue.copy(alpha = 0.1f),
                    center = Offset(x.toFloat(), y.toFloat()),
                    radius = 2.dp.toPx()
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BEACON",
                        color = SciFiBlue,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Emergency Network v1.0",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }

                // Status chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SciFiStatusChip(
                        text = "ONLINE",
                        statusColor = CyberGreen,
                        modifier = Modifier.widthIn(min = 80.dp)
                    )
                    SciFiStatusChip(
                        text = "GPS ACTIVE",
                        statusColor = SciFiBlue,
                        modifier = Modifier.widthIn(min = 80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SOS Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "EMERGENCY ALERT",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )

                SciFiSOSButton(
                    onClick = viewModel::onSosClick,
                    isActive = showCountdown
                )

                Text(
                    text = "Tap for immediate assistance",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Severity Controls
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ALERT SEVERITY",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

                SciFiSeverityButton(
                    label = "MINOR - Assistance",
                    color = CyberGreen,
                    icon = "⚡",
                    onClick = { viewModel.onSeverityClick(SeverityLevel.MINOR) },
                    modifier = Modifier.fillMaxWidth()
                )

                SciFiSeverityButton(
                    label = "MAJOR - Emergency",
                    color = Color(0xFFFF9800),
                    icon = "⚠️",
                    onClick = { viewModel.onSeverityClick(SeverityLevel.MAJOR) },
                    modifier = Modifier.fillMaxWidth()
                )

                SciFiSeverityButton(
                    label = "DISASTER - Critical",
                    color = EmergencyRed,
                    icon = "🔥",
                    onClick = { viewModel.onSeverityClick(SeverityLevel.DISASTER) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SciFiQuickAction(
                    label = "HELP REQUEST",
                    icon = "🆘",
                    onClick = { /* TODO: custom help request */ },
                    modifier = Modifier.weight(1f)
                )

                SciFiQuickAction(
                    label = "CUSTOM MSG",
                    icon = "✏️",
                    onClick = { /* TODO: custom message */ },
                    modifier = Modifier.weight(1f)
                )
            }

            // Footer
            Text(
                text = "Offline P2P Ready • Encrypted • Anonymous",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        // Overlays
        if (showCountdown) {
            SciFiCountdownOverlay(
                countdownValue = countdownValue,
                totalTime = 5,
                onCancel = viewModel::onCancelCountdown
            )
        }

        if (showToast) {
            SciFiAlertToast(
                onDismiss = viewModel::onDismissToast
            )
        }

        if (showSheet && selectedSeverity != null) {
            SciFiConfirmationSheet(
                severity = selectedSeverity!!,
                onSend = viewModel::onConfirmSheet,
                onCancel = viewModel::onDismissSheet
            )
        }
    }
}