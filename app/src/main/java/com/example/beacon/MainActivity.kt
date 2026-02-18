package com.example.beacon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.lifecycleScope
import com.example.data.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.core.ui.theme.*
import com.example.domain.model.SeverityLevel
import com.example.presentation.components.SciFiSOSButton
import com.example.presentation.components.SciFiStatusChip
import com.example.presentation.components.SciFiSeverityButton
import com.example.presentation.components.SciFiQuickAction
import com.example.presentation.screens.auth.AuthScreen
import com.example.presentation.screens.home.HomeScreen
import com.example.presentation.sheets.SciFiCountdownOverlay
import com.example.presentation.sheets.SciFiAlertToast
import com.example.presentation.sheets.SciFiConfirmationSheet
import com.example.presentation.sheets.DetailRow






class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeaconTheme {
                var isAuthenticated by remember { mutableStateOf(false) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    authRepository.signInAnonymously().onSuccess {
                        isAuthenticated = true
                    }.onFailure {
                        // Handle error (maybe show a message)
                    }
                    isLoading = false
                }

                when {
                    isLoading -> {
                        // Show a simple loading indicator
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text("Loading...", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    isAuthenticated -> {
                        HomeScreen()
                    }
                    else -> {
                        AuthScreen()
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BeaconHomeScreen(
//    onSendAlert: () -> Unit,
//    onCustomMessage: () -> Unit,
//    internetStatus: String = "ONLINE",
//    gpsStatus: String = "ACTIVE",
//    p2pStatus: String = "MESH: 3"
//) {
//    var showCustomMessageDialog by remember { mutableStateOf(false) }
//    var showCustomMessageSentToast by remember { mutableStateOf(false) }
//    var lastSentMessage by remember { mutableStateOf("") }
//
//    var showSheet by remember { mutableStateOf(false) }
//    var selectedSeverity by remember { mutableStateOf<SeverityLevel?>(null) }
//    var showCountdown by remember { mutableStateOf(false) }
//    var countdownValue by remember { mutableStateOf(5) }
//    var showToast by remember { mutableStateOf(false) }
//    val coroutineScope = rememberCoroutineScope()
//
//    // Launch countdown coroutine
//    LaunchedEffect(key1 = showCountdown, key2 = countdownValue) {
//        if (showCountdown && countdownValue > 0) {
//            delay(1000L)
//            countdownValue--
//        } else if (showCountdown && countdownValue == 0) {
//            // Countdown finished - send alert
//            showCountdown = false
//            countdownValue = 5
//            showToast = true
//
//            // TODO: Actual alert sending logic here
//            // Send with severity = MAJOR
//
//            // Auto-hide toast after 3 seconds
//            delay(3000L)
//            showToast = false
//        }
//    }
//
//    // Animated background particles
//    val infiniteTransition = rememberInfiniteTransition()
//    val particleOffset by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 100f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(5000, easing = LinearEasing)
//        )
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Transparent)
//    ) {
//        // Background particles
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            repeat(20) { i ->
//                val x = (size.width * (i / 20f) + particleOffset) % size.width
//                val y = size.height * (i / 20f)
//                drawCircle(
//                    color = SciFiBlue.copy(alpha = 0.1f),
//                    center = Offset(x, y),
//                    radius = 2.dp.toPx()
//                )
//            }
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // Header with status
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text(
//                        text = "BEACON",
//                        color = SciFiBlue,
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        letterSpacing = 2.sp
//                    )
//                    Text(
//                        text = "Emergency Network v1.0",
//                        color = Color.White.copy(alpha = 0.6f),
//                        fontSize = 12.sp
//                    )
//                }
//
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End, // Align to end
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Reduced spacing
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        SciFiStatusChip(
//                            text = internetStatus,
//                            statusColor = CyberGreen,
//                            icon = "●",
//                            modifier = Modifier.widthIn(min = 80.dp) // Minimum width
//                        )
//                        SciFiStatusChip(
//                            text = gpsStatus,
//                            statusColor = SciFiBlue,
//                            icon = "●",
//                            modifier = Modifier.widthIn(min = 80.dp)
//                        )
//
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // SOS Section
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                Text(
//                    text = "EMERGENCY ALERT",
//                    color = Color.White.copy(alpha = 0.7f),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    letterSpacing = 1.sp
//                )
//
//                SciFiSOSButton(
//                    onClick = {
//                        showCountdown = true
//                        countdownValue = 5
//                    },
//                    isActive = showCountdown
//                )
//
//                Text(
//                    text = "Tap for immediate assistance",
//                    color = Color.White.copy(alpha = 0.5f),
//                    fontSize = 12.sp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Severity Controls
//            Column(
//                verticalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "ALERT SEVERITY",
//                    color = Color.White.copy(alpha = 0.7f),
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Medium,
//                    letterSpacing = 1.sp,
//                    modifier = Modifier.padding(start = 4.dp)
//                )
//
//                SciFiSeverityButton(
//                    label = "MINOR - Assistance",
//                    color = CyberGreen,
//                    icon = "⚡",
//                    onClick = {
//                        selectedSeverity = SeverityLevel.MINOR
//                        showSheet = true
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                SciFiSeverityButton(
//                    label = "MAJOR - Emergency",
//                    color = Color(0xFFFF9800),
//                    icon = "⚠️",
//                    onClick = {
//                        selectedSeverity = SeverityLevel.MAJOR
//                        showSheet = true
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                SciFiSeverityButton(
//                    label = "DISASTER - Critical",
//                    color = EmergencyRed,
//                    icon = "🔥",
//                    onClick = {
//                        selectedSeverity = SeverityLevel.DISASTER
//                        showSheet = true
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//           // Spacer(modifier = Modifier.height(20.dp))
//
//            // Network Visualization
////            NetworkVisualization(peerCount = 3)
//
//            Spacer(modifier = Modifier.height(5.dp))
//
//            // Quick Actions
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                SciFiQuickAction(
//                    label = "HELP REQUEST",
//                    icon = "🆘",
//                    onClick = onSendAlert,
//                    modifier = Modifier.weight(1f)
//                )
//
//                SciFiQuickAction(
//                    label = "CUSTOM MSG",
//                    icon = "✏️",
//                    onClick = onCustomMessage,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//
//            // Footer
//            Text(
//                text = "Offline P2P Ready • Encrypted • Anonymous",
//                color = Color.White.copy(alpha = 0.4f),
//                fontSize = 10.sp,
//                modifier = Modifier.padding(top = 20.dp)
//            )
//        }
//
//        // Countdown Overlay
//        if (showCountdown) {
//            SciFiCountdownOverlay(
//                countdownValue = countdownValue,
//                totalTime = 5,
//                onCancel = {
//                    showCountdown = false
//                    countdownValue = 5
//                }
//            )
//        }
//
//        // Toast Notification
//        if (showToast) {
//            SciFiAlertToast(
//                onDismiss = { showToast = false }
//            )
//        }
//
//        // Confirmation Sheet
//        if (showSheet && selectedSeverity != null) {
//            SciFiConfirmationSheet(
//                severity = selectedSeverity!!,
//                onSend = {
//                    showSheet = false
//                    // TODO: Send severity-based alert
//                },
//                onCancel = {
//                    showSheet = false
//                }
//            )
//        }
//
//
//    }
//}

