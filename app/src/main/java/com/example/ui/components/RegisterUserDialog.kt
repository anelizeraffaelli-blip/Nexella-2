package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserDialog(
    onDismiss: () -> Unit,
    onRegister: (
        name: String,
        businessName: String,
        category: String,
        neighborhood: String,
        services: String,
        description: String,
        instagram: String,
        whatsapp: String,
        procuro: String,
        ofereco: String,
        isCorretora: Boolean,
        creci: String?,
        email: String,
        password: String
    ) -> Unit,
    onLogin: ((emailOrName: String) -> Unit)? = null
) {
    var isRegisterTab by remember { mutableStateOf(true) }

    // Register State
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Corretoras de Imóveis") }
    var selectedNeighborhood by remember { mutableStateOf("Centro") }
    var services by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var procuro by remember { mutableStateOf("") }
    var ofereco by remember { mutableStateOf("") }
    var isCorretora by remember { mutableStateOf(false) }
    var creci by remember { mutableStateOf("") }

    // Login State
    var loginInput by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var neighborhoodExpanded by remember { mutableStateOf(false) }

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

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("onboarding_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Faça Parte da Nexella 💜",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NexellaPurple
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Conecte seu negócio à primeira rede de mulheres empreendedoras em Cascavel.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF555555),
                                lineHeight = 16.sp
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Selector Tabs (Cadastro / Entrar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF2F0F7))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isRegisterTab) NexellaPurple else Color.Transparent)
                            .clickable { isRegisterTab = true }
                            .padding(vertical = 8.dp)
                            .testTag("tab_register_onboarding"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cadastrar Perfil",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isRegisterTab) Color.White else Color.Gray
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isRegisterTab) NexellaPurple else Color.Transparent)
                            .clickable { isRegisterTab = false }
                            .padding(vertical = 8.dp)
                            .testTag("tab_login_onboarding"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Entrar / Login",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (!isRegisterTab) Color.White else Color.Gray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (isRegisterTab) {
                    // --- REGISTER / ONBOARDING FORM ---
                    Text(
                        text = "1. INFORMAÇÕES PESSOAIS E DE PERFIL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaGold,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Seu Nome Completo") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Seu E-mail") },
                        placeholder = { Text("exemplo@nexella.com.br") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_email"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha de Acesso") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("reg_password"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "2. INFORMAÇÕES DO SEU NEGÓCIO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaGold,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Nome do Negócio / Marca") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_business"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria do Negócio") },
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
                                        if (c == "Corretoras de Imóveis") isCorretora = true
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isCorretora,
                            onCheckedChange = { isCorretora = it }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sou Corretora / Mercado Imobiliário",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    if (isCorretora) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = creci,
                            onValueChange = { creci = it },
                            label = { Text("Número do CRECI (Opcional)") },
                            placeholder = { Text("Ex: 42810-F") },
                            modifier = Modifier.fillMaxWidth().testTag("reg_creci"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Bio / Descrição do Negócio") },
                        placeholder = { Text("Resumo do seu propósito e diferenciais...") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_desc"),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = procuro,
                        onValueChange = { procuro = it },
                        label = { Text("O que você PROCURA na comunidade?") },
                        placeholder = { Text("Ex: 📸 Fotógrafa imobiliária, Designer de marca") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_procuro"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ofereco,
                        onValueChange = { ofereco = it },
                        label = { Text("O que você OFERECE para a comunidade?") },
                        placeholder = { Text("Ex: 🤝 Parcerias em lançamentos, Descontos") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_ofereco"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("WhatsApp de Contato (ex: 45999887766)") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_whatsapp"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = instagram,
                        onValueChange = { instagram = it },
                        label = { Text("Instagram (ex: @meunegocio)") },
                        modifier = Modifier.fillMaxWidth().testTag("reg_instagram"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank() && businessName.isNotBlank()) {
                                onRegister(
                                    name,
                                    businessName,
                                    selectedCategory,
                                    selectedNeighborhood,
                                    services,
                                    description,
                                    instagram,
                                    whatsapp,
                                    procuro,
                                    ofereco,
                                    isCorretora,
                                    if (isCorretora && creci.isNotBlank()) creci else null,
                                    email,
                                    password
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("submit_register_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Salvar Perfil e Fazer Parte →",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    // --- LOGIN FORM ---
                    Text(
                        text = "ENTRAR NA SUA CONTA NEXELLA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NexellaPurple,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = loginInput,
                        onValueChange = { loginInput = it },
                        label = { Text("E-mail ou Nome Cadastrado") },
                        placeholder = { Text("exemplo@nexella.com.br ou Maria Oliveira") },
                        modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text("Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (loginInput.isNotBlank()) {
                                onLogin?.invoke(loginInput)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("submit_login_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NexellaPurple),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Acessar Plataforma →",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
