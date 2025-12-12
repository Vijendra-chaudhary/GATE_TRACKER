package com.gate.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun ProgressLineGraph(
    dailyCompletions: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val data = dailyCompletions.values.toList()
    val maxValue = data.maxOrNull() ?: 1
    val totalCompleted = data.sum()
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Progress Trend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Last 30 days",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$totalCompleted",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "chapters",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Graph
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                
                android.util.Log.d("GATE_TRACKER", "Line Graph - Data size: ${data.size}, Total: $totalCompleted, Max: $maxValue")
                android.util.Log.d("GATE_TRACKER", "Line Graph - Data: $data")
                
                // Only skip drawing if there's absolutely no data
                if (totalCompleted == 0) {
                    // Draw "No data yet" placeholder text
                    android.util.Log.d("GATE_TRACKER", "Line Graph - No data to display")
                    return@Canvas
                }
                
                val xStep = canvasWidth / max(1f, (data.size - 1).toFloat())
                val actualMax = if (maxValue > 0) maxValue else 1
                val yScale = (canvasHeight - 40f) / actualMax
                
                // Draw grid lines
                val gridLineColor = Color.Gray.copy(alpha = 0.1f)
                for (i in 0..4) {
                    val y = canvasHeight - (i * (canvasHeight / 4))
                    drawLine(
                        color = gridLineColor,
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                }
                
                // Draw gradient area under curve
                val gradientPath = Path().apply {
                    moveTo(0f, canvasHeight)
                    data.forEachIndexed { index, value ->
                        val x = index * xStep
                        val y = canvasHeight - (value * yScale) - 20f
                        if (index == 0) {
                            lineTo(x, y)
                        } else {
                            lineTo(x, y)
                        }
                    }
                    lineTo(canvasWidth, canvasHeight)
                    close()
                }
                
                drawPath(
                    path = gradientPath,
                    color = Color(0xFF4A90E2).copy(alpha = 0.1f)
                )
                
                // Draw line
                val linePath = Path()
                data.forEachIndexed { index, value ->
                    val x = index * xStep
                    val y = canvasHeight - (value * yScale) - 20f
                    
                    if (index == 0) {
                        linePath.moveTo(x, y)
                    } else {
                        linePath.lineTo(x, y)
                    }
                }
                
                drawPath(
                    path = linePath,
                    color = Color(0xFF4A90E2),
                    style = Stroke(
                        width = 3f,
                        cap = StrokeCap.Round
                    )
                )
                
                // Draw data points
                data.forEachIndexed { index, value ->
                    val x = index * xStep
                    val y = canvasHeight - (value * yScale) - 20f
                    
                    // Outer circle (white background)
                    drawCircle(
                        color = Color.White,
                        radius = 5f,
                        center = Offset(x, y)
                    )
                    
                    // Inner circle (colored)
                    drawCircle(
                        color = Color(0xFF4A90E2),
                        radius = 3f,
                        center = Offset(x, y)
                    )
                }
            }
            
            // Legend
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "30 days ago",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "Today",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}
