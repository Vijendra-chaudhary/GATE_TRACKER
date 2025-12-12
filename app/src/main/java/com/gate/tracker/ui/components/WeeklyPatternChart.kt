package com.gate.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.ui.calendar.WeekdayActivity

@Composable
fun WeeklyPatternChart(
    weeklyPattern: List<WeekdayActivity>,
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
                text = "📈 Weekly Pattern",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Text(
                text = "Study activity by day of week",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            
            if (weeklyPattern.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                val maxCount = weeklyPattern.maxOfOrNull { it.count } ?: 1
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyPattern.forEach { activity ->
                        WeekdayBar(
                            activity = activity,
                            maxCount = maxCount,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Summary
                val mostActiveDay = weeklyPattern.maxByOrNull { it.count }
                mostActiveDay?.let {
                    Text(
                        text = "Most active on ${it.dayName}s (${it.count} chapters)",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekdayBar(
    activity: WeekdayActivity,
    maxCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Count label
        if (activity.count > 0) {
            Text(
                text = activity.count.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }
        
        // Bar
        val heightFraction = if (maxCount > 0) activity.count.toFloat() / maxCount else 0f
        val barHeight = (180 * heightFraction).coerceAtLeast(4f).dp
        
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(barHeight)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    if (activity.count > 0) 
                        MaterialTheme.colorScheme.primary
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
        )
        
        // Day label
        Text(
            text = activity.dayName,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
