package com.gate.tracker.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.ui.components.CalendarGrid
import com.gate.tracker.ui.components.ExamReadinessCard
import com.gate.tracker.ui.components.MonthHeader
import com.gate.tracker.ui.components.ProgressLineGraph
import com.gate.tracker.ui.components.YearlyHeatmap
import com.gate.tracker.ui.components.WeeklyPatternChart
import com.gate.tracker.ui.components.SubjectDistributionChart
import com.gate.tracker.ui.components.SwipeHint
import com.gate.tracker.util.DateUtils
import java.util.Calendar
import androidx.compose.runtime.*
import kotlinx.coroutines.delay




@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ProgressCalendarScreen(
    viewModel: ProgressCalendarViewModel,
    modifier: Modifier = Modifier
) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentWeek by viewModel.currentWeek.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val completionCounts by viewModel.completionCounts.collectAsState()
    val chaptersOnSelectedDate by viewModel.chaptersOnSelectedDate.collectAsState()
    val monthStats by viewModel.monthStats.collectAsState()
    
    // Match status bar color with app bar
    val view = androidx.compose.ui.platform.LocalView.current
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    androidx.compose.runtime.SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        window.statusBarColor = primaryContainer.hashCode()
        androidx.core.view.WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }
    
    Scaffold(
        topBar = {
            // Theme-matching app bar
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .statusBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Title Section
                    androidx.compose.foundation.layout.Column {
                        androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
                            // Calendar-themed icon
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.CalendarToday,
                                    contentDescription = "Calendar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            androidx.compose.foundation.layout.Column {
                                Text(
                                    text = "Study Calendar",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Track your progress",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp), // Add top padding to separate from App Bar
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Month header
        item {

            MonthHeader(
                month = currentMonth.get(Calendar.MONTH),
                year = currentMonth.get(Calendar.YEAR),
                onPreviousClick = { viewModel.navigateToPreviousMonth() },
                onNextClick = { viewModel.navigateToNextMonth() },
                onTodayClick = { viewModel.navigateToToday() }
            )
        }
        
        // Swipeable Content (Calendar, Graph, Heatmap, Weekly, Distribution, Readiness)
        item {
            val dailyCompletionsForMonth by viewModel.dailyCompletionsForMonth.collectAsState()
            val readiness by viewModel.examReadiness.collectAsState()
            val yearlyActivity by viewModel.yearlyActivity.collectAsState()
            val weeklyPatternForWeek by viewModel.weeklyPatternForWeek.collectAsState()
            val subjectDistribution by viewModel.subjectDistribution.collectAsState()
            
            // Pager state for 6 pages
            val pagerState = rememberPagerState(pageCount = { 6 })
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Horizontal Pager with FIXED height to prevent jitter
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(370.dp) // Fixed height for smooth swiping
                ) { page ->
                     when (page) {
                        0 -> {
                            // Page 1: Calendar Grid
                            Card(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                CalendarGrid(
                                    year = currentMonth.get(Calendar.YEAR),
                                    month = currentMonth.get(Calendar.MONTH),
                                    completionCounts = completionCounts,
                                    selectedDate = selectedDate,
                                    onDateClick = { date ->
                                        if (selectedDate == date) {
                                            viewModel.clearSelectedDate()
                                        } else {
                                            viewModel.selectDate(date)
                                        }
                                    },
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        1 -> {
                            // Page 2: Weekly Pattern with Navigation
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Week Navigation Header
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { viewModel.navigateToPreviousWeek() }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Previous Week",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // Get week range
                                            val weekStart = currentWeek.clone() as java.util.Calendar
                                            weekStart.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY)
                                            val weekEnd = currentWeek.clone() as java.util.Calendar
                                            weekEnd.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SATURDAY)
                                            
                                            val dateFormat = java.text.SimpleDateFormat("MMM d", java.util.Locale.getDefault())
                                            val yearFormat = java.text.SimpleDateFormat("yyyy", java.util.Locale.getDefault())
                                            
                                            Text(
                                                text = "${dateFormat.format(weekStart.time)} - ${dateFormat.format(weekEnd.time)}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Text(
                                                text = yearFormat.format(weekStart.time),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                            )
                                        }
                                        
                                        IconButton(onClick = { viewModel.navigateToNextWeek() }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "Next Week",
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                    }
                                }
                                
                                // Weekly Pattern Chart
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    WeeklyPatternChart(
                                        weeklyPattern = weeklyPatternForWeek,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        2 -> {
                            // Page 3: Progress Graph (Centered)
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                ProgressLineGraph(
                                    dailyCompletions = dailyCompletionsForMonth,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                        3 -> {
                            // Page 4: Subject Distribution
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                SubjectDistributionChart(
                                    subjects = subjectDistribution,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                        4 -> {
                            // Page 5: Exam Readiness (Centered)
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                readiness?.let { data ->
                                    ExamReadinessCard(
                                        velocity = data.velocity,
                                        projectedDate = data.projectedFinishDate,
                                        examDate = data.examDate,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                } ?: Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Loading data...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        5 -> {
                            // Page 6: Yearly Heatmap
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                YearlyHeatmap(
                                    yearlyActivity = yearlyActivity,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                
                // Page Indicators with Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val pageLabels = listOf("Calendar", "Weekly", "Graph", "Subjects", "Readiness", "Heatmap")
                    
                    pageLabels.forEachIndexed { index, label ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                    )
                            )
                            
                            if (pagerState.currentPage == index) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Statistics section
        item {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    icon = Icons.Default.Whatshot,
                    title = "Current Streak",
                    value = "${monthStats.currentStreak}",
                    subtitle = "days",
                    iconTint = Color(0xFFFF6B35),
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    icon = Icons.Default.TrendingUp,
                    title = "Best Streak",
                    value = "${monthStats.longestStreak}",
                    subtitle = "days",
                    iconTint = Color(0xFF4ECDC4),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    icon = Icons.Default.CalendarToday,
                    title = "Days Studied",
                    value = "${monthStats.totalDaysStudied}",
                    subtitle = "total",
                    iconTint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    icon = Icons.Default.TrendingUp,
                    title = "Avg/Day",
                    value = String.format("%.1f", monthStats.averagePerDay),
                    subtitle = "chapters",
                    iconTint = Color(0xFF9B59B6),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Month comparison
        if (monthStats.lastMonthTotal > 0 || monthStats.thisMonthTotal > 0) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                MonthComparisonCard(
                    thisMonthTotal = monthStats.thisMonthTotal,
                    lastMonthTotal = monthStats.lastMonthTotal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
        
        // Best day info
        monthStats.bestDay?.let { (date, count) ->
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                BestDayCard(
                    date = date,
                    count = count,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
        
        // Selected date details
        if (selectedDate != null && chaptersOnSelectedDate.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Text(
                    text = "Completed on ${DateUtils.formatDate(selectedDate, "dd MMMM yyyy")}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            items(chaptersOnSelectedDate) { chapter ->
                ChapterCompletionItem(
                    chapter = chapter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        } else if (selectedDate != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Text(
                    text = "No chapters completed on ${DateUtils.formatDate(selectedDate, "dd MMM yyyy")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        
        // Legend
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            CompletionLegend(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    title: String,
    value: String,
    subtitle: String,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun MonthComparisonCard(
    thisMonthTotal: Int,
    lastMonthTotal: Int,
    modifier: Modifier = Modifier
) {
    val difference = thisMonthTotal - lastMonthTotal
    val percentChange = if (lastMonthTotal > 0) {
        ((difference.toFloat() / lastMonthTotal) * 100).toInt()
    } else {
        0
    }
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (difference >= 0) 
                Color(0xFF10B981).copy(alpha = 0.1f) 
            else 
                Color(0xFFEF4444).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "This Month vs Last Month",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "$thisMonthTotal",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = "vs $lastMonthTotal",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (difference >= 0) "+$difference" else "$difference",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (difference >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
                )
                
                if (percentChange != 0) {
                    Text(
                        text = if (percentChange >= 0) "+$percentChange%" else "$percentChange%",
                        fontSize = 12.sp,
                        color = if (difference >= 0) Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

@Composable
private fun BestDayCard(
    date: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFBBF24).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🏆 Best Day",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "$count chapters",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFBBF24)
            )
        }
    }
}

@Composable
private fun ChapterCompletionItem(
    chapter: ChapterEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                chapter.category?.let { category ->
                    Text(
                        text = category,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Text(
                text = chapter.completedDate?.let { 
                    DateUtils.formatDate(it, "hh:mm a") 
                } ?: "",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun CompletionLegend(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Completion Legend",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LegendItem("1-2", Color(0xFF93C5FD), Modifier.weight(1f))
                LegendItem("3-4", Color(0xFF60A5FA), Modifier.weight(1f))
                LegendItem("5-6", Color(0xFF3B82F6), Modifier.weight(1f))
                LegendItem("7-9", Color(0xFF2563EB), Modifier.weight(1f))
                LegendItem("10+", Color(0xFF1E3A8A), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun LegendItem(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color, RoundedCornerShape(4.dp))
        )
        
        Text(
            text = label,
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
