package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val velocityX: Float,
    val velocityY: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val color: Color,
    val size: Float,
    val shape: ConfettiShape
)

enum class ConfettiShape {
    CIRCLE, SQUARE, TRIANGLE
}

@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    onAnimationComplete: () -> Unit = {}
) {
    val confettiColors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFFFFE66D),
        Color(0xFF95E1D3),
        Color(0xFFA8E6CF),
        Color(0xFFFFD3B6),
        Color(0xFFFFAAA5),
        Color(0xFFFF8B94)
    )
    
    var particles by remember {
        mutableStateOf(
            List(60) { i ->
                val angle = (i * 360f / 60) + Random.nextFloat() * 30f
                val speed = 300f + Random.nextFloat() * 400f
                ConfettiParticle(
                    x = 0.5f, // Start from center
                    y = 0.5f,
                    velocityX = cos(Math.toRadians(angle.toDouble())).toFloat() * speed,
                    velocityY = sin(Math.toRadians(angle.toDouble())).toFloat() * speed - 200f, // Upward bias
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = (Random.nextFloat() - 0.5f) * 720f,
                    color = confettiColors.random(),
                    size = 8f + Random.nextFloat() * 8f,
                    shape = ConfettiShape.values().random()
                )
            }
        )
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    var animationTime by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (animationTime < 3f) {
            withFrameMillis {
                animationTime = (System.currentTimeMillis() - startTime) / 1000f
                
                // Update particles
                particles = particles.map { particle ->
                    val dt = 0.016f
                    particle.copy(
                        x = particle.x + particle.velocityX * dt / 1000f,
                        y = particle.y + (particle.velocityY + 500f * animationTime) * dt / 1000f, // Gravity
                        rotation = particle.rotation + particle.rotationSpeed * dt
                    )
                }
            }
        }
        onAnimationComplete()
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            if (particle.y < 1.2f) { // Only draw if still on screen
                val x = particle.x * size.width
                val y = particle.y * size.height
                
                rotate(particle.rotation, pivot = Offset(x, y)) {
                    when (particle.shape) {
                        ConfettiShape.CIRCLE -> {
                            drawCircle(
                                color = particle.color,
                                radius = particle.size,
                                center = Offset(x, y)
                            )
                        }
                        ConfettiShape.SQUARE -> {
                            drawRect(
                                color = particle.color,
                                topLeft = Offset(x - particle.size, y - particle.size),
                                size = androidx.compose.ui.geometry.Size(particle.size * 2, particle.size * 2)
                            )
                        }
                        ConfettiShape.TRIANGLE -> {
                            val path = Path().apply {
                                moveTo(x, y - particle.size)
                                lineTo(x + particle.size, y + particle.size)
                                lineTo(x - particle.size, y + particle.size)
                                close()
                            }
                            drawPath(path, particle.color)
                        }
                    }
                }
            }
        }
    }
}
