package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NexellaPurple

@Composable
fun NexellaBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(0.5.dp, Color(0xFFEEEEEE))
    ) {
        NavigationBar(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("nexella_bottom_nav"),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            val items = listOf(
                NavTabItem(0, "Início", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
                NavTabItem(1, "Descobrir", Icons.Filled.Explore, Icons.Outlined.Explore, "tab_explore"),
                NavTabItem(2, "Conectar", Icons.Filled.Handshake, Icons.Outlined.Handshake, "tab_connections"),
                NavTabItem(3, "Radar", Icons.Filled.Radar, Icons.Outlined.Radar, "tab_radar"),
                NavTabItem(4, "Perfil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
            )

            items.forEach { tab ->
                val isSelected = selectedTab == tab.index
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab.index) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                            contentDescription = tab.label
                        )
                    },
                    label = {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NexellaPurple,
                        selectedTextColor = NexellaPurple,
                        indicatorColor = NexellaPurple.copy(alpha = 0.12f),
                        unselectedIconColor = Color(0xFF8E8E93),
                        unselectedTextColor = Color(0xFF8E8E93)
                    ),
                    modifier = Modifier.testTag(tab.testTag)
                )
            }
        }
    }
}

private data class NavTabItem(
    val index: Int,
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
