package com.gate.tracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun ProgressCard(
    completedChapters: Int,
    totalChapters: Int,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isRevisionMode: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    val progress = if (totalChapters > 0) {
        completedChapters.toFloat() / totalChapters.toFloat()
    } else {
        0f
    }
    val percentage = (progress * 100).toInt()
    
    // Confetti state for milestones
    var showConfetti by remember { mutableStateOf(false) }
    val previousPercentage = remember { mutableStateOf(0) }
    
    // Trigger confetti at milestones: 25%, 50%, 75%, 100%
    LaunchedEffect(percentage) {
        val milestones = listOf(25, 50, 75, 100)
        if (percentage > previousPercentage.value && percentage in milestones) {
            showConfetti = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        previousPercentage.value = percentage
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "progress"
    )
    
    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (isRevisionMode) {
                                if (isSystemInDarkTheme()) {
                                    listOf(Color(0xFF9333EA), Color(0xFF7C3AED))
                                } else {
                                    listOf(Color(0xFFF3E8FF), Color(0xFFEEE4FF))
                                }
                            } else listOf(
                                Color(0xFF667eea),
                                Color(0xFF764ba2)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isRevisionMode) "Revision Progress" else "Overall Progress",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764) else Color.White.copy(alpha = 0.9f),
                        letterSpacing = 0.5.sp
                    )
                    
                    // Large percentage display
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764) else Color.White,
                        fontSize = 56.sp
                    )
                    
                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764).copy(alpha = 0.1f) else Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .fillMaxHeight()
                                .background(
                                    color = if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764) else Color.White,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    }
                    
                    // Chapter count
                    Text(
                        text = if (isRevisionMode) 
                            "$completedChapters of $totalChapters chapters revised" 
                        else 
                            "$completedChapters of $totalChapters chapters completed",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                    
                    // Tap hint
                    Text(
                        text = "Tap to view all subjects →",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isRevisionMode && !isSystemInDarkTheme()) Color(0xFF3B0764).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        
        // Full-screen confetti overlay
        if (showConfetti) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f)
            ) {
                ConfettiAnimation(
                    onAnimationComplete = { showConfetti = false }
                )
            }
        }
    }
}
