package com.gate.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.util.DateUtils
import java.util.Calendar


@Composable
fun MonthHeader(
    month: Int,
    year: Int,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTodayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous month",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${DateUtils.getMonthName(month)} $year",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            TextButton(onClick = onTodayClick) {
                Text(
                    text = "Today",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        IconButton(onClick = onNextClick) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next month",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CalendarGrid(
    year: Int,
    month: Int,
    completionCounts: Map<Int, Int>,
    selectedDate: Long?,
    onDateClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = DateUtils.getDaysInMonth(year, month)
    val firstDayOfWeek = DateUtils.getFirstDayOfWeek(year, month)
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Day of week headers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Calendar grid
        var dayCounter = 1
        for (week in 0 until 6) {
            if (dayCounter > daysInMonth) break
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (dayOfWeek in 1..7) {
                    if ((week == 0 && dayOfWeek < firstDayOfWeek) || dayCounter > daysInMonth) {
                        // Empty cell
                        Box(modifier = Modifier.weight(1f).height(50.dp))
                    } else {
                        val day = dayCounter
                        val count = completionCounts[day] ?: 0
                        val timestamp = DateUtils.getTimestampForDate(year, month, day)
                        val isSelected = selectedDate?.let { DateUtils.isSameDay(it, timestamp) } ?: false
                        
                        CalendarDayCell(
                            day = day,
                            count = count,
                            isSelected = isSelected,
                            isToday = isToday(year, month, day),
                            onClick = { onDateClick(timestamp) },
                            modifier = Modifier.weight(1f)
                        )
                        dayCounter++
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    day: Int,
    count: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    count > 0 -> getCompletionColor(count)
                    else -> Color.Transparent
                }
            )
            .clickable(onClick = onClick)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                fontSize = 14.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                    isToday -> MaterialTheme.colorScheme.primary
                    count > 0 -> Color.White
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
            
            if (count > 0) {
                CompletionIndicator(count = count, isSelected = isSelected)
            }
        }
    }
}

@Composable
fun CompletionIndicator(
    count: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    Color.White.copy(alpha = 0.9f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (count > 9) "9+" else count.toString(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected)
                Color.White
            else
                getCompletionColor(count)
        )
    }
}

private fun getCompletionColor(count: Int): Color {
    return when {
        count >= 10 -> Color(0xFF1E3A8A) // Deep blue
        count >= 7 -> Color(0xFF2563EB) // Strong blue
        count >= 5 -> Color(0xFF3B82F6) // Medium blue
        count >= 3 -> Color(0xFF60A5FA) // Light blue
        count >= 1 -> Color(0xFF93C5FD) // Very light blue
        else -> Color.Transparent
    }
}

private fun isToday(year: Int, month: Int, day: Int): Boolean {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.YEAR) == year &&
            calendar.get(Calendar.MONTH) == month &&
            calendar.get(Calendar.DAY_OF_MONTH) == day
}
