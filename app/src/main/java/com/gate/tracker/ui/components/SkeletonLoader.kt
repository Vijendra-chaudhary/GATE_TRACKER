package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.3f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Box(
        modifier = modifier.background(brush)
    )
}

@Composable
fun DashboardCardSkeleton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        ShimmerEffect()
    }
}

@Composable
fun DashboardSkeletonLoader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Countdown card skeleton
        DashboardCardSkeleton(modifier = Modifier.height(140.dp))
        
        // Progress card skeleton
        DashboardCardSkeleton(modifier = Modifier.height(200.dp))
        
        // Streak card skeleton
        DashboardCardSkeleton(modifier = Modifier.height(180.dp))
        
        // Continue studying skeleton
        DashboardCardSkeleton(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun CalendarDaySkeleton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
    ) {
        ShimmerEffect()
    }
}

@Composable
fun CalendarSkeletonLoader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Month header skeleton
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            ShimmerEffect()
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid skeleton
        repeat(5) { // 5 weeks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { // 7 days
                    CalendarDaySkeleton()
                }
            }
        }
    }
}
