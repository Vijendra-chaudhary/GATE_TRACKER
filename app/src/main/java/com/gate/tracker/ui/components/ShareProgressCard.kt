package com.gate.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Beautiful shareable progress card for social media
 */
@Composable
fun ShareProgressCard(
    branchName: String,
    completedChapters: Int,
    totalChapters: Int,
    currentStreak: Int,
    daysUntilExam: Int,
    isRevisionMode: Boolean = false
) {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val progressPercent = if (totalChapters > 0) {
        (completedChapters * 100) / totalChapters
    } else 0
    
    // Gradient background - purple for revision mode, blue for normal mode
    val gradient = Brush.linearGradient(
        colors = if (isRevisionMode) {
            if (isDarkTheme) {
                listOf(Color(0xFF9333EA), Color(0xFF7C3AED))
            } else {
                listOf(Color(0xFFF3E8FF), Color(0xFFEEE4FF))
            }
        } else listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2)
        )
    )
    
    // Text color based on background brightness
    val contentColor = if (isRevisionMode && !isDarkTheme) Color(0xFF3B0764) else Color.White
    
    Box(
        modifier = Modifier
            .width(600.dp)
            .height(600.dp)
            .background(gradient, RoundedCornerShape(24.dp))
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "📚 GATE Tracker",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = branchName,
                fontSize = 16.sp,
                color = contentColor.copy(alpha = 0.9f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Large circular progress
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(180.dp)
            ) {
                CircularProgressIndicator(
                    progress = progressPercent / 100f,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 16.dp,
                    color = contentColor,
                    trackColor = contentColor.copy(alpha = 0.3f)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$progressPercent%",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )
                    Text(
                        text = "Complete",
                        fontSize = 18.sp,
                        color = contentColor.copy(alpha = 0.9f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = "📖",
                    value = "$completedChapters/$totalChapters",
                    label = "Chapters",
                    contentColor = contentColor
                )
                StatItem(
                    icon = "🔥",
                    value = "$currentStreak",
                    label = "Day Streak",
                    contentColor = contentColor
                )
                StatItem(
                    icon = "📅",
                    value = "$daysUntilExam",
                    label = "Days Left",
                    contentColor = contentColor
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Motivational quote
            Text(
                text = "\"Keep pushing! You're making great progress!\"",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = contentColor.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Footer
            Divider(color = contentColor.copy(alpha = 0.3f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Shared from GATE Tracker",
                fontSize = 12.sp,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun StatItem(
    icon: String,
    value: String,
    label: String,
    contentColor: Color = Color.White
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = icon,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = contentColor.copy(alpha = 0.8f)
        )
    }
}
