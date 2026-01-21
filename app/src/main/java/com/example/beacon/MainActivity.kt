//package com.example.beacon
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//import com.example.beacon.ui.theme.BeaconTheme
//
//// ------------------------------------------------------------
//// Severity Model
//// ------------------------------------------------------------
//enum class SeverityLevel {
//    MINOR, MAJOR, DISASTER
//}
//
//class MainActivity : ComponentActivity() {
//
//    private var p2pState = mutableStateOf("P2P: Idle")
//
//    private fun isInternetAvailable(): Boolean = false
//    private fun isGpsEnabled(): Boolean = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            BeaconTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    BeaconHomeScreen(
//                        onSendAlert = {},
//                        onCustomMessage = {},
//                        internetStatus = if (isInternetAvailable()) "Internet: ON" else "Internet: OFF",
//                        gpsStatus = if (isGpsEnabled()) "GPS: ON" else "GPS: OFF",
//                        p2pStatus = p2pState.value
//                    )
//                }
//            }
//        }
//    }
//}
//
//// ------------------------------------------------------------
//// Home Screen
//// ------------------------------------------------------------
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BeaconHomeScreen(
//    onSendAlert: () -> Unit,
//    onCustomMessage: () -> Unit,
//    internetStatus: String = "Internet: OFF",
//    gpsStatus: String = "GPS: ON",
//    p2pStatus: String = "P2P: Searching..."
//) {
//    var showSheet by remember { mutableStateOf(false) }
//    var selectedSeverity by remember { mutableStateOf<SeverityLevel?>(null) }
//    var showCountdown by remember { mutableStateOf(false) }
//    var countdownValue by remember { mutableStateOf(5) }
//    var showToast by remember { mutableStateOf(false) }
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
//            // Send with severity = MAJOR (as requested)
//
//            // Auto-hide toast after 3 seconds
//            delay(3000L)
//            showToast = false
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Status Chip Row
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            StatusChip(internetStatus, Color.Red)
//            StatusChip(gpsStatus, Color.Green)
//            StatusChip(p2pStatus, Color.Yellow)
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        // SOS Button with Countdown Overlay
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.size(220.dp)
//        ) {
//            SOSButton(
//                onClick = {
//                    if (!showCountdown) {
//                        showCountdown = true
//                        countdownValue = 5
//                    }
//                },
//                isActive = showCountdown
//            )
//
//            // Countdown Overlay
//            if (showCountdown) {
//                CountdownOverlay(
//                    countdownValue = countdownValue,
//                    totalTime = 5,
//                    onCancel = {
//                        showCountdown = false
//                        countdownValue = 5
//                    }
//                )
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // Severity Buttons
//        SeverityButtons(
//            onMinor = {
//                selectedSeverity = SeverityLevel.MINOR
//                showSheet = true
//            },
//            onMajor = {
//                selectedSeverity = SeverityLevel.MAJOR
//                showSheet = true
//            },
//            onDisaster = {
//                selectedSeverity = SeverityLevel.DISASTER
//                showSheet = true
//            }
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            QuickActionButton("Help Request", onSendAlert)
//            QuickActionButton("Custom Msg", onCustomMessage)
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        // Map Placeholder
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.LightGray),
//            contentAlignment = Alignment.Center
//        ) {
//            Text("Map Preview (Location)")
//        }
//    }
//
//    // Toast Notification for Alert Sent
//    if (showToast) {
//        AlertSentToast(
//            onDismiss = { showToast = false }
//        )
//    }
//
//    // Confirmation Sheet for Severity Buttons
//    if (showSheet && selectedSeverity != null) {
//        AlertConfirmationSheet(
//            severity = selectedSeverity!!,
//            onSend = {
//                showSheet = false
//                // TODO: Send severity-based alert
//            },
//            onCancel = {
//                showSheet = false
//            }
//        )
//    }
//}
//
//// ------------------------------------------------------------
//// Components
//// ------------------------------------------------------------
//@Composable
//fun QuickActionButton(label: String, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier.height(48.dp),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Text(label)
//    }
//}
//
//@Composable
//fun SOSButton(
//    onClick: () -> Unit,
//    isActive: Boolean = false
//) {
//    val buttonColor = if (isActive) Color(0xFFD32F2F) else Color.Red
//
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .size(200.dp)
//            .clip(CircleShape),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = buttonColor,
//            contentColor = Color.White
//        )
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "SOS",
//                style = MaterialTheme.typography.headlineLarge,
//                fontWeight = FontWeight.Bold
//            )
//            if (!isActive) {
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Hold 5s to send",
//                    style = MaterialTheme.typography.labelSmall,
//                    fontWeight = FontWeight.Normal
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CountdownOverlay(
//    countdownValue: Int,
//    totalTime: Int,
//    onCancel: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.7f))
//            .clip(CircleShape),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // Circular Progress
//            Box(
//                modifier = Modifier.size(120.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                // Background circle
//                Canvas(modifier = Modifier.fillMaxSize()) {
//                    drawCircle(
//                        color = Color.Gray.copy(alpha = 0.3f),
//                        style = Stroke(width = 8f, cap = StrokeCap.Round)
//                    )
//                }
//
//                // Progress circle
//                val progress = (totalTime - countdownValue) / totalTime.toFloat()
//                Canvas(modifier = Modifier.fillMaxSize()) {
//                    drawArc(
//                        color = Color.Red,
//                        startAngle = -90f,
//                        sweepAngle = 360f * progress,
//                        useCenter = false,
//                        style = Stroke(width = 8f, cap = StrokeCap.Round)
//                    )
//                }
//
//                // Countdown number
//                Text(
//                    text = "$countdownValue",
//                    style = MaterialTheme.typography.displayLarge,
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Sending SOS in $countdownValue...",
//                color = Color.White,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(
//                onClick = onCancel,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Gray,
//                    contentColor = Color.White
//                ),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Text("Cancel")
//            }
//        }
//    }
//}
//
//@Composable
//fun AlertSentToast(
//    onDismiss: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Transparent),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth(0.8f)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFF4CAF50).copy(alpha = 0.95f)
//            )
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "✓ Alert Sent",
//                    color = Color.White,
//                    style = MaterialTheme.typography.headlineSmall,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text(
//                    text = "SOS alert sent to trusted contacts and nearby users",
//                    color = Color.White.copy(alpha = 0.9f),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Severity: MAJOR",
//                    color = Color.White,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//                Button(
//                    onClick = onDismiss,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White,
//                        contentColor = Color(0xFF4CAF50)
//                    ),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text("OK")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun StatusChip(text: String, color: Color) {
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(50))
//            .background(color.copy(alpha = 0.2f))
//            .padding(horizontal = 12.dp, vertical = 8.dp)
//    ) {
//        Text(
//            text = text,
//            color = color,
//            fontWeight = FontWeight.Medium,
//            fontSize = 14.sp
//        )
//    }
//}
//
//@Composable
//fun SeverityButtons(
//    onMinor: () -> Unit,
//    onMajor: () -> Unit,
//    onDisaster: () -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(10.dp)
//    ) {
//        SeverityButton("Minor", Color(0xFF4CAF50), onMinor)
//        SeverityButton("Major", Color(0xFFFF9800), onMajor)
//        SeverityButton("Disaster", Color(0xFFF44336), onDisaster)
//    }
//}
//
//@Composable
//private fun SeverityButton(label: String, color: Color, onClick: () -> Unit) {
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = ButtonDefaults.buttonColors(containerColor = color)
//    ) {
//        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
//    }
//}
//
//// ------------------------------------------------------------
//// Confirmation Sheet UI (for Severity Buttons)
//// ------------------------------------------------------------
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AlertConfirmationSheet(
//    severity: SeverityLevel,
//    onSend: () -> Unit,
//    onCancel: () -> Unit
//) {
//    ModalBottomSheet(
//        onDismissRequest = onCancel
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(
//                text = "Confirm Alert",
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = "Severity: ${severity.name}",
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Medium
//            )
//
//            Text(
//                text = "Message: \"I need help.\"",
//                style = MaterialTheme.typography.bodyMedium
//            )
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(Color.LightGray),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Map Preview")
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Button(
//                    onClick = onCancel,
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Gray
//                    )
//                ) {
//                    Text("Cancel")
//                }
//
//                Button(
//                    onClick = onSend,
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.Red
//                    )
//                ) {
//                    Text("Send Now")
//                }
//            }
//        }
//    }
//}


package com.example.beacon

import android.os.Bundle
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ------------------------------------------------------------
// Custom Sci-Fi Color Palette
// ------------------------------------------------------------
val SciFiBlue = Color(0xFF00D4FF)
val SciFiPurple = Color(0xFF9D4EDD)
val EmergencyRed = Color(0xFFFF1744)
val CyberGreen = Color(0xFF00FFAB)
val DarkBg = Color(0xFF0A0A1A)
val CardBg = Color(0xFF151530)
val GlowBlue = Color(0xFF00D4FF).copy(alpha = 0.3f)

// ------------------------------------------------------------
// Severity Model
// ------------------------------------------------------------
enum class SeverityLevel {
    MINOR, MAJOR, DISASTER
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = SciFiBlue,
                    secondary = SciFiPurple,
                    background = DarkBg,
                    surface = CardBg,
                    onPrimary = Color.White,
                    onSecondary = Color.White,
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            ) {
                // Sci-fi gradient background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(DarkBg, Color(0xFF1A1A2E))
                            )
                        )
                ) {
                    BeaconHomeScreen(
                        onSendAlert = {},
                        onCustomMessage = {},
                        internetStatus = "ONLINE",
                        gpsStatus = "ACTIVE",
                        p2pStatus = "MESH: 3"
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------
// Animated Sci-Fi Components
// ------------------------------------------------------------

// Pulsing SOS Button
@Composable
fun SciFiSOSButton(
    onClick: () -> Unit,
    isActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .size(180.dp)
            .graphicsLayer {
                scaleX = if (isActive) 1f else pulseScale
                scaleY = if (isActive) 1f else pulseScale
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EmergencyRed.copy(alpha = glowAlpha),
                        EmergencyRed.copy(alpha = 0f)
                    ),
                    center = center,
                    radius = size.minDimension / 2
                ),
                center = center,
                radius = size.minDimension / 2
            )
        }

        // Main button
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(EmergencyRed, Color(0xFFD50000)),
                        center = Offset(70f, 70f),
                        radius = 140f
                    )
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, EmergencyRed)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SOS",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.offset(y = (-2).dp)
            )

            // Subtle inner glow
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    center = center,
                    radius = size.minDimension / 3
                )
            }
        }

        // Animated rings when active
        if (isActive) {
            val ringScale by rememberInfiniteTransition().animateFloat(
                initialValue = 1f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing)
                )
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = EmergencyRed.copy(alpha = 0.3f),
                    center = center,
                    radius = (size.minDimension / 2) * ringScale,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

// Sci-Fi Status Chip
@Composable
fun SciFiStatusChip(
    text: String,
    statusColor: Color,
    icon: String = "●",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = CardBg.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        statusColor.copy(alpha = 0.8f),
                        statusColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Animated dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )

            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// Sci-Fi Severity Button
@Composable
fun SciFiSeverityButton(
    label: String,
    color: Color,
    icon: String = "⚡",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.3f),
                        color.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(color, color.copy(alpha = 0.5f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onClick()
                    // Reset pressed state
                    coroutineScope.launch {
                        delay(300)
                        isPressed = false
                    }
                }
            )
            .graphicsLayer {
                scaleX = if (isPressed) 0.95f else 1f
                scaleY = if (isPressed) 0.95f else 1f
            }
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with glow
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    color = color,
                    fontSize = 14.sp
                )
            }

            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

// Animated Network Visualization
@Composable
fun NetworkVisualization(
    peerCount: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg.copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(SciFiBlue.copy(alpha = 0.3f), SciFiPurple.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Network nodes visualization
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                repeat(3) { index ->
                    NetworkNode(
                        isActive = index < peerCount,
                        pulsePhase = pulse + (index * 0.3f)
                    )
                }
            }

            // Connection lines
            Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
                val nodeSpacing = size.width / 4
                val yCenter = size.height / 2

                // Animated connection line
                drawLine(
                    brush = Brush.linearGradient(
                        colors = listOf(SciFiBlue, SciFiPurple)
                    ),
                    start = Offset(nodeSpacing, yCenter),
                    end = Offset(nodeSpacing * 3, yCenter),
                    strokeWidth = 2.dp.toPx(),
                    alpha = 0.6f
                )

                // Pulsing dot moving along the line
                val dotX = nodeSpacing + (nodeSpacing * 2) * pulse
                drawCircle(
                    color = CyberGreen,
                    center = Offset(dotX, yCenter),
                    radius = 4.dp.toPx()
                )
            }

            Text(
                text = "MESH NETWORK: $peerCount NODES",
                color = SciFiBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun NetworkNode(isActive: Boolean, pulsePhase: Float) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = if (isActive) 0.8f else 0.5f,
        targetValue = if (isActive) 1.2f else 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (isActive) listOf(
                        CyberGreen.copy(alpha = 0.8f),
                        CyberGreen.copy(alpha = 0.2f)
                    ) else listOf(
                        Color.Gray.copy(alpha = 0.3f),
                        Color.Gray.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = if (isActive) CyberGreen else Color.Gray.copy(alpha = 0.5f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = CyberGreen.copy(alpha = 0.3f),
                    center = center,
                    radius = size.minDimension / 2 * (0.5f + pulsePhase * 0.5f),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }
    }
}

// Quick Action Button with hover effect
@Composable
fun SciFiQuickAction(
    label: String,
    icon: String = "▷",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = CardBg,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        SciFiBlue.copy(alpha = if (isHovered) 0.8f else 0.3f),
                        SciFiPurple.copy(alpha = if (isHovered) 0.8f else 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .graphicsLayer {
                translationY = if (isHovered) (-2).dp.toPx() else 0f
            }
            .animateContentSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                color = SciFiBlue,
                fontSize = 18.sp
            )

            Text(
                text = label,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

// ------------------------------------------------------------
// Enhanced Home Screen
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeaconHomeScreen(
    onSendAlert: () -> Unit,
    onCustomMessage: () -> Unit,
    internetStatus: String = "ONLINE",
    gpsStatus: String = "ACTIVE",
    p2pStatus: String = "MESH: 3"
) {
    var showCustomMessageDialog by remember { mutableStateOf(false) }
    var showCustomMessageSentToast by remember { mutableStateOf(false) }
    var lastSentMessage by remember { mutableStateOf("") }

    var showSheet by remember { mutableStateOf(false) }
    var selectedSeverity by remember { mutableStateOf<SeverityLevel?>(null) }
    var showCountdown by remember { mutableStateOf(false) }
    var countdownValue by remember { mutableStateOf(5) }
    var showToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Launch countdown coroutine
    LaunchedEffect(key1 = showCountdown, key2 = countdownValue) {
        if (showCountdown && countdownValue > 0) {
            delay(1000L)
            countdownValue--
        } else if (showCountdown && countdownValue == 0) {
            // Countdown finished - send alert
            showCountdown = false
            countdownValue = 5
            showToast = true

            // TODO: Actual alert sending logic here
            // Send with severity = MAJOR

            // Auto-hide toast after 3 seconds
            delay(3000L)
            showToast = false
        }
    }

    // Animated background particles
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
            .background(Color.Transparent)
    ) {
        // Background particles
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

                // System status
                // System status
                // System status - Compact layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // Align to end
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Reduced spacing
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SciFiStatusChip(
                            text = internetStatus,
                            statusColor = CyberGreen,
                            icon = "●",
                            modifier = Modifier.widthIn(min = 80.dp) // Minimum width
                        )
                        SciFiStatusChip(
                            text = gpsStatus,
                            statusColor = SciFiBlue,
                            icon = "●",
                            modifier = Modifier.widthIn(min = 80.dp)
                        )
//                        SciFiStatusChip(
//                            text = p2pStatus,
//                            statusColor = SciFiPurple,
//                            icon = "●",
//                            modifier = Modifier.widthIn(min = 100.dp) // Slightly wider for MESH text
//                        )
                    }
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
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )

                SciFiSOSButton(
                    onClick = {
                        showCountdown = true
                        countdownValue = 5
                    },
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
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )

                SciFiSeverityButton(
                    label = "MINOR - Assistance",
                    color = CyberGreen,
                    icon = "⚡",
                    onClick = {
                        selectedSeverity = SeverityLevel.MINOR
                        showSheet = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SciFiSeverityButton(
                    label = "MAJOR - Emergency",
                    color = Color(0xFFFF9800),
                    icon = "⚠️",
                    onClick = {
                        selectedSeverity = SeverityLevel.MAJOR
                        showSheet = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SciFiSeverityButton(
                    label = "DISASTER - Critical",
                    color = EmergencyRed,
                    icon = "🔥",
                    onClick = {
                        selectedSeverity = SeverityLevel.DISASTER
                        showSheet = true
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

           // Spacer(modifier = Modifier.height(20.dp))

            // Network Visualization
//            NetworkVisualization(peerCount = 3)

            Spacer(modifier = Modifier.height(5.dp))

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SciFiQuickAction(
                    label = "HELP REQUEST",
                    icon = "🆘",
                    onClick = onSendAlert,
                    modifier = Modifier.weight(1f)
                )

                SciFiQuickAction(
                    label = "CUSTOM MSG",
                    icon = "✏️",
                    onClick = onCustomMessage,
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

        // Countdown Overlay
        if (showCountdown) {
            SciFiCountdownOverlay(
                countdownValue = countdownValue,
                totalTime = 5,
                onCancel = {
                    showCountdown = false
                    countdownValue = 5
                }
            )
        }

        // Toast Notification
        if (showToast) {
            SciFiAlertToast(
                onDismiss = { showToast = false }
            )
        }

        // Confirmation Sheet
        if (showSheet && selectedSeverity != null) {
            SciFiConfirmationSheet(
                severity = selectedSeverity!!,
                onSend = {
                    showSheet = false
                    // TODO: Send severity-based alert
                },
                onCancel = {
                    showSheet = false
                }
            )
        }


    }
}

// Sci-Fi Countdown Overlay
@Composable
fun SciFiCountdownOverlay(
    countdownValue: Int,
    totalTime: Int,
    onCancel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scanLine by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        // Scanning effect
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        EmergencyRed.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                ),
                topLeft = Offset(0f, size.height * scanLine - 50f),
                size = Size(size.width, 100f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // Countdown circle
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(EmergencyRed, EmergencyRed.copy(alpha = 0.3f))
                        ),
                        center = center,
                        radius = size.minDimension / 2 - 10,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Progress arc
                val progress = (totalTime - countdownValue) / totalTime.toFloat()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(EmergencyRed, CyberGreen)
                        ),
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Center display
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.8f))
                        .border(
                            width = 2.dp,
                            brush = Brush.radialGradient(
                                listOf(EmergencyRed, Color.Transparent)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$countdownValue",
                        color = EmergencyRed,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "ALERT ACTIVATION",
                    color = EmergencyRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Sending in $countdownValue...",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CardBg,
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Text("ABORT SEQUENCE")
                }
            }
        }
    }
}

// Sci-Fi Toast Notification
@Composable
fun SciFiAlertToast(
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        visible = false
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBg.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Success icon with animation
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(CyberGreen.copy(alpha = 0.3f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = CyberGreen,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "ALERT TRANSMITTED",
                        color = CyberGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "Emergency signal broadcast to network\n3 nodes reached • Encryption active",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider(
                        color = Color.White.copy(alpha = 0.2f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "SEVERITY",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "MAJOR",
                                color = Color(0xFFFF9800),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = "MODE",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = "P2P + CLOUD",
                                color = SciFiBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// Sci-Fi Confirmation Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SciFiConfirmationSheet(
    severity: SeverityLevel,
    onSend: () -> Unit,
    onCancel: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel,
        containerColor = CardBg,
        modifier = Modifier
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(SciFiBlue, SciFiPurple)),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CONFIRM ALERT",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when (severity) {
                                SeverityLevel.MINOR -> CyberGreen.copy(alpha = 0.2f)
                                SeverityLevel.MAJOR -> Color(0xFFFF9800).copy(alpha = 0.2f)
                                SeverityLevel.DISASTER -> EmergencyRed.copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = severity.name,
                        color = when (severity) {
                            SeverityLevel.MINOR -> CyberGreen
                            SeverityLevel.MAJOR -> Color(0xFFFF9800)
                            SeverityLevel.DISASTER -> EmergencyRed
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailRow(
                    label = "MESSAGE",
                    value = "I need emergency assistance",
                    color = SciFiBlue
                )

                DetailRow(
                    label = "LOCATION",
                    value = "GPS coordinates acquired",
                    color = CyberGreen
                )

                DetailRow(
                    label = "BROADCAST",
                    value = when (severity) {
                        SeverityLevel.MINOR -> "Trusted contacts only"
                        SeverityLevel.MAJOR -> "Local network + cloud"
                        SeverityLevel.DISASTER -> "Full epidemic spread"
                    },
                    color = SciFiPurple
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("CANCEL")
                }

                Button(
                    onClick = onSend,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when (severity) {
                            SeverityLevel.MINOR -> CyberGreen
                            SeverityLevel.MAJOR -> Color(0xFFFF9800)
                            SeverityLevel.DISASTER -> EmergencyRed
                        },
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("SEND ALERT")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Column {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            letterSpacing = 1.sp
        )
        Text(
            text = value,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}