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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Radar
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
import com.example.data.local.entity.OpportunityEntity
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple

@Composable
fun OpportunityCard(
    opportunity: OpportunityEntity,
    onInterestClick: (OpportunityEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = if (opportunity.isImobiliario) NexellaGold else NexellaPurple

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("opportunity_card_${opportunity.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Accent Strip Indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(accentColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Author Avatar (48dp)
            AsyncImage(
                model = opportunity.authorPhoto,
                contentDescription = opportunity.authorName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details Content Column
            Column(modifier = Modifier.weight(1f)) {
                // Top Row: Name & Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = opportunity.authorName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    )

                    if (opportunity.isImobiliario) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NexellaGold.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Corretora",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NexellaGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Subtitle: Category & Neighborhood
                Text(
                    text = "${opportunity.authorBusiness} • ${opportunity.neighborhood}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NexellaPurple,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Opportunity Title & Description
                Text(
                    text = opportunity.title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                )
                Text(
                    text = opportunity.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF666666),
                        lineHeight = 18.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Interest Button
                Button(
                    onClick = { onInterestClick(opportunity) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("interest_opportunity_button_${opportunity.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Radar,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tenho Interesse",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

