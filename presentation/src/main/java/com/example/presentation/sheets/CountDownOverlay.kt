package com.example.presentation.sheets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.CyberGreen
import com.example.core.ui.theme.EmergencyRed

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
