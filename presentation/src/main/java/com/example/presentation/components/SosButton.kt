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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.core.ui.theme.EmergencyRed
import kotlinx.coroutines.delay

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