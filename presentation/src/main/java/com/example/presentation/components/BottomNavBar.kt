package com.example.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core.ui.theme.CardBg
import com.example.core.ui.theme.SciFiBlue

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "HOME"),
        NavItem("contacts", Icons.Default.People, "CONTACTS"),
        NavItem("alerts", Icons.Default.Notifications, "ALERTS"),  // new item
//        NavItem("settings", Icons.Default.Settings, "SETTINGS")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(CardBg)
            .border(
                width = 1.dp,
                color = SciFiBlue.copy(alpha = 0.4f),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            // REMOVED: horizontalArrangement = Arrangement.SpaceEvenly
            // We use fixed width items instead for stability
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

@Composable
fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) SciFiBlue else Color.White.copy(alpha = 0.5f)
    val backgroundColor = if (isSelected) SciFiBlue.copy(alpha = 0.15f) else Color.Transparent

    Box(
        modifier = Modifier
            .width(100.dp) // FIXED WIDTH: Ensures all 3 items fit evenly
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
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
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}



data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)