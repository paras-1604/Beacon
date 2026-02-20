package com.example.presentation.components



import com.example.core.ui.theme.SciFiBlue

import com.example.core.ui.theme.CardBg

import com.example.core.ui.theme.SciFiPurple

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "HOME"),
        NavItem("contacts", Icons.Default.People, "CONTACTS"),
        NavItem("settings", Icons.Default.Settings, "SETTINGS")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)  // slightly reduced height
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            CardBg.copy(alpha = 0.9f),
                            Color(0xFF1E1E3F).copy(alpha = 0.9f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(SciFiBlue.copy(alpha = 0.6f), SciFiPurple.copy(alpha = 0.6f))
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    BottomNavItem(
                        item = item,
                        isSelected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                SciFiBlue.copy(alpha = 0.3f),
                SciFiPurple.copy(alpha = 0.3f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }

    val contentColor = if (isSelected) SciFiBlue else Color.White.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.33f)  // One third of parent width
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(brush = backgroundColor)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = SciFiBlue.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(24.dp)
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = item.label,
                color = contentColor,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)