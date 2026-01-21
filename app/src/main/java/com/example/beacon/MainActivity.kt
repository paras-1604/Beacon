//
//
//package com.example.beacon
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.beacon.ui.theme.BeaconTheme
//
//// ---------------------------------------------------
//// Severity Model
//// ---------------------------------------------------
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
//// ---------------------------------------------------
//// Home Screen
//// ---------------------------------------------------
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
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//
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
//        SOSButton(
//            onClick = {
//                selectedSeverity = SeverityLevel.DISASTER
//                showSheet = true
//            }
//        )
//
//        Spacer(Modifier.height(16.dp))
//
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
//    if (showSheet && selectedSeverity != null) {
//        AlertConfirmationSheet(
//            severity = selectedSeverity!!,
//            onSend = {
//                showSheet = false
//                // TODO: final sending logic
//            },
//            onCancel = {
//                showSheet = false
//            }
//        )
//    }
//}
//
//
//
//// ---------------------------------------------------
//// Components
//// ---------------------------------------------------
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
//onClick: () -> Unit = {
//    var showSosDialog = true
//}
//)
// {
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .size(200.dp)
//            .clip(CircleShape),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Red,
//            contentColor = Color.White
//        )
//    ) {
//        Text(
//            text = "SOS",
//            style = MaterialTheme.typography.headlineLarge,
//            fontWeight = FontWeight.Bold
//        )
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
//
//// ---------------------------------------------------
//// Confirmation Sheet UI
//// ---------------------------------------------------
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
//
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
//
//
//
//@Composable
//fun SosSendDialog(
//    onConfirm: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(
//                text = "Send SOS Alert?",
//                fontWeight = FontWeight.Bold
//            )
//        },
//        text = {
//            Text("This will notify your trusted contacts with your location.")
//        },
//        confirmButton = {
//            Button(
//                onClick = onConfirm,
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Red
//                )
//            ) {
//                Text("Send Now")
//            }
//        },
//        dismissButton = {
//            Button(
//                onClick = onDismiss
//            ) {
//                Text("Cancel")
//            }
//        }
//    )
//}










package com.example.beacon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.beacon.ui.theme.BeaconTheme

// ------------------------------------------------------------
// Severity Model
// ------------------------------------------------------------
enum class SeverityLevel {
    MINOR, MAJOR, DISASTER
}

class MainActivity : ComponentActivity() {

    private var p2pState = mutableStateOf("P2P: Idle")

    private fun isInternetAvailable(): Boolean = false
    private fun isGpsEnabled(): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BeaconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BeaconHomeScreen(
                        onSendAlert = {},
                        onCustomMessage = {},
                        internetStatus = if (isInternetAvailable()) "Internet: ON" else "Internet: OFF",
                        gpsStatus = if (isGpsEnabled()) "GPS: ON" else "GPS: OFF",
                        p2pStatus = p2pState.value
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------
// Home Screen
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeaconHomeScreen(
    onSendAlert: () -> Unit,
    onCustomMessage: () -> Unit,
    internetStatus: String = "Internet: OFF",
    gpsStatus: String = "GPS: ON",
    p2pStatus: String = "P2P: Searching..."
) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedSeverity by remember { mutableStateOf<SeverityLevel?>(null) }
    var showCountdown by remember { mutableStateOf(false) }
    var countdownValue by remember { mutableStateOf(5) }
    var showToast by remember { mutableStateOf(false) }

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
            // Send with severity = MAJOR (as requested)

            // Auto-hide toast after 3 seconds
            delay(3000L)
            showToast = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Status Chip Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusChip(internetStatus, Color.Red)
            StatusChip(gpsStatus, Color.Green)
            StatusChip(p2pStatus, Color.Yellow)
        }

        Spacer(Modifier.height(24.dp))

        // SOS Button with Countdown Overlay
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(220.dp)
        ) {
            SOSButton(
                onClick = {
                    if (!showCountdown) {
                        showCountdown = true
                        countdownValue = 5
                    }
                },
                isActive = showCountdown
            )

            // Countdown Overlay
            if (showCountdown) {
                CountdownOverlay(
                    countdownValue = countdownValue,
                    totalTime = 5,
                    onCancel = {
                        showCountdown = false
                        countdownValue = 5
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Severity Buttons
        SeverityButtons(
            onMinor = {
                selectedSeverity = SeverityLevel.MINOR
                showSheet = true
            },
            onMajor = {
                selectedSeverity = SeverityLevel.MAJOR
                showSheet = true
            },
            onDisaster = {
                selectedSeverity = SeverityLevel.DISASTER
                showSheet = true
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton("Help Request", onSendAlert)
            QuickActionButton("Custom Msg", onCustomMessage)
        }

        Spacer(Modifier.height(20.dp))

        // Map Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Map Preview (Location)")
        }
    }

    // Toast Notification for Alert Sent
    if (showToast) {
        AlertSentToast(
            onDismiss = { showToast = false }
        )
    }

    // Confirmation Sheet for Severity Buttons
    if (showSheet && selectedSeverity != null) {
        AlertConfirmationSheet(
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

// ------------------------------------------------------------
// Components
// ------------------------------------------------------------
@Composable
fun QuickActionButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(label)
    }
}

@Composable
fun SOSButton(
    onClick: () -> Unit,
    isActive: Boolean = false
) {
    val buttonColor = if (isActive) Color(0xFFD32F2F) else Color.Red

    Button(
        onClick = onClick,
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SOS",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            if (!isActive) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hold 5s to send",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun CountdownOverlay(
    countdownValue: Int,
    totalTime: Int,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Circular Progress
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.Gray.copy(alpha = 0.3f),
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                }

                // Progress circle
                val progress = (totalTime - countdownValue) / totalTime.toFloat()
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.Red,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 8f, cap = StrokeCap.Round)
                    )
                }

                // Countdown number
                Text(
                    text = "$countdownValue",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sending SOS in $countdownValue...",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun AlertSentToast(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50).copy(alpha = 0.95f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✓ Alert Sent",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SOS alert sent to trusted contacts and nearby users",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Severity: MAJOR",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SeverityButtons(
    onMinor: () -> Unit,
    onMajor: () -> Unit,
    onDisaster: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SeverityButton("Minor", Color(0xFF4CAF50), onMinor)
        SeverityButton("Major", Color(0xFFFF9800), onMajor)
        SeverityButton("Disaster", Color(0xFFF44336), onDisaster)
    }
}

@Composable
private fun SeverityButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

// ------------------------------------------------------------
// Confirmation Sheet UI (for Severity Buttons)
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertConfirmationSheet(
    severity: SeverityLevel,
    onSend: () -> Unit,
    onCancel: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onCancel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Confirm Alert",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Severity: ${severity.name}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Message: \"I need help.\"",
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Map Preview")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = onSend,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Send Now")
                }
            }
        }
    }
}
