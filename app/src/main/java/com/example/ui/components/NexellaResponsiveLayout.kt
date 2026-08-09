package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.NexellaBackground
import com.example.ui.theme.NexellaBlush
import com.example.ui.theme.NexellaBorder
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark
import com.example.ui.theme.NexellaRose
import com.example.ui.theme.NexellaRoseLight
import com.example.ui.theme.NexellaSubtext

/**
 * Responsive layout container implementing Nexella's modern 'feminine-empowering' brand design system.
 * Adapts between mobile compact, medium tablet, and expanded desktop screen sizes.
 */
@Composable
fun NexellaResponsiveLayout(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    currentUser: UserEntity?,
    onOpenElla: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(NexellaBackground)
            .testTag("nexella_responsive_layout_root")
    ) {
        val isExpanded = maxWidth >= 840.dp
        val isMedium = maxWidth in 600.dp..839.dp

        if (isExpanded) {
            // Expanded Tablet / Desktop Layout with Side Navigation Rail + Main Bounded Container
            Row(modifier = Modifier.fillMaxSize()) {
                // Side Rail Navigation
                NexellaSideNavRail(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected,
                    onOpenElla = onOpenElla
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    // Header Bar with Empowering Accent Top Gradient Line
                    EmpoweringTopAccentLine()

                    NexellaHeader(
                        currentUser = currentUser,
                        onOpenElla = onOpenElla,
                        onOpenProfile = onOpenProfile
                    )

                    // Main Content Container (Center-Aligned Max Width)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(NexellaBackground),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 1200.dp)
                                .fillMaxSize()
                                .padding(horizontal = 24.dp)
                        ) {
                            content(PaddingValues(bottom = 16.dp))
                        }
                    }

                    // Rich Empowering Footer for Wide Screens
                    NexellaEmpoweringFooter(
                        onTabSelected = onTabSelected,
                        onOpenElla = onOpenElla
                    )
                }
            }
        } else if (isMedium) {
            // Medium Layout (Large Phones / Small Tablets)
            Column(modifier = Modifier.fillMaxSize()) {
                EmpoweringTopAccentLine()

                NexellaHeader(
                    currentUser = currentUser,
                    onOpenElla = onOpenElla,
                    onOpenProfile = onOpenProfile
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(NexellaBackground),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .widthIn(max = 720.dp)
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        content(PaddingValues(bottom = 8.dp))
                    }
                }

                NexellaBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        } else {
            // Compact Mobile Layout
            Column(modifier = Modifier.fillMaxSize()) {
                EmpoweringTopAccentLine()

                NexellaHeader(
                    currentUser = currentUser,
                    onOpenElla = onOpenElla,
                    onOpenProfile = onOpenProfile
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(NexellaBackground)
                ) {
                    content(PaddingValues(bottom = 0.dp))
                }

                NexellaBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        }
    }
}

/**
 * Modern Feminine Accent Gradient Bar (Nexella Purple + Rose + Gold)
 */
@Composable
fun EmpoweringTopAccentLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        NexellaPurple,
                        NexellaRose,
                        NexellaGold
                    )
                )
            )
            .testTag("empowering_accent_top_line")
    )
}

/**
 * Responsive Side Navigation Rail for Large Screen Devices
 */
@Composable
fun NexellaSideNavRail(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onOpenElla: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(80.dp)
            .testTag("nexella_side_nav_rail"),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, NexellaBorder)
    ) {
        NavigationRail(
            containerColor = Color.White,
            header = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(NexellaPurple, NexellaRose)
                                )
                            )
                            .clickable { onOpenElla() }
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Ella AI Mentora",
                                tint = NexellaPurple,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Text(
                        text = "Ella AI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        ) {
            val tabs = listOf(
                RailTabItem(0, "Início", Icons.Filled.Home, Icons.Outlined.Home, "rail_tab_home"),
                RailTabItem(1, "Descobrir", Icons.Filled.Explore, Icons.Outlined.Explore, "rail_tab_explore"),
                RailTabItem(2, "Conectar", Icons.Filled.Handshake, Icons.Outlined.Handshake, "rail_tab_connections"),
                RailTabItem(3, "Radar", Icons.Filled.Radar, Icons.Outlined.Radar, "rail_tab_radar"),
                RailTabItem(4, "Perfil", Icons.Filled.Person, Icons.Outlined.Person, "rail_tab_profile")
            )

            tabs.forEach { tab ->
                val isSelected = selectedTab == tab.index
                NavigationRailItem(
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
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = NexellaPurple,
                        selectedTextColor = NexellaPurple,
                        indicatorColor = NexellaRoseLight,
                        unselectedIconColor = NexellaSubtext,
                        unselectedTextColor = NexellaSubtext
                    ),
                    modifier = Modifier.testTag(tab.testTag)
                )
            }
        }
    }
}

/**
 * Rich Empowering Footer Component for Expanded Screens / App Overview
 */
@Composable
fun NexellaEmpoweringFooter(
    onTabSelected: (Int) -> Unit,
    onOpenElla: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("nexella_empowering_footer"),
        color = Color.White,
        border = BorderStroke(1.dp, NexellaBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = 1200.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Column: Brand Statement & Mission
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NEXELLA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = NexellaPurple,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = NexellaRoseLight
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = NexellaRose,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Cascavel/PR",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NexellaRose,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }

                    Text(
                        text = "Conectando e impulsionando mulheres empreendedoras através do networking ativo, radar de oportunidades e mentoria inteligente.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = NexellaSubtext,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        modifier = Modifier.padding(top = 4.dp, end = 24.dp)
                    )
                }

                // Middle Column: Quick Nav Links
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FooterLinkChip(label = "Início", onClick = { onTabSelected(0) })
                    FooterLinkChip(label = "Comunidade", onClick = { onTabSelected(1) })
                    FooterLinkChip(label = "Conexões", onClick = { onTabSelected(2) })
                    FooterLinkChip(label = "Radar Nexella", onClick = { onTabSelected(3) })
                    FooterLinkChip(label = "Ella Mentora AI", onClick = { onOpenElla() })
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                color = NexellaBorder
            )

            Row(
                modifier = Modifier
                    .widthIn(max = 1200.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "© 2026 Nexella Cascavel • Redes de Empreendedorismo Feminino",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Privacidade Garantida",
                        tint = NexellaGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Plataforma Segura & Verificada",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NexellaGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FooterLinkChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NexellaBlush,
        border = BorderStroke(1.dp, NexellaRose.copy(alpha = 0.2f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = NexellaPurpleDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private data class RailTabItem(
    val index: Int,
    val label: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
