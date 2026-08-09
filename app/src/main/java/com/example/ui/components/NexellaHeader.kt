package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R

@Composable
fun NexellaHeader(
    currentUser: UserEntity?,
    onOpenElla: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("nexella_header_surface"),
        color = Color(0xFFF7F7F7)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo with Icon Box & Location
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, NexellaPurple.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nexella_official_logo_1786244618811),
                            contentDescription = "Nexella Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEXELLA",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                            color = NexellaPurple
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = NexellaGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Cascavel, PR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF888888),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Right Actions: Badge + Profile Avatar
            Row(verticalAlignment = Alignment.CenterVertically) {
                // "Fundadora" Pill Badge
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, NexellaGold.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(NexellaGold)
                        )
                        Text(
                            text = "MEMBRO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NexellaGold,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // ELLA Button Launcher
                Box(
                    modifier = Modifier
                        .testTag("header_ella_button")
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(NexellaPurple, NexellaPurpleDark)
                            )
                        )
                        .clickable { onOpenElla() }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "ELLA IA",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Profile Avatar
                Box(
                    modifier = Modifier
                        .testTag("header_profile_avatar")
                        .clickable { onOpenProfile() }
                ) {
                    AsyncImage(
                        model = currentUser?.photoUrl
                            ?: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&auto=format&fit=crop&q=80",
                        contentDescription = "Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}

