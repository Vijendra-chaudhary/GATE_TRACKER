package com.gate.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SubjectsSummarySection(
    completedCount: Int,
    inProgressCount: Int,
    notStartedCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Subjects Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                count = completedCount,
                label = "Completed",
                icon = Icons.Default.CheckCircle,
                gradientColors = listOf(
                    Color(0xFF4CAF50),
                    Color(0xFF81C784)
                ),
                iconTint = Color(0xFF2E7D32)
            )
            
            SummaryCard(
                modifier = Modifier.weight(1f),
                count = inProgressCount,
                label = "In Progress",
                icon = Icons.Default.PlayCircle,
                gradientColors = listOf(
                    Color(0xFF2196F3),
                    Color(0xFF64B5F6)
                ),
                iconTint = Color(0xFF1565C0)
            )
            
            SummaryCard(
                modifier = Modifier.weight(1f),
                count = notStartedCount,
                label = "Not Started",
                icon = Icons.Default.RadioButtonUnchecked,
                gradientColors = listOf(
                    Color(0xFF9E9E9E),
                    Color(0xFFBDBDBD)
                ),
                iconTint = Color(0xFF616161)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    count: Int,
    label: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.verticalGradient(gradientColors))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
                
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}
