package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Confetti(
    val emoji: String,
    val startX: Float,
    val startY: Float,
    val endY: Float,
    val duration: Int,
    val rotation: Float
)

@Composable
fun CelebrationOverlay(
    onDismiss: () -> Unit,
    isRevisionMode: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    var isVisible by remember { mutableStateOf(true) }
    
    // Celebratory pulsed haptic pattern
    LaunchedEffect(Unit) {
        // Pulse 1: Initial burst
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(200)
        
        // Pulse 2: Celebration continues
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        delay(300)
        
        // Pulse 3: Final celebratory tap
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    
    // Auto-dismiss after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        isVisible = false
        delay(500) // Wait for fade out
        onDismiss()
    }
    
    // Generate confetti particles
    val confettiList = remember {
        List(30) { index ->
            Confetti(
                emoji = listOf("🎉", "🎊", "✨", "⭐", "🌟", "💫", "🏆", "👏").random(),
                startX = Random.nextFloat(),
                startY = -0.1f,
                endY = 1.2f,
                duration = Random.nextInt(2000, 3500),
                rotation = Random.nextFloat() * 720f - 360f
            )
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = if (isVisible) 0.6f else 0f))
    ) {
        // Confetti particles
        confettiList.forEachIndexed { index, confetti ->
            ConfettiParticle(
                confetti = confetti,
                delay = index * 100,
                isVisible = isVisible
            )
        }
        
        // Celebration text
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = if (isVisible) 1f else 0f
                    scaleX = if (isVisible) 1f else 0.8f
                    scaleY = if (isVisible) 1f else 0.8f
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🎉",
                fontSize = 80.sp
            )
            
            Text(
                text = if (isRevisionMode) "Subject Revised!" else "Subject Completed!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Amazing work! 💪",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun ConfettiParticle(
    confetti: Confetti,
    delay: Int,
    isVisible: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    
    // Falling animation
    val yPosition by infiniteTransition.animateFloat(
        initialValue = confetti.startY,
        targetValue = confetti.endY,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = confetti.duration,
                delayMillis = delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "fall"
    )
    
    // Rotation animation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = confetti.rotation,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = confetti.duration,
                delayMillis = delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotate"
    )
    
    // Swaying left-right
    val xOffset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = confetti.emoji,
            fontSize = 24.sp,
            modifier = Modifier
                .offset(
                    x = (confetti.startX * 1000).dp,
                    y = (yPosition * 1000).dp
                )
                .graphicsLayer {
                    translationX = xOffset
                    rotationZ = rotation
                    alpha = if (isVisible) 1f else 0f
                }
        )
    }
}
