package com.gate.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.util.DateUtils
import java.util.Calendar

@Composable
fun YearlyHeatmap(
    yearlyActivity: Map<String, Int>,
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
                text = "📊 Yearly Activity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Text(
                text = "Last 365 days",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                // Show last 53 weeks (371 days to ensure full weeks)
                items(53) { weekIndex ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        // 7 days per week
                        for (dayIndex in 0..6) {
                            val daysAgo = (52 - weekIndex) * 7 + (6 - dayIndex)
                            val date = System.currentTimeMillis() - (daysAgo.toLong() * 24 * 60 * 60 * 1000)
                            val dateKey = DateUtils.getDateKey(date)
                            val count = yearlyActivity[dateKey] ?: 0
                            
                            DayCell(count = count)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Less",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 4.dp)
                )
                
                listOf(0, 1, 3, 5, 10).forEach { count ->
                    DayCell(count = count, modifier = Modifier.padding(horizontal = 2.dp))
                }
                
                Text(
                    text = "More",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    count: Int,
    modifier: Modifier = Modifier
) {
    val color = when {
        count == 0 -> Color(0xFFE5E7EB) // Gray-200
        count <= 2 -> Color(0xFF93C5FD) // Blue-300
        count <= 4 -> Color(0xFF60A5FA) // Blue-400
        count <= 6 -> Color(0xFF3B82F6) // Blue-500
        count <= 9 -> Color(0xFF2563EB) // Blue-600
        else -> Color(0xFF1E3A8A) // Blue-900
    }
    
    Box(
        modifier = modifier
            .size(12.dp)
            .background(color, shape = RoundedCornerShape(2.dp))
    )
}
