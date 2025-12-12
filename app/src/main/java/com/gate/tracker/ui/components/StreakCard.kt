package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.data.model.StreakBadge
import kotlinx.coroutines.delay

@Composable
fun StreakCard(
    currentStreak: Int,
    longestStreak: Int,
    currentBadge: StreakBadge,
    modifier: Modifier = Modifier
) {
    // Celebration animation state
    var isCelebrating by remember { mutableStateOf(false) }
    val isAtRecord = currentStreak == longestStreak && currentStreak > 0
    
    // Trigger celebration when at record or new badge
    LaunchedEffect(currentStreak, currentBadge) {
        if (currentStreak > 0 && (isAtRecord || currentBadge != StreakBadge.NONE)) {
            isCelebrating = true
            delay(2000) // Celebrate for 2 seconds
            isCelebrating = false
        }
    }
    
    // Animation values
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Compact Banner Design
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            currentBadge.gradientStart,
                            currentBadge.gradientEnd
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Streak count with fire emoji
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fire emoji with animation
                    Text(
                        text = "🔥",
                        fontSize = 32.sp,
                        modifier = if (isCelebrating) {
                            Modifier.scale(pulseScale)
                        } else Modifier
                    )
                    
                    Column {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = currentStreak.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                lineHeight = 28.sp,
                                modifier = if (isCelebrating && isAtRecord) {
                                    Modifier.scale(pulseScale)
                                } else Modifier
                            )
                            Text(
                                text = if (currentStreak == 1) "Day" else "Days",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.95f),
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        
                        // Show "New Record!" if at record
                        if (isAtRecord && currentStreak > 1) {
                            Text(
                                text = "👑 New Record!",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
                
                // Right: Badge indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Badge emoji in circle
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentBadge.emoji,
                            fontSize = 20.sp,
                            modifier = if (isCelebrating) {
                                Modifier.scale(pulseScale)
                            } else Modifier
                        )
                    }
                    
                    // Badge title (hidden on small screens, shown on larger)
                    if (currentBadge != StreakBadge.NONE) {
                        Text(
                            text = currentBadge.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.5.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
