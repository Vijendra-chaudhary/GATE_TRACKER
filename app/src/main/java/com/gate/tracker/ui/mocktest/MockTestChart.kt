package com.gate.tracker.ui.mocktest

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Component that displays a line chart of mock test score trends
 */
@Composable
fun MockTestChart(
    dataPoints: List<Pair<Long, Float>>, // (timestamp, percentage)
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    val textColor = MaterialTheme.colorScheme.onSurface
    
    if (dataPoints.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data to display",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Chart Title
        Text(
            text = "Score Progression",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Canvas for drawing chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 8.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val padding = 40f
            val chartWidth = canvasWidth - (padding * 2)
            val chartHeight = canvasHeight - (padding * 2)

            // Find min/max for scaling
            val maxPercentage = 100f
            val minPercentage = 0f

            // Draw grid lines (horizontal)
            for (i in 0..4) {
                val y = padding + (chartHeight / 4) * i
                drawLine(
                    color = surfaceColor,
                    start = Offset(padding, y),
                    end = Offset(canvasWidth - padding, y),
                    strokeWidth = 1f
                )
            }

            // Calculate points
            val points = dataPoints.mapIndexed { index, (_, percentage) ->
                val x = padding + (chartWidth / (dataPoints.size - 1).coerceAtLeast(1)) * index
                val normalizedY = 1 - ((percentage - minPercentage) / (maxPercentage - minPercentage))
                val y = padding + (chartHeight * normalizedY)
                Offset(x, y)
            }

            // Draw gradient fill under line
            if (points.size >= 2) {
                val fillPath = Path().apply {
                    moveTo(points.first().x, canvasHeight - padding)
                    points.forEach { point ->
                        lineTo(point.x, point.y)
                    }
                    lineTo(points.last().x, canvasHeight - padding)
                    close()
                }
                
                drawPath(
                    path = fillPath,
                    color = primaryColor.copy(alpha = 0.2f)
                )
            }

            // Draw line connecting points
            if (points.size >= 2) {
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = primaryColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 3f
                    )
                }
            }

            // Draw dots at each point
            points.forEach { point ->
                drawCircle(
                    color = primaryColor,
                    radius = 5f,
                    center = point
                )
                drawCircle(
                    color = Color.White,
                    radius = 3f,
                    center = point
                )
            }
        }

        // X-axis labels (dates)
        if (dataPoints.size <= 5) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dataPoints.forEach { (timestamp, _) ->
                    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                    Text(
                        text = dateFormat.format(Date(timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            // Show only first, middle, and last date if too many points
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                Text(
                    text = dateFormat.format(Date(dataPoints.first().first)),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f)
                )
                Text(
                    text = dateFormat.format(Date(dataPoints[dataPoints.size / 2].first)),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f)
                )
                Text(
                    text = dateFormat.format(Date(dataPoints.last().first)),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}
