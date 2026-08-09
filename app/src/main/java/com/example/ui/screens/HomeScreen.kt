package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.entity.MeetingEntity
import com.example.data.local.entity.OpportunityEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.MeetingCard
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.DashboardViewModel
import com.example.ui.components.DashboardMetricsCard
import com.example.ui.components.OpportunityCard
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple

@Composable
fun HomeScreen(
    currentUser: UserEntity?,
    opportunities: List<OpportunityEntity>,
    meetings: List<MeetingEntity>,
    onNavigateTab: (Int) -> Unit,
    onOpenCreateOpportunity: () -> Unit,
    onOpenElla: () -> Unit,
    onOpenRegister: () -> Unit,
    onCreateConnectionWithUser: (UserEntity) -> Unit,
    onJoinMeeting: (MeetingEntity) -> Unit,
    onFilterNeighborhood: ((String) -> Unit)? = null,
    dashboardViewModel: DashboardViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val userName = currentUser?.name?.split(" ")?.firstOrNull() ?: "Anelize"
    var ellaInlineQuery by remember { mutableStateOf("") }
    val dashboardMetrics by dashboardViewModel.metricsState.collectAsStateWithLifecycle()

    val spotlightWomen = listOf(
        SpotlightProfile(
            id = 101,
            name = "Mariana Silva",
            role = "Corretora de Imóveis",
            neighborhood = "Cascavel • Centro",
            photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80",
            phrase = "Especialista em lançamentos de alto padrão no Centro.",
            offers = "Acesso a investidores imobiliários",
            seeks = "Fotógrafa imobiliária & Designers"
        ),
        SpotlightProfile(
            id = 102,
            name = "Ana Paula Santos",
            role = "Fotografia Imobiliária",
            neighborhood = "Cascavel • Neva",
            photoUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&auto=format&fit=crop&q=80",
            phrase = "Capturando a essência de imóveis para acelerar vendas.",
            offers = "Pacotes de fotos HDR & Vídeo Drone",
            seeks = "Corretoras de imóveis parceiras"
        ),
        SpotlightProfile(
            id = 103,
            name = "Juliana Alencar",
            role = "Arquitetura & Interiores",
            neighborhood = "Cascavel • Cancelli",
            photoUrl = "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?w=400&auto=format&fit=crop&q=80",
            phrase = "Projetando ambientes que encantam e valorizam empreendimentos.",
            offers = "Consultoria de Home Staging",
            seeks = "Marceneiras & Decoradoras"
        )
    )

    val neighborhoodsCount = listOf(
        NeighborhoodStat("Centro", 12),
        NeighborhoodStat("Neva", 8),
        NeighborhoodStat("Floresta", 6),
        NeighborhoodStat("Cancelli", 5),
        NeighborhoodStat("Coqueiral", 4),
        NeighborhoodStat("Alto Alegre", 3)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // 1. Header Title: "Olá, [Nome]"
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_welcome_header"),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Olá, ",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Light,
                            color = Color(0xFF222222),
                            fontSize = 26.sp
                        )
                    )
                    Text(
                        text = "$userName 💜",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF222222),
                            fontSize = 26.sp
                        )
                    )
                }
                Text(
                    text = "Quem você precisa conhecer hoje?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = NexellaPurple,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                )
            }
        }

        // 2. The 4 Quick Action Cards (Encontrar, Conectar, Divulgar, Oportunidade)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MainPathwayCard(
                        title = "ENCONTRAR",
                        subtitle = "Buscar Empreendedoras",
                        desc = "Encontre mulheres e parceiras estratégicas em Cascavel",
                        icon = Icons.Default.Search,
                        accentColor = NexellaPurple,
                        badgeText = "Comunidade",
                        modifier = Modifier.weight(1f).testTag("path_find"),
                        onClick = { onNavigateTab(1) } // Community
                    )
                    MainPathwayCard(
                        title = "CONECTAR",
                        subtitle = "Minhas Parcerias",
                        desc = "Crie e gerencie conexões valiosas na rede",
                        icon = Icons.Default.Handshake,
                        accentColor = NexellaGold,
                        badgeText = "Conexões",
                        modifier = Modifier.weight(1f).testTag("path_connect"),
                        onClick = { onNavigateTab(2) } // Connections
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MainPathwayCard(
                        title = "DIVULGAR",
                        subtitle = "Meu Negócio",
                        desc = "Aumente sua visibilidade e apresente sua empresa",
                        icon = Icons.Default.Campaign,
                        accentColor = Color(0xFF0288D1),
                        badgeText = "Perfil",
                        modifier = Modifier.weight(1f).testTag("path_divulgar"),
                        onClick = onOpenRegister
                    )
                    MainPathwayCard(
                        title = "OPORTUNIDADE",
                        subtitle = "Radar Nexella",
                        desc = "Publique demandas ou encontre anúncios ativos",
                        icon = Icons.Default.Radar,
                        accentColor = Color(0xFF2E7D32),
                        badgeText = "Radar",
                        modifier = Modifier.weight(1f).testTag("path_opp"),
                        onClick = onOpenCreateOpportunity
                    )
                }
            }
        }

        // 3. Ella AI Assistant Inline Widget
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_ella_inline_widget"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NexellaPurple),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Ella",
                                tint = NexellaGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Pergunte para Ella",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "“Eu encontro a conexão para você em Cascavel.”",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Text(
                        text = "🤍 O que você está tentando resolver? Conte para a Ella.",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White.copy(alpha = 0.95f),
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ellaInlineQuery,
                            onValueChange = { ellaInlineQuery = it },
                            placeholder = {
                                Text(
                                    "Ex: Preciso de uma fotógrafa...",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("home_ella_inline_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = NexellaGold,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        Button(
                            onClick = onOpenElla,
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("btn_find_connection_ella"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NexellaGold,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Encontrar",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // 4. "Perto de Você" — Mapa Vivo dos Bairros de Cascavel
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Perto de você",
                            tint = NexellaPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Empreendedoras Perto de Você",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )
                        )
                    }

                    Text(
                        text = "Cascavel/PR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Text(
                    text = "Toque no bairro e descubra quem está ali movimentando a economia feminina:",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(neighborhoodsCount) { item ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .border(1.dp, NexellaPurple.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .clickable {
                                    onFilterNeighborhood?.invoke(item.name)
                                    onNavigateTab(1) // Discover
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "📍 ${item.name}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF333333)
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(NexellaPurple.copy(alpha = 0.1f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${item.count}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NexellaPurple,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Conexões em Destaque (Spotlight Showcase Cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "🔥 CONEXÕES EM DESTAQUE",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF999999),
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = "Talvez a próxima conexão do seu negócio esteja aqui:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(spotlightWomen) { woman ->
                        Card(
                            modifier = Modifier
                                .width(260.dp)
                                .testTag("spotlight_card_${woman.id}"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AsyncImage(
                                        model = woman.photoUrl,
                                        contentDescription = woman.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = woman.name,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF333333)
                                            )
                                        )
                                        Text(
                                            text = woman.role,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = NexellaPurple,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                        Text(
                                            text = woman.neighborhood,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.Gray,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "\"${woman.phrase}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF555555),
                                        fontSize = 11.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF9F9FB))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "🤝 Oferece: ${woman.offers}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF333333),
                                            fontSize = 10.sp
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "🔍 Procura: ${woman.seeks}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NexellaPurple,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        maxLines = 1
                                    )
                                }

                                Button(
                                    onClick = { onNavigateTab(1) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NexellaPurple,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = "Criar Conexão",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. Section: Nexella Conecta | Imobiliário
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_imobiliario_showcase_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NexellaGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HomeWork,
                                    contentDescription = null,
                                    tint = NexellaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Nexella Conecta | Imobiliário",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                )
                            )
                        }
                    }

                    Text(
                        text = "Corretoras + profissionais que movimentam o mercado imobiliário em Cascavel.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                    )

                    Text(
                        text = "“Você não precisa fazer tudo sozinha. Encontre quem pode fazer com você.”",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = NexellaPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Ecosystem roles pills
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val roles = listOf("Corretoras", "Arquitetas", "Fotógrafas", "Designers", "Marceneiras", "Paisagistas")
                        items(roles) { r ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF2EFFD))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = r,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NexellaPurple,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { onNavigateTab(1) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NexellaGold,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Explorar Ecossistema Imobiliário",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 7. Radar Nexella Header & Live Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "⚡ O RADAR NEXELLA",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            color = Color(0xFF999999),
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "Oportunidades acontecendo agora em Cascavel",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF666666))
                    )
                }

                Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = NexellaPurple,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { onNavigateTab(2) }
                        .padding(4.dp)
                )
            }
        }

        // Radar Cards
        items(opportunities.take(2)) { opp ->
            OpportunityCard(
                opportunity = opp,
                onInterestClick = { onCreateConnectionWithUser(UserEntity(
                    id = 999,
                    name = opp.authorName,
                    businessName = opp.authorBusiness,
                    category = opp.category,
                    neighborhood = opp.neighborhood,
                    services = "Oportunidade no Radar",
                    description = opp.description,
                    instagram = "@nexella",
                    whatsapp = "45999887766",
                    photoUrl = opp.authorPhoto,
                    procuro = opp.title,
                    ofereco = opp.type
                )) }
            )
        }

        // 8. Encontros Presenciais Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRÓXIMOS ENCONTROS NOS BAIRROS",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = Color(0xFF999999),
                        fontSize = 12.sp
                    )
                )

                Text(
                    text = "Ver agenda",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = NexellaPurple,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { onNavigateTab(3) }
                        .padding(4.dp)
                )
            }
        }

        items(meetings.take(1)) { meeting ->
            MeetingCard(
                meeting = meeting,
                onJoinClick = { onJoinMeeting(meeting) }
            )
        }

        // 9. Contador Vivo (Real Platform Statistics)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_live_counter_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "A NEXELLA HOJE EM CASCAVEL",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = NexellaPurple
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCounterItem(number = "127", label = "Empreendedoras")
                        StatCounterItem(number = "84", label = "Conexões")
                        StatCounterItem(number = "31", label = "Oportunidades")
                        StatCounterItem(number = "6", label = "Encontros")
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun MainPathwayCard(
    title: String,
    subtitle: String,
    desc: String,
    icon: ImageVector,
    containerColor: Color = Color.White,
    accentColor: Color,
    badgeText: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFECECEC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (badgeText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(accentColor.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = accentColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = accentColor,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF222222),
                        fontSize = 14.sp
                    )
                )
            }

            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF666666),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun StatCounterItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = NexellaPurple,
                fontSize = 20.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray,
                fontSize = 10.sp
            )
        )
    }
}

private data class SpotlightProfile(
    val id: Int,
    val name: String,
    val role: String,
    val neighborhood: String,
    val photoUrl: String,
    val phrase: String,
    val offers: String,
    val seeks: String
)

private data class NeighborhoodStat(
    val name: String,
    val count: Int
)
