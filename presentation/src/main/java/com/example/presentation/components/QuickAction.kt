package com.example.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.SciFiBlue
import com.example.core.ui.theme.SciFiPurple

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
