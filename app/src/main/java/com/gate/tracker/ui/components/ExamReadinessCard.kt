package com.gate.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.util.DateUtils
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Composable
fun ExamReadinessCard(
    velocity: Float, // Chapters per day
    projectedDate: Long?, // Null if velocity is 0
    examDate: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Exam Readiness",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (velocity > 0) String.format("%.1f chapters / day", velocity) else "Not enough data yet",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Status Badge
                val isSafe = projectedDate != null && projectedDate < examDate
                val statusColor = if (isSafe) Color(0xFF10B981) else Color(0xFFEF4444) // Green or Red
                val statusText = if (velocity <= 0) "UNKNOWN" else if (isSafe) "ON TRACK" else "DANGER"
                
                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Projection Logic
            if (velocity <= 0 || projectedDate == null) {
                Text(
                    text = "Start completing chapters consistently to see your projected finish date.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                val today = System.currentTimeMillis()
                val totalDays = TimeUnit.MILLISECONDS.toDays(examDate - today).coerceAtLeast(1)
                
                // Visualization
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val lineY = height / 2
                    
                    // Main Timeline Base (Gray)
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(0f, lineY),
                        end = Offset(width, lineY),
                        strokeWidth = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    // Calculate positions (0 = Today, 1 = Exam Date)
                    // We need to extend the scale if projected date is beyond exam date
                    val maxDate = maxOf(examDate, projectedDate)
                    val timeRange = maxDate - today
                    // Add buffer (10%)
                    val scaleRange = (timeRange * 1.1).toLong()
                    
                    fun getX(date: Long): Float {
                        if (date <= today) return 0f
                        val progress = (date - today).toFloat() / scaleRange
                        return (width * progress).coerceIn(0f, width)
                    }
                    
                    val examX = getX(examDate)
                    val projX = getX(projectedDate)
                    
                    // Draw Progress Bar (Today -> Projected)
                    drawLine(
                        color = if (projectedDate < examDate) Color(0xFF10B981) else Color(0xFFEF4444),
                        start = Offset(0f, lineY),
                        end = Offset(projX, lineY),
                        strokeWidth = 4.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    // Draw Exam Flag
                    drawLine(
                        color = Color.Black,
                        start = Offset(examX, lineY - 15.dp.toPx()),
                        end = Offset(examX, lineY + 15.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                    
                    // Draw Projected Flag (Circle)
                    drawCircle(
                        color = if (projectedDate < examDate) Color(0xFF10B981) else Color(0xFFEF4444),
                        radius = 6.dp.toPx(),
                        center = Offset(projX, lineY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 3.dp.toPx(),
                        center = Offset(projX, lineY)
                    )
                }
                
                // Labels below canvas using simple Box/Row logic would be hard to align perfectly with canvas.
                // Let's print text details below.
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Estimated Finish",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = DateUtils.formatDate(projectedDate),
                            style = MaterialTheme.typography.titleSmall,
                            color = if (projectedDate < examDate) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Exam Date",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = DateUtils.formatDate(examDate),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
