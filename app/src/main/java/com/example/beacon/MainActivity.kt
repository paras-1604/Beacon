

package com.example.beacon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beacon.ui.theme.BeaconTheme

// ---------------------------------------------------
// Severity Model
// ---------------------------------------------------
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

// ---------------------------------------------------
// Home Screen
// ---------------------------------------------------
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatusChip(internetStatus, Color.Red)
            StatusChip(gpsStatus, Color.Green)
            StatusChip(p2pStatus, Color.Yellow)
        }

        Spacer(Modifier.height(24.dp))

        SOSButton(
            onClick = {
                selectedSeverity = SeverityLevel.DISASTER
                showSheet = true
            }
        )

        Spacer(Modifier.height(16.dp))

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

    if (showSheet && selectedSeverity != null) {
        AlertConfirmationSheet(
            severity = selectedSeverity!!,
            onSend = {
                showSheet = false
                // TODO: final sending logic
            },
            onCancel = {
                showSheet = false
            }
        )
    }
}



// ---------------------------------------------------
// Components
// ---------------------------------------------------
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
onClick: () -> Unit = {
    var showSosDialog = true
}
)
 {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "SOS",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
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


// ---------------------------------------------------
// Confirmation Sheet UI
// ---------------------------------------------------
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



@Composable
fun SosSendDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Send SOS Alert?",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text("This will notify your trusted contacts with your location.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Send Now")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
