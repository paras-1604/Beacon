package com.example.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.CyberGreen
import com.example.core.ui.theme.SciFiBlue
import com.example.core.ui.theme.SciFiPurple


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



