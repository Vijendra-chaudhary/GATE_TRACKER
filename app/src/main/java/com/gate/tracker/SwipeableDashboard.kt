package com.gate.tracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.ui.calendar.ProgressCalendarScreen
import com.gate.tracker.ui.calendar.ProgressCalendarViewModel
import com.gate.tracker.ui.dashboard.DashboardScreen
import com.gate.tracker.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SwipeableDashboard(
    branchId: Int,
    dashboardViewModel: DashboardViewModel,
    calendarViewModel: ProgressCalendarViewModel,
    onSubjectClick: (Int) -> Unit,
    onProgressClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onExtrasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    
    // Check revision mode
    val isRevisionMode by dashboardViewModel.isRevisionMode.collectAsState()
    
    // Set system navigation bar to match app background (Solid)
    val view = androidx.compose.ui.platform.LocalView.current
    val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
    
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            window.navigationBarColor = navigationBarColor
            androidx.core.view.WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Horizontal pager for swipeable content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    // Dashboard
                    DashboardScreen(
                        branchId = branchId,
                        viewModel = dashboardViewModel,
                        onSubjectClick = onSubjectClick,
                        onProgressClick = onProgressClick,
                        onSettingsClick = onSettingsClick,
                        onExtrasClick = onExtrasClick
                    )
                }
                1 -> {
                    // Calendar
                    ProgressCalendarScreen(
                        viewModel = calendarViewModel
                    )
                }
            }
        }
        
        // Bottom tab indicator/navigator
        BottomTabBar(
            pagerState = pagerState,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            isRevisionMode = isRevisionMode,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding() // Push up above the solid nav bar
                .padding(16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomTabBar(
    pagerState: PagerState,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isRevisionMode: Boolean = false
) {
    // Glass Effect Gradients
    val glassBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), // Top: More opaque to catch light
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)  // Bottom: More transparent
        )
    )
    
    val glassBorder = Brush.verticalGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.8f),      // Top highlight (Rim light)
            Color.White.copy(alpha = 0.2f),      // Middle transparency
            Color.White.copy(alpha = 0.1f)       // Bottom lowlight
        )
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp)) // Slightly more rounded
            .background(glassBackground) // Gradient Glass Fill
            .border(
                width = 1.dp,
                brush = glassBorder, // Shining Edge Border
                shape = RoundedCornerShape(32.dp)
            )
            .padding(1.dp) // Internal spacing for the "thick glass" look
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(
                icon = Icons.Default.Home,
                label = "Dashboard",
                isSelected = pagerState.currentPage == 0,
                onClick = { onTabSelected(0) },
                isRevisionMode = isRevisionMode
            )
            
            TabItem(
                icon = Icons.Default.CalendarMonth,
                label = "Calendar",
                isSelected = pagerState.currentPage == 1,
                onClick = { onTabSelected(1) },
                isRevisionMode = isRevisionMode
            )
        }
    }
}

@Composable
fun TabItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isRevisionMode: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    
    // Animate selection state
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tab_scale"
    )
    
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "background_alpha"
    )
    
    val contentAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(durationMillis = 200),
        label = "content_alpha"
    )
    
    // Color based on revision mode
    val tabColor = if (isRevisionMode) {
        Color(0xFF9333EA) // Purple for revision mode
    } else {
        MaterialTheme.colorScheme.primary // Blue for normal mode
    }
    
    // Glassmorphic selected background
    val selectedGradient = Brush.horizontalGradient(
        colors = listOf(
            tabColor.copy(alpha = 0.30f * backgroundAlpha),
            tabColor.copy(alpha = 0.20f * backgroundAlpha)
        )
    )
    
    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (backgroundAlpha > 0f) {
                    Modifier
                        .background(selectedGradient)
                        .border(
                            width = 1.dp,
                            color = tabColor.copy(alpha = 0.50f * backgroundAlpha),
                            shape = RoundedCornerShape(16.dp)
                        )
                } else {
                    Modifier
                }
            )
    ) {
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = tabColor.copy(alpha = contentAlpha)
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(20.dp)
            )
            
            // Animate label appearance
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(animationSpec = tween(200)) + expandHorizontally(),
                exit = fadeOut(animationSpec = tween(200)) + shrinkHorizontally()
            ) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
