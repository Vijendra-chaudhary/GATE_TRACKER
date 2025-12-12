package com.gate.tracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.SubjectEntity
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun SubjectCard(
    subject: SubjectEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCelebrate: () -> Unit = {}, // Callback when subject is completed
    isRevisionMode: Boolean = false
) {
    // Use appropriate chapter count based on mode
    val chaptersCount = if (isRevisionMode) subject.revisedChapters else subject.completedChapters
    
    val progress = if (subject.totalChapters > 0) {
        chaptersCount.toFloat() / subject.totalChapters.toFloat()
    } else {
        0f
    }
    
    val isCompleted = progress == 1f
    
    // Track completion state to trigger celebration once
    var wasCompleted by remember { mutableStateOf(isCompleted) }
    
    // Trigger banner celebration when subject becomes completed
    LaunchedEffect(isCompleted) {
        if (isCompleted && !wasCompleted) {
            onCelebrate()
            wasCompleted = true
        }
    }
    
    val cardColor by animateColorAsState(
        targetValue = if (isCompleted) {
            Color(0xFFE8F5E9)
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "cardColor"
    )
    
    // Color scheme based on completion status and revision mode
    // Color scheme based on completion status and revision mode
    val isDarkTheme = isSystemInDarkTheme()
    val progressColor = when {
        isRevisionMode && progress >= 0.75f -> if (isDarkTheme) Color(0xFF9333EA) else Color(0xFF7C3AED) // Purple for revision mode (high progress)
        isRevisionMode && progress >= 0.5f -> if (isDarkTheme) Color(0xFFA855F7) else Color(0xFF9333EA) // Lighter purple for revision mode (medium progress)
        isRevisionMode -> if (isDarkTheme) Color(0xFFC084FC) else Color(0xFFA855F7) // Lightest purple for revision mode (low progress)
        progress >= 0.75f -> Color(0xFF4CAF50) // Green for high progress (normal mode)
        progress >= 0.5f -> Color(0xFF2196F3) // Blue for medium progress (normal mode)
        progress >= 0.25f -> Color(0xFFFFA726) // Orange for low progress
        else -> Color(0xFF9E9E9E) // Gray for very low progress
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 1.dp else 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Subject name and status icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) {
                            Color(0xFF2E7D32)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
                
                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    progressColor,
                                    progressColor.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }
            
            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$chaptersCount / ${subject.totalChapters} chapters",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCompleted) {
                        Color(0xFF2E7D32)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.Medium
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(progressColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = progressColor
                    )
                }
            }
        }
    }
}
