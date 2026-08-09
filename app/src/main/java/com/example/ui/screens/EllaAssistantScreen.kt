package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.EllaMessageEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark

@Composable
fun EllaAssistantScreen(
    messages: List<EllaMessageEntity>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onClose: () -> Unit = {},
    onClearChat: (() -> Unit)? = null,
    onShowToast: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Mentoria de Negócios") }
    var showTipBanner by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    val categoryTopics = listOf(
        "Mentoria de Negócios",
        "Vendas & Parcerias",
        "Marketing em Cascavel",
        "Precificação & Margem",
        "Radar de Oportunidades"
    )

    val topicSuggestions = when (selectedCategory) {
        "Vendas & Parcerias" -> listOf(
            "Como montar uma proposta de parceria estratégica?",
            "Qual o melhor jeito de pedir indicações de clientes?",
            "Como abordar empreendedoras do meu bairro em Cascavel?"
        )
        "Marketing em Cascavel" -> listOf(
            "Como divulgar meu negócio local sem gastar muito?",
            "Recomende uma profissional de marketing na comunidade",
            "Como criar um posicionamento forte no Instagram?"
        )
        "Precificação & Margem" -> listOf(
            "Como calcular minha hora de trabalho sem prejuízo?",
            "Como reajustar meus preços sem perder clientes?",
            "Quais custos invisíveis devo considerar no meu serviço?"
        )
        "Radar de Oportunidades" -> listOf(
            "Como publicar uma demanda no Radar Nexella?",
            "Quais oportunidades ativas existem em Cascavel hoje?",
            "Procuro uma arquiteta ou designer no Centro"
        )
        else -> listOf(
            "Como estruturar meu plano de ação para os próximos 3 meses?",
            "Como encontrar uma mentora de negócios na minha área?",
            "Preciso de uma orientação para expandir meu serviço em Cascavel",
            "Como criar uma apresentação atraente do meu negócio?"
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FA))
    ) {
        // ELLA Interactive Header Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(NexellaPurple, NexellaPurpleDark)
                                    )
                                )
                                .border(2.dp, NexellaGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "ELLA Avatar",
                                tint = NexellaGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Ella Mentora AI",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NexellaPurple
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = NexellaGold.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Cascavel/PR",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF795548),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Mentoria de negócios, parcerias & conexões reais 💜",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Row {
                        if (onClearChat != null) {
                            IconButton(
                                onClick = { onClearChat() },
                                modifier = Modifier.testTag("clear_ella_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Limpar Conversa",
                                    tint = Color.Gray
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.testTag("close_ella_screen")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar Ella",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                // Mentorship Daily Tip Banner
                AnimatedVisibility(visible = showTipBanner) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .testTag("mentorship_tip_banner"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = NexellaPurple.copy(alpha = 0.06f)),
                        border = BorderStroke(1.dp, NexellaPurple.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Dica de Mentoria",
                                tint = NexellaGold,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Dica Nexella: 'Parcerias locais em Cascavel geram 3x mais negócios do que vendas diretas isoladas. Peça indicações hoje!'",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NexellaPurpleDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { showTipBanner = false },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Ocultar Dica",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips for Mentorship Topics
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryTopics) { topic ->
                val isSelected = topic == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = topic },
                    label = {
                        Text(
                            text = topic,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NexellaPurple,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = NexellaPurple
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = NexellaPurple.copy(alpha = 0.3f),
                        selectedBorderColor = NexellaPurple,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Interactive Topic Suggestions
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(topicSuggestions) { prompt ->
                SuggestionChip(
                    onClick = { onSendMessage(prompt) },
                    label = {
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = NexellaGold,
                            modifier = Modifier.size(14.dp)
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color.White,
                        labelColor = NexellaPurpleDark
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E8))
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Message History List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isUser = msg.sender == "USER"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (isUser) 18.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 18.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) NexellaPurple else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = if (!isUser) BorderStroke(1.dp, NexellaPurple.copy(alpha = 0.1f)) else null,
                        modifier = Modifier
                            .fillMaxWidth(0.88f)
                            .testTag("chat_bubble_${msg.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            if (!isUser) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 6.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = NexellaGold,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "ELLA MENTORA AI",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = NexellaPurple,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp
                                            )
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(msg.text))
                                            onShowToast?.invoke("Conselho de mentoria copiado! 📋")
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copiar Texto",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isUser) Color.White else Color(0xFF222222),
                                    lineHeight = 20.sp
                                )
                            )

                            if (!isUser && msg.text.length > 80) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AssistChip(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(msg.text))
                                            onShowToast?.invoke("Orientação salva no bloco de notas! 📋")
                                        },
                                        label = { Text("Salvar Mentoria", style = MaterialTheme.typography.labelSmall) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.TrendingUp,
                                                contentDescription = null,
                                                tint = NexellaPurple,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = NexellaPurple.copy(alpha = 0.08f),
                                            labelColor = NexellaPurple
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .testTag("ella_loading_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = NexellaPurple,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Ella formulando sua mentoria estratégica em Cascavel...",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NexellaPurpleDark,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = NexellaGold,
                                trackColor = NexellaPurple.copy(alpha = 0.15f)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Interactive Input Field
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Pergunte sobre mentoria, negócios ou parceiras em Cascavel...",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color.Gray)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_ella_prompt"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = !isLoading && inputText.isNotBlank(),
                    modifier = Modifier.testTag("send_ella_prompt_button")
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (inputText.isNotBlank() && !isLoading) NexellaPurple else Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
