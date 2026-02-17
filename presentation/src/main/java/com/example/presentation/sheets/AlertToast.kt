package com.example.presentation.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.CyberGreen
import com.example.core.ui.theme.SciFiBlue
import kotlinx.coroutines.delay


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
