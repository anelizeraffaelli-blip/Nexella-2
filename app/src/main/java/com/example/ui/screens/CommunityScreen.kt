package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.entity.UserEntity
import com.example.ui.components.EmpreendedoraCard
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    users: List<UserEntity>,
    searchQuery: String,
    selectedNeighborhood: String,
    selectedCategory: String,
    isImobiliarioOnly: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onNeighborhoodSelect: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onToggleImobiliario: (Boolean) -> Unit,
    onCreateConnection: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var categoryExpanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Todas As Categorias",
        "Corretoras de Imóveis",
        "Fotografia Imobiliária",
        "Arquitetura & Design",
        "Branding & Social Media",
        "Contabilidade & Finanças",
        "Advocacia & Contratos"
    )

    val neighborhoods = listOf(
        "Todos Os Bairros",
        "Centro",
        "Neva",
        "Floresta",
        "Brasília",
        "Santa Cruz",
        "Parque Verde",
        "Cancelli"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F9))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Title
        item {
            Column {
                Text(
                    text = "✨ Descobrir",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = NexellaPurple
                    )
                )
                Text(
                    text = "Encontre a empreendedora certa para o seu negócio em Cascavel",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar por nome, serviço, palavra-chave...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("community_search_input"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexellaPurple,
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        // Quick Tag: Nexella Conecta | Imobiliário
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isImobiliarioOnly) NexellaPurple else Color.White)
                    .clickable { onToggleImobiliario(!isImobiliarioOnly) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .testTag("filter_imobiliario_toggle")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HomeWork,
                            contentDescription = "Imobiliário",
                            tint = if (isImobiliarioOnly) NexellaGold else NexellaPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nexella Conecta | Imobiliário",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isImobiliarioOnly) Color.White else NexellaPurple
                            )
                        )
                    }

                    Text(
                        text = if (isImobiliarioOnly) "Ativo ✓" else "Filtrar Corretoras & Ecossistema",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isImobiliarioOnly) NexellaGold else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Neighborhood Filter Horizontal Scroll
        item {
            Column {
                Text(
                    text = "Filtrar por Bairro em Cascavel:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF444444)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(neighborhoods) { n ->
                        val isSelected = selectedNeighborhood == n
                        FilterChip(
                            selected = isSelected,
                            onClick = { onNeighborhoodSelect(n) },
                            label = { Text(n) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NexellaPurple,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Color(0xFF444444)
                            )
                        )
                    }
                }
            }
        }

        // Category Dropdown
        item {
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c) },
                            onClick = {
                                onCategorySelect(c)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Empreendedora Cards List
        if (users.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma empreendedora encontrada para esses filtros.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
        } else {
            items(users) { user ->
                EmpreendedoraCard(
                    user = user,
                    onCreateConnection = onCreateConnection
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
