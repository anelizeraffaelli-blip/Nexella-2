package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.UserEntity
import com.example.ui.AdminMetrics
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleLight

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.mutableIntStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAdminScreen(
    currentUser: UserEntity?,
    allUsers: List<UserEntity>,
    adminMetrics: AdminMetrics,
    onSwitchUser: (UserEntity) -> Unit,
    onOpenRegisterModal: () -> Unit,
    onOpenEditProfileModal: (() -> Unit)? = null,
    onApproveUser: (Long) -> Unit,
    onSuspendUser: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Meu Perfil, 1 = Gestão & Admin
    var switchUserExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(Color(0xFFF7F7F9))) {
        // Top Tab Row
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = Color.White,
            contentColor = NexellaPurple,
            indicator = { tabPositions ->
                if (selectedSubTab < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                        color = NexellaPurple,
                        height = 3.dp
                    )
                }
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Text(
                        text = "Meu Perfil",
                        fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedSubTab == 0) NexellaPurple else Color(0xFF666666)
                    )
                },
                modifier = Modifier.testTag("tab_sub_profile")
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = {
                    Text(
                        text = "Gestão & Admin",
                        fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedSubTab == 1) NexellaPurple else Color(0xFF666666)
                    )
                },
                modifier = Modifier.testTag("tab_sub_admin")
            )
        }

        if (selectedSubTab == 0) {
            ProfileScreen(
                user = currentUser,
                onEditProfileClick = onOpenEditProfileModal
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                // Screen Title
                item {
                    Text(
                        text = "Painel de Gestão & Métricas",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NexellaPurple
                        )
                    )
                }

        // Active Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_active_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = currentUser?.photoUrl
                                ?: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80",
                            contentDescription = currentUser?.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentUser?.name ?: "Membro Nexella",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF222222)
                        )
                    )

                    Text(
                        text = "${currentUser?.businessName} • ${currentUser?.neighborhood}, Cascavel",
                        style = MaterialTheme.typography.bodyMedium.copy(color = NexellaPurple, fontWeight = FontWeight.SemiBold)
                    )

                    if (!currentUser?.creci.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE3F2FD))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CRECI ${currentUser?.creci}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF1976D2),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch Logged-in User (For Testing MVP)
                    ExposedDropdownMenuBox(
                        expanded = switchUserExpanded,
                        onExpandedChange = { switchUserExpanded = !switchUserExpanded }
                    ) {
                        OutlinedTextField(
                            value = "Alternar Usuária Ativa: ${currentUser?.name}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Simular Acesso de Outra Empreendedora") },
                            leadingIcon = { Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = switchUserExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = switchUserExpanded,
                            onDismissRequest = { switchUserExpanded = false }
                        ) {
                            allUsers.forEach { u ->
                                DropdownMenuItem(
                                    text = { Text("${u.name} (${u.businessName})") },
                                    onClick = {
                                        onSwitchUser(u)
                                        switchUserExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (onOpenEditProfileModal != null) {
                            androidx.compose.material3.OutlinedButton(
                                onClick = onOpenEditProfileModal,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("admin_edit_profile_button"),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, NexellaPurple)
                            ) {
                                Text("Editar Perfil Ativo", color = NexellaPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Button(
                            onClick = onOpenRegisterModal,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("profile_register_modal_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cadastrar Nova", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // --- PAINEL ADMINISTRATIVO NEXELLA ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_dashboard_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = NexellaPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Painel Administrativo Nexella",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NexellaPurple
                            )
                        )
                    }

                    Text(
                        text = "Métricas em tempo real de Cascavel",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBox(
                            count = adminMetrics.totalUsers.toString(),
                            label = "Usuárias",
                            icon = Icons.Default.People,
                            color = NexellaPurple,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            count = adminMetrics.totalCorretoras.toString(),
                            label = "Corretoras",
                            icon = Icons.Default.HomeWork,
                            color = NexellaGold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBox(
                            count = adminMetrics.totalConnections.toString(),
                            label = "Conexões",
                            icon = Icons.Default.Handshake,
                            color = Color(0xFF0288D1),
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            count = adminMetrics.totalSuccessfulDeals.toString(),
                            label = "Negócios Gerados 🚀",
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricBox(
                            count = adminMetrics.totalOpportunities.toString(),
                            label = "Radar Oportunidades",
                            icon = Icons.Default.Radar,
                            color = Color(0xFFE65100),
                            modifier = Modifier.weight(1f)
                        )
                        MetricBox(
                            count = adminMetrics.totalMeetings.toString(),
                            label = "Encontros",
                            icon = Icons.Default.Stars,
                            color = NexellaPurple,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Moderation List: Users Management
        item {
            Text(
                text = "Moderação & Aprovação de Membros",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NexellaPurple
                )
            )
        }

        items(allUsers) { u ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = u.photoUrl,
                        contentDescription = u.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = u.name,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${u.businessName} • Status: ${u.status}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                    }

                    if (u.status == "Pendente") {
                        Button(
                            onClick = { onApproveUser(u.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Aprovar", style = MaterialTheme.typography.labelSmall)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = u.status,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
    }
    }
}

@Composable
private fun MetricBox(
    count: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(color = color, fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            )
        }
    }
}

private fun String?.isNotBlank() = !this.isNullOrEmpty()
private fun String?.isNullOrEmpty() = this == null || this.trim().isEmpty()
