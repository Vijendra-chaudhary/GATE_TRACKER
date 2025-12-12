package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

/**
 * Full-screen color sweep animation for mode transitions
 */
@Composable
fun ColorSweepTransition(
    fromColor: Color,
    toColor: Color,
    onComplete: () -> Unit
) {
    var animationStarted by remember { mutableStateOf(false) }
    
    val sweepProgress = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        animationStarted = true
        sweepProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        delay(100) // Small delay for visual effect
        onComplete()
    }
    
    if (animationStarted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            toColor,
                            fromColor,
                            fromColor
                        ),
                        start = Offset(
                            x = -1000f + (sweepProgress.value * 3000f),
                            y = 0f
                        ),
                        end = Offset(
                            x = 1000f + (sweepProgress.value * 3000f),
                            y = 1000f
                        )
                    )
                )
        )
    }
}
