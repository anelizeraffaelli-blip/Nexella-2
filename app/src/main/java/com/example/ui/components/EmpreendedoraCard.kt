package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaGoldLight
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleLight

@Composable
fun EmpreendedoraCard(
    user: UserEntity,
    onCreateConnection: (UserEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("empreendedora_card_${user.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Section: Photo, Name, Business, Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box {
                    AsyncImage(
                        model = user.photoUrl,
                        contentDescription = user.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2B2B2B)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = user.businessName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = NexellaPurple
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Local",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${user.neighborhood}, Cascavel",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )

                        if (!user.creci.isNull_or_blank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE3F2FD))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "CRECI ${user.creci}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF1976D2),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category tag
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(NexellaPurpleLight)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = user.category,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = NexellaPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Short Description
            Text(
                text = user.description,
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF555555)),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Procuro & Ofereço sections
            if (user.procuro.isNotBlank() || user.ofereco.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF9F9FC))
                        .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    if (user.procuro.isNotBlank()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "Procuro: ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NexellaPurple
                                )
                            )
                            Text(
                                text = user.procuro,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF333333)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (user.ofereco.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "Ofereço: ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NexellaGold
                                )
                            )
                            Text(
                                text = user.ofereco,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF333333)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button: Criar Conexão
            Button(
                onClick = { onCreateConnection(user) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_connection_button_${user.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NexellaPurple
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Criar Conexão",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

private fun String?.isNotBlank() = !this.isNull_or_blank()
private fun String?.isNull_or_blank() = this == null || this.trim().isEmpty()
