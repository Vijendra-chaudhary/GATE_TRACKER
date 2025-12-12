package com.gate.tracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.ui.calendar.SubjectProgress
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SubjectDistributionChart(
    subjects: List<SubjectProgress>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "🎯 Subject Distribution",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Text(
                text = "This month's focus",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            
            if (subjects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chapters completed this month",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Donut Chart
                    DonutChart(
                        subjects = subjects,
                        modifier = Modifier
                            .size(160.dp)
                            .weight(1f)
                    )
                    
                    // Legend
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subjects.take(5).forEach { subject ->
                            LegendItem(
                                name = subject.subjectName,
                                count = subject.completedCount,
                                color = subject.color
                            )
                        }
                        
                        if (subjects.size > 5) {
                            val others = subjects.drop(5).sumOf { it.completedCount }
                            LegendItem(
                                name = "Others",
                                count = others,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    subjects: List<SubjectProgress>,
    modifier: Modifier = Modifier
) {
    val topSubjects = subjects.take(5)
    val total = subjects.sumOf { it.completedCount }
    
    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2
        val strokeWidth = canvasSize * 0.25f
        
        var startAngle = -90f
        
        topSubjects.forEach { subject ->
            val sweepAngle = (subject.completedCount.toFloat() / total) * 360f
            
            drawArc(
                color = subject.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
                style = Stroke(width = strokeWidth)
            )
            
            startAngle += sweepAngle
        }
        
        // Handle "Others" if more than 5 subjects
        if (subjects.size > 5) {
            val othersCount = subjects.drop(5).sumOf { it.completedCount }
            val sweepAngle = (othersCount.toFloat() / total) * 360f
            
            drawArc(
                color = Color.Gray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(canvasSize - strokeWidth, canvasSize - strokeWidth),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

@Composable
private fun LegendItem(
    name: String,
    count: Int,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
        
        Text(
            text = count.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
