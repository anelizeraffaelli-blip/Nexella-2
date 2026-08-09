package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOpportunityDialog(
    onDismiss: () -> Unit,
    onCreateOpportunity: (
        title: String,
        description: String,
        category: String,
        neighborhood: String,
        type: String,
        isImobiliario: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Fotografia Imobiliária") }
    var selectedNeighborhood by remember { mutableStateOf("Centro") }
    var selectedType by remember { mutableStateOf("Procuro profissional") }
    var isImobiliario by remember { mutableStateOf(false) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var neighborhoodExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Corretoras de Imóveis",
        "Fotografia Imobiliária",
        "Arquitetura & Design",
        "Branding & Social Media",
        "Contabilidade & Finanças",
        "Advocacia & Contratos",
        "Organização & Home Staging",
        "Outros Serviços"
    )

    val neighborhoods = listOf("Centro", "Neva", "Floresta", "Brasília", "Santa Cruz", "Parque Verde", "Cancelli", "Coqueiral", "Tropical")

    val types = listOf(
        "Procuro profissional",
        "Procuro parceira",
        "Ofereço serviço",
        "Procuro fornecedor",
        "Quero indicar alguém",
        "Outra oportunidade"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("create_opportunity_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Criar Oportunidade no Radar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da oportunidade") },
                    placeholder = { Text("Ex: Procuro fotógrafa imobiliária para apartamentos no Centro") },
                    modifier = Modifier.fillMaxWidth().testTag("input_opp_title"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de oportunidade") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        types.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t) },
                                onClick = {
                                    selectedType = t
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c) },
                                onClick = {
                                    selectedCategory = c
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Neighborhood Dropdown
                ExposedDropdownMenuBox(
                    expanded = neighborhoodExpanded,
                    onExpandedChange = { neighborhoodExpanded = !neighborhoodExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedNeighborhood,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Bairro em Cascavel") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = neighborhoodExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = neighborhoodExpanded,
                        onDismissRequest = { neighborhoodExpanded = false }
                    ) {
                        neighborhoods.forEach { n ->
                            DropdownMenuItem(
                                text = { Text(n) },
                                onClick = {
                                    selectedNeighborhood = n
                                    neighborhoodExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição detalhada") },
                    placeholder = { Text("Descreva o que você procura ou oferece...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("input_opp_desc"),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Radar Imobiliário Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isImobiliario,
                        onCheckedChange = { isImobiliario = it }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Destacar no Radar Imobiliário (Corretoras & Parceiras)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onCreateOpportunity(
                                title,
                                description,
                                selectedCategory,
                                selectedNeighborhood,
                                selectedType,
                                isImobiliario
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("submit_opportunity_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Publicar Oportunidade",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
