package com.gate.tracker.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodoItemExplosion(onFinished: () -> Unit) {
    val particles = remember { List(10) { TodoItemParticle() } }
    val transition = androidx.compose.animation.core.rememberInfiniteTransition(label = "confetti")
    
    var progress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        androidx.compose.animation.core.animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(600, easing = androidx.compose.animation.core.LinearOutSlowInEasing)
        ) { value, _ ->
            progress = value
        }
        onFinished()
    }
    
    androidx.compose.foundation.Canvas(modifier = Modifier.size(50.dp)) {
        val center = center
        particles.forEach { particle ->
            val distance = progress * 60.dp.toPx() * particle.velocity
            val x = center.x + (kotlin.math.cos(particle.angle) * distance).toFloat()
            val y = center.y + (kotlin.math.sin(particle.angle) * distance).toFloat()
            val alpha = 1f - progress
            
            drawCircle(
                color = particle.color.copy(alpha = alpha),
                radius = 4.dp.toPx() * (1f - progress),
                center = androidx.compose.ui.geometry.Offset(x, y)
            )
        }
    }
}

private data class TodoItemParticle(
    val angle: Double = Math.random() * 2 * Math.PI,
    val velocity: Float = (0.5f + Math.random() * 0.5f).toFloat(),
    val color: Color = listOf(
        Color(0xFF4A90E2), // Blue
        Color(0xFFF39C12), // Orange
        Color(0xFF27AE60), // Green
        Color(0xFFE74C3C)  // Red
    ).random()
)
