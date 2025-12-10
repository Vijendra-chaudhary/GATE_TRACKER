package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

data class MiniConfetti(
    val emoji: String,
    val startX: Float,
    val delay: Int
)

@Composable
fun CelebrationBanner(
    message: String = "Chapter Completed!",
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    // Slide in animation from bottom
    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000) // Show for 2 seconds
        isVisible = false
        delay(300) // Wait for slide out
        onDismiss()
    }
    
    val slideOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 200f,  // Positive = slide down (hide at bottom)
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "slide"
    )
    
    // Generate mini confetti
    val confettiList = remember {
        List(6) { index ->
            MiniConfetti(
                emoji = listOf("🎉", "✨", "⭐").random(),
                startX = Random.nextFloat(),
                delay = index * 100
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom  // Position at bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .graphicsLayer {
                    translationY = slideOffset
                }
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Confetti particles
            confettiList.forEach { confetti ->
                MiniConfettiParticle(
                    confetti = confetti,
                    isVisible = isVisible
                )
            }
        
            // Banner card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2196F3),
                                Color(0xFF42A5F5)
                            )
                        )
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎊",
                        fontSize = 28.sp
                    )
                    
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "✨",
                        fontSize = 28.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MiniConfettiParticle(
    confetti: MiniConfetti,
    isVisible: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mini-confetti")
    
    // Floating upward animation (reversed from falling)
    val yPosition by infiniteTransition.animateFloat(
        initialValue = 1.2f,  // Start from bottom
        targetValue = -0.2f,  // Float up to top
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                delayMillis = confetti.delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "float"
    )
    
    // Rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 800,
                delayMillis = confetti.delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Text(
            text = confetti.emoji,
            fontSize = 18.sp,
            modifier = Modifier
                .offset(
                    x = (confetti.startX * 350).dp,
                    y = (yPosition * 120).dp
                )
                .graphicsLayer {
                    rotationZ = rotation
                    alpha = if (isVisible) 1f else 0f
                }
        )
    }
}
