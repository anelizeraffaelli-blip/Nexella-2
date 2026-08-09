package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark
import com.example.ui.theme.NexellaRose
import com.example.ui.theme.NexellaRoseLight
import com.example.ui.theme.NexellaSubtext

/**
 * Custom dialog for viewing and editing an entrepreneur user profile in Nexella.
 * Persists updates to Room Local Database & Supabase in real-time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentUser: UserEntity,
    onDismiss: () -> Unit,
    onSaveProfile: (updatedUser: UserEntity) -> Unit,
    isSubmitting: Boolean = false,
    isSupabaseConfigured: Boolean = true
) {
    // Form state initialized with current user details
    var name by remember { mutableStateOf(currentUser.name) }
    var photoUrl by remember { mutableStateOf(currentUser.photoUrl) }
    var businessName by remember { mutableStateOf(currentUser.businessName) }
    var category by remember { mutableStateOf(currentUser.category.ifBlank { "Corretoras de Imóveis" }) }
    var neighborhood by remember { mutableStateOf(currentUser.neighborhood.ifBlank { "Centro" }) }
    var description by remember { mutableStateOf(currentUser.description) }
    var services by remember { mutableStateOf(currentUser.services) }
    var procuro by remember { mutableStateOf(currentUser.procuro) }
    var ofereco by remember { mutableStateOf(currentUser.ofereco) }
    var whatsapp by remember { mutableStateOf(currentUser.whatsapp) }
    var instagram by remember { mutableStateOf(currentUser.instagram) }
    var email by remember { mutableStateOf(currentUser.email) }
    var isCorretora by remember { mutableStateOf(currentUser.isCorretora) }
    var creci by remember { mutableStateOf(currentUser.creci ?: "") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var neighborhoodExpanded by remember { mutableStateOf(false) }

    val categories = listOf(
        "Corretoras de Imóveis",
        "Fotografia Imobiliária",
        "Arquitetura & Design",
        "Branding & Social Media",
        "Contabilidade & Finanças",
        "Advocacia & Jurídico",
        "Estética & Bem-Estar",
        "Gastronomia & Eventos",
        "Consultoria de Negócios",
        "Outros Serviços"
    )

    val neighborhoods = listOf(
        "Centro",
        "Neva",
        "Parque Verde",
        "Cancelli",
        "Alto Alegre",
        "Coqueiral",
        "Tropical",
        "Recanto Tropical",
        "Santa Cruz",
        "Fag",
        "Guarujá",
        "Outros Bairros de Cascavel"
    )

    val presetAvatars = listOf(
        "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1567532939604-b6b5b0db2604?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1573497019940-1c28c88b4f3e?w=400&auto=format&fit=crop&q=80"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp)
                .testTag("edit_profile_dialog_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Gradient Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(NexellaPurple, NexellaRose, NexellaGold)
                            )
                        )
                )

                // Dialog Title Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Editar Meu Perfil",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NexellaPurple,
                                    fontSize = 18.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = NexellaRoseLight
                            ) {
                                Text(
                                    text = "Supabase DB",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NexellaRose,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Mantenha suas informações e contatos atualizados na rede",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NexellaSubtext,
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("edit_profile_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color.Gray
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF0F0F4))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Photo Selection Section
                    Text(
                        text = "FOTO DE PERFIL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            letterSpacing = 0.5.sp
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Foto Atual",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(2.dp, NexellaPurple, CircleShape)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Escolha um avatar ou cole uma URL:",
                                style = MaterialTheme.typography.labelSmall.copy(color = NexellaSubtext)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(presetAvatars) { avatar ->
                                    val isSelected = photoUrl == avatar
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = if (isSelected) 3.dp else 1.dp,
                                                color = if (isSelected) NexellaRose else Color.LightGray,
                                                shape = CircleShape
                                            )
                                            .clickable { photoUrl = avatar }
                                    ) {
                                        AsyncImage(
                                            model = avatar,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = photoUrl,
                        onValueChange = { photoUrl = it },
                        label = { Text("URL da Foto de Perfil") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NexellaPurple) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_photo_url_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F4))

                    // 2. Personal & Business Identifiers
                    Text(
                        text = "IDENTIFICAÇÃO & LOCALIZAÇÃO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            letterSpacing = 0.5.sp
                        )
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Seu Nome Completo *") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NexellaPurple) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Nome da sua Empresa / Marca *") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Business, contentDescription = null, tint = NexellaPurple) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_business_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Dropdown Categoria
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ramo de Atuação / Categoria") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("edit_profile_category_dropdown"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        category = item
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Dropdown Bairro
                    ExposedDropdownMenuBox(
                        expanded = neighborhoodExpanded,
                        onExpandedChange = { neighborhoodExpanded = !neighborhoodExpanded }
                    ) {
                        OutlinedTextField(
                            value = neighborhood,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Bairro Principal em Cascavel/PR") },
                            leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = NexellaPurple) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = neighborhoodExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("edit_profile_neighborhood_dropdown"),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = neighborhoodExpanded,
                            onDismissRequest = { neighborhoodExpanded = false }
                        ) {
                            neighborhoods.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        neighborhood = item
                                        neighborhoodExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Checkbox Corretora CRECI
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF7F5FE),
                        border = BorderStroke(1.dp, NexellaPurple.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCorretora = !isCorretora }
                            ) {
                                Checkbox(
                                    checked = isCorretora,
                                    onCheckedChange = { isCorretora = it },
                                    modifier = Modifier.testTag("edit_profile_corretora_checkbox")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Sou Corretora de Imóveis Credenciada",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = NexellaPurpleDark
                                        )
                                    )
                                    Text(
                                        text = "Destaca seu registro CRECI no Radar e Parcerias",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = NexellaSubtext,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            if (isCorretora) {
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = creci,
                                    onValueChange = { creci = it },
                                    label = { Text("Número do CRECI (Ex: 42810-F)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("edit_profile_creci_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF0F0F4))

                    // 3. Procuro / Ofereço / Bio
                    Text(
                        text = "CONEXÕES & SOLUÇÕES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            letterSpacing = 0.5.sp
                        )
                    )

                    OutlinedTextField(
                        value = procuro,
                        onValueChange = { procuro = it },
                        label = { Text("PROCURO NA REDE (Parceiras, indicações, serviços)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NexellaPurple) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_procuro_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = ofereco,
                        onValueChange = { ofereco = it },
                        label = { Text("OFEREÇO PARA PARCEIRAS (Serviços, benefícios, descontos)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Handshake, contentDescription = null, tint = NexellaGold) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_ofereco_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        maxLines = 4
                    )

                    OutlinedTextField(
                        value = services,
                        onValueChange = { services = it },
                        label = { Text("Lista de Serviços & Especialidades") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_services_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        maxLines = 3
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Sobre Mim & Trajetória Profissional") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = NexellaPurple) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_description_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 5
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F4))

                    // 4. Contacts & Social Media
                    Text(
                        text = "CONTATOS & REDES SOCIAIS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            letterSpacing = 0.5.sp
                        )
                    )

                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("WhatsApp Comercial (Ex: 45999887766)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color(0xFF2E7D32)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_whatsapp_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = instagram,
                        onValueChange = { instagram = it },
                        label = { Text("Instagram (Ex: @seu.negocio)") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = NexellaRose) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_instagram_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("E-mail Comercial") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color(0xFF0288D1)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_profile_email_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Submit Action Button
                    Button(
                        onClick = {
                            if (name.isNotBlank() && businessName.isNotBlank()) {
                                val updated = currentUser.copy(
                                    name = name.trim(),
                                    photoUrl = photoUrl.trim(),
                                    businessName = businessName.trim(),
                                    category = category,
                                    neighborhood = neighborhood,
                                    description = description.trim(),
                                    services = services.trim(),
                                    procuro = procuro.trim(),
                                    ofereco = ofereco.trim(),
                                    whatsapp = whatsapp.trim(),
                                    instagram = instagram.trim(),
                                    email = email.trim(),
                                    isCorretora = isCorretora,
                                    creci = if (isCorretora) creci.trim() else null,
                                    specialities = services.ifBlank { category }
                                )
                                onSaveProfile(updated)
                            }
                        },
                        enabled = !isSubmitting && name.isNotBlank() && businessName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("edit_profile_save_button")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salvando no Supabase...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Salvar Alterações no Perfil",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("edit_profile_cancel_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text("Cancelar", color = Color.Gray, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
