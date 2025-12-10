package com.gate.tracker.ui.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.gate.tracker.ui.components.SubjectCard
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.gate.tracker.ui.dashboard.DashboardViewModel
import androidx.compose.foundation.isSystemInDarkTheme

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun SubjectsOverviewScreen(
    branchId: Int,
    viewModel: DashboardViewModel,
    onSubjectClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val selectedBranch by viewModel.selectedBranch.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val totalCompleted by viewModel.totalCompleted.collectAsState()
    val totalChapters by viewModel.totalChapters.collectAsState()
    val completedSubjects by viewModel.completedSubjects.collectAsState()
    val inProgressSubjects by viewModel.inProgressSubjects.collectAsState()
    val notStartedSubjects by viewModel.notStartedSubjects.collectAsState()
    val isRevisionMode by viewModel.isRevisionMode.collectAsState()
    val totalRevised by viewModel.totalRevised.collectAsState()
    
    LaunchedEffect(branchId) {
        viewModel.loadDashboard(branchId)
    }
    
    // Match status bar color with revision mode
    // Match status bar color with revision mode
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    val appBarColor = if (isRevisionMode) {
        if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFFF3E8FF)
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val appBarTextColor = if (isRevisionMode) {
        if (isDarkTheme) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color(0xFF3B0764)
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
        
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        window.statusBarColor = appBarColor.hashCode()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(appBarColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = appBarTextColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "All Subjects",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = appBarTextColor
                    )
                    Text(
                        text = selectedBranch?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = appBarTextColor.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Swipeable Progress Cards
                val pagerState = rememberPagerState(pageCount = { 4 })
                
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isRevisionMode)
                                if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFFF3E8FF)
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth()
                        ) { page ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (page) {
                                    0 -> {
                                        // Total Progress
                                        Column {
                                            Text(
                                                text = "Total Progress",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = if (isRevisionMode) {
                                                    "$totalRevised / $totalChapters chapters"
                                                } else {
                                                    "$totalCompleted / $totalChapters chapters"
                                                },
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            )
                                        }
                                        Text(
                                            text = "${if (totalChapters > 0) ((if (isRevisionMode) totalRevised else totalCompleted) * 100 / totalChapters) else 0}%",
                                            style = MaterialTheme.typography.displaySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    1 -> {
                                        // Completed Subjects
                                        Column {
                                            Text(
                                                text = "Completed",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Subjects finished",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            )
                                        }
                                        Text(
                                            text = "$completedSubjects",
                                            style = MaterialTheme.typography.displaySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                    2 -> {
                                        // In Progress
                                        Column {
                                            Text(
                                                text = "In Progress",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Currently studying",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            )
                                        }
                                        Text(
                                            text = "$inProgressSubjects",
                                            style = MaterialTheme.typography.displaySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFF59E0B)
                                        )
                                    }
                                    3 -> {
                                        // Not Started
                                        Column {
                                            Text(
                                                text = "Not Started",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Yet to begin",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            )
                                        }
                                        Text(
                                            text = "$notStartedSubjects",
                                            style = MaterialTheme.typography.displaySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Page indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                    )
                            )
                            if (index < 3) Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Subjects (${subjects.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = { onSubjectClick(subject.id) },
                    isRevisionMode = isRevisionMode
                )
            }
        }
    }
}
