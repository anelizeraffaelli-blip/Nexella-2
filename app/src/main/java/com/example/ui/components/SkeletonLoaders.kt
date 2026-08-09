package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NexellaGold
import com.example.ui.theme.NexellaPurple

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val shimmerColors = listOf(
        Color(0xFFEBEBF0),
        Color(0xFFF7F7FA),
        Color(0xFFEBEBF0)
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 300f, translateAnim - 300f),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    height: Dp = 20.dp,
    width: Dp = Dp.Unspecified,
    cornerRadius: Dp = 8.dp
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .then(if (width != Dp.Unspecified) Modifier.width(width) else Modifier.fillMaxWidth())
            .height(height)
            .clip(shape)
            .shimmerEffect()
    )
}

@Composable
fun OpportunityCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("opportunity_card_skeleton"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    SkeletonBox(height = 16.dp, width = 120.dp)
                    Spacer(modifier = Modifier.height(6.dp))
                    SkeletonBox(height = 12.dp, width = 80.dp)
                }
                SkeletonBox(height = 20.dp, width = 70.dp, cornerRadius = 12.dp)
            }
            Spacer(modifier = Modifier.height(14.dp))
            SkeletonBox(height = 18.dp, width = 200.dp)
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBox(height = 14.dp)
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonBox(height = 14.dp, width = 240.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkeletonBox(height = 14.dp, width = 100.dp)
                SkeletonBox(height = 36.dp, width = 110.dp, cornerRadius = 20.dp)
            }
        }
    }
}

@Composable
fun EmpreendedoraCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("empreendedora_card_skeleton"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                SkeletonBox(height = 18.dp, width = 140.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 14.dp, width = 100.dp)
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(height = 12.dp, width = 180.dp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            SkeletonBox(height = 36.dp, width = 80.dp, cornerRadius = 20.dp)
        }
    }
}

@Composable
fun MeetingCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(260.dp)
            .testTag("meeting_card_skeleton"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SkeletonBox(height = 20.dp, width = 160.dp)
            Spacer(modifier = Modifier.height(8.dp))
            SkeletonBox(height = 14.dp, width = 200.dp)
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SkeletonBox(height = 12.dp, width = 80.dp)
                Spacer(modifier = Modifier.width(12.dp))
                SkeletonBox(height = 12.dp, width = 90.dp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            SkeletonBox(height = 36.dp, cornerRadius = 20.dp)
        }
    }
}

@Composable
fun SyncProgressBar(
    isSyncing: Boolean,
    statusText: String,
    modifier: Modifier = Modifier
) {
    if (isSyncing) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(NexellaPurple.copy(alpha = 0.08f))
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .testTag("sync_progress_bar"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = statusText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = NexellaPurple
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = NexellaGold,
                trackColor = NexellaPurple.copy(alpha = 0.2f)
            )
        }
    }
}
