package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DashboardMetricsUiState
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple
import com.example.ui.theme.NexellaPurpleDark

@Composable
fun DashboardMetricsCard(
    metricsState: DashboardMetricsUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_metrics_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFEFEFEF))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Title & Real-time Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Métricas",
                            tint = NexellaPurple,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Métricas da Comunidade",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF222222),
                                fontSize = 17.sp
                            )
                        )
                    }
                    Text(
                        text = "Dados em tempo real em Cascavel - PR",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF777777),
                            fontSize = 12.sp
                        )
                    )
                }

                // Live Room DB Indicator
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NexellaPurple.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, NexellaPurple.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E7D32))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Room DB",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NexellaPurpleDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            // 2x2 Grid of Key Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricItemCard(
                    title = "Total Conexões",
                    value = "${metricsState.totalConnections}",
                    subtitle = "Interações iniciadas",
                    icon = Icons.Default.Handshake,
                    accentColor = NexellaPurple,
                    testTag = "metric_total_connections",
                    modifier = Modifier.weight(1f)
                )

                MetricItemCard(
                    title = "Oportunidades Resolvidas",
                    value = "${metricsState.opportunitiesResolved}",
                    subtitle = "Parcerias de sucesso",
                    icon = Icons.Default.CheckCircle,
                    accentColor = NexellaGold,
                    testTag = "metric_opportunities_resolved",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricItemCard(
                    title = "Empreendedoras",
                    value = "${metricsState.totalUsers}",
                    subtitle = "Membros ativas",
                    icon = Icons.Default.Groups,
                    accentColor = Color(0xFF0288D1),
                    testTag = "metric_total_users",
                    modifier = Modifier.weight(1f)
                )

                MetricItemCard(
                    title = "Radar de Oportunidades",
                    value = "${metricsState.totalOpportunities}",
                    subtitle = "Demandas cadastradas",
                    icon = Icons.Default.Radar,
                    accentColor = Color(0xFF2E7D32),
                    testTag = "metric_total_opportunities",
                    modifier = Modifier.weight(1f)
                )
            }

            // Highlight Impact Banner
            AnimatedVisibility(
                visible = metricsState.opportunitiesResolved > 0,
                enter = fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    NexellaPurple.copy(alpha = 0.08f),
                                    NexellaGold.copy(alpha = 0.12f)
                                )
                            )
                        )
                        .border(1.dp, NexellaPurple.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NexellaPurple),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "${metricsState.opportunitiesResolved} parcerias geraram negócios reais!",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF222222),
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Membros se conectaram e fecharam contratos na comunidade Nexella.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF555555),
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

@Composable
private fun MetricItemCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        color = accentColor.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.18f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF555555),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E1E1E),
                    fontSize = 22.sp
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF777777),
                    fontSize = 10.sp
                )
            )
        }
    }
}
