package com.example.presentation.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.CyberGreen
import com.example.core.ui.theme.EmergencyRed
import com.example.core.ui.theme.SciFiBlue
import com.example.core.ui.theme.SciFiPurple
import com.example.domain.model.SeverityLevel







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
