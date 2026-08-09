package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.ProfileEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaGoldLight
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark

@Composable
fun ProfileScreen(
    user: UserEntity? = null,
    profile: ProfileEntity? = null,
    onConnectClick: ((UserEntity) -> Unit)? = null,
    onOpenWhatsapp: ((String) -> Unit)? = null,
    onOpenInstagram: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // Determine details from UserEntity or fallback to ProfileEntity
    val name = user?.name ?: profile?.name ?: "Empreendedora Nexella"
    val businessName = user?.businessName ?: profile?.businessName ?: "Negócio Local em Cascavel"
    val city = user?.city ?: profile?.city ?: "Cascavel"
    val neighborhood = user?.neighborhood ?: profile?.neighborhood ?: "Centro"
    val location = "$neighborhood, $city - PR"
    val photoUrl = user?.photoUrl ?: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80"
    val category = user?.category ?: profile?.category ?: "Geral"
    val description = user?.description ?: profile?.bio ?: "Membro da rede de conexões empreendedoras Nexella em Cascavel."
    
    val procuro = user?.procuro?.ifBlank { "Busco parceiras de negócios, indicações de clientes e troca de experiências estratégicas." }
        ?: "Busco conexões locais com empreendedoras e parceiras em Cascavel."
    
    val ofereco = user?.ofereco?.ifBlank { user.services }?.ifBlank { "Consultoria, serviços especializados e parcerias em Cascavel." }
        ?: "Serviços profissionais, soluções sob medida e rede de contatos."

    val instagram = user?.instagram ?: profile?.instagram ?: ""
    val whatsapp = user?.whatsapp ?: profile?.whatsapp ?: ""
    val website = user?.website ?: ""
    val email = user?.email ?: profile?.email ?: ""
    val isFoundingMember = user?.isFoundingMember ?: true

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FC))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // 1. Header Card with Photo, Name, Business Name, Location
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card_header"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Cover Banner Gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(NexellaPurple, NexellaPurpleDark)
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar overlapped with cover
                        Box(
                            modifier = Modifier.offset(y = (-40).dp)
                        ) {
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, Color.White, CircleShape)
                            )
                            if (isFoundingMember) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(NexellaGold)
                                        .border(2.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = "Membro Fundadora",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(y = (-24).dp)
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF1E1E1E)
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = "Empresa",
                                    tint = NexellaPurple,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = businessName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = NexellaPurple,
                                        fontSize = 15.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Location Badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF0ECFD))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Localização",
                                    tint = NexellaPurple,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = location,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = NexellaPurpleDark,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Tags / Categories
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFFFF8E1),
                                    border = BorderStroke(1.dp, NexellaGoldLight)
                                ) {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF8D6E63),
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }

                                if (isFoundingMember) {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = NexellaGold.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, NexellaGold)
                                    ) {
                                        Text(
                                            text = "Membro Fundadora 👑",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF795548),
                                                fontWeight = FontWeight.Bold
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Procuro / Ofereço Cards Section
        item {
            Text(
                text = "Interesses e Parcerias",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF222222)
                )
            )
        }

        // Card PROCURO
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card_procuro"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, NexellaPurple.copy(alpha = 0.2f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NexellaPurple.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Procuro",
                                tint = NexellaPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "PROCURO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NexellaPurple,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "O que estou buscando na rede",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF666666),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Divider(color = Color(0xFFF0F0F0))

                    Text(
                        text = procuro,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF333333),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Card OFEREÇO
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card_ofereco"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, NexellaGold.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NexellaGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = "Ofereço",
                                tint = Color(0xFFB78103),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "OFEREÇO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB78103),
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "Minhas soluções e especialidades",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF666666),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Divider(color = Color(0xFFF0F0F0))

                    Text(
                        text = ofereco,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF333333),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // 3. About / Bio Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card_about"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Sobre",
                            tint = NexellaPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Sobre mim & Minha Trajetória",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222)
                            )
                        )
                    }

                    Divider(color = Color(0xFFF0F0F0))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF444444),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // 4. Contact & Action Buttons
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card_contact"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Contatos & Redes",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF222222)
                        )
                    )

                    if (whatsapp.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "WhatsApp",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "WhatsApp: $whatsapp",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333))
                            )
                        }
                    }

                    if (instagram.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Instagram",
                                tint = NexellaPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Instagram: @${instagram.replace("@", "")}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333))
                            )
                        }
                    }

                    if (email.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "E-mail",
                                tint = Color(0xFF0288D1),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333))
                            )
                        }
                    }

                    if (user != null && onConnectClick != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = { onConnectClick(user) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("profile_connect_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Handshake,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Solicitar Conexão com ${user.name.split(" ").first()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
