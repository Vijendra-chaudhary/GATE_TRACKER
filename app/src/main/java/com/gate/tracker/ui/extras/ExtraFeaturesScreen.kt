package com.gate.tracker.ui.extras

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Extra Features Screen - Minimal design matching login and settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtraFeaturesScreen(
    onBackClick: () -> Unit,
    onMockTestsClick: () -> Unit,
    branchId: Int,
    onRevisionModeClick: () -> Unit,
    onMarkPreviousClick: () -> Unit,
    onShareProgressClick: () -> Unit
) {
    // Gradient background
    val gradientColors = listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surface
    )
    
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Extra Features",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(gradientColors))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
            
            // Active Features Section
            Text(
                text = "Advanced Tools",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 4.dp)
            )

            MinimalFeatureCard(
                icon = Icons.Default.Loop,
                title = "Revision Mode",
                description = "Track your syllabus revisions",
                onClick = onRevisionModeClick,
                iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer
            )

            MinimalFeatureCard(
                icon = Icons.Default.Assessment,
                title = "Mock Tests",
                description = "Track practice test scores",
                onClick = onMockTestsClick,
                iconBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                iconTint = MaterialTheme.colorScheme.onPrimaryContainer
            )

            MinimalFeatureCard(
                icon = Icons.Default.CheckCircle,
                title = "Mark Completed Topics",
                description = "Mark previously finished topics",
                onClick = onMarkPreviousClick,
                iconBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                iconTint = MaterialTheme.colorScheme.onSecondaryContainer
            )

            MinimalFeatureCard(
                icon = Icons.Default.Share,
                title = "Share Progress",
                description = "Share your study progress",
                onClick = onShareProgressClick,
                iconBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconTint = MaterialTheme.colorScheme.onTertiaryContainer
            )

            // Coming Soon Section
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Coming Soon",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                modifier = Modifier.padding(start = 4.dp)
            )

            MinimalFeatureCard(
                icon = Icons.Default.Widgets,
                title = "Home Screen Widgets",
                description = "View progress from your home screen",
                onClick = {},
                enabled = false
            )

            MinimalFeatureCard(
                icon = Icons.Default.School,
                title = "Study Timer",
                description = "Track your study sessions",
                onClick = {},
                enabled = false
            )

            MinimalFeatureCard(
                icon = Icons.Default.EmojiEvents,
                title = "Achievements",
                description = "Unlock achievements as you progress",
                onClick = {},
                enabled = false
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
}

/**
 * Minimal feature card component
 */
@Composable
fun MinimalFeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconTint: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (enabled) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (enabled)
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        tonalElevation = if (enabled) 1.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (enabled)
                    iconBackgroundColor
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (enabled)
                            iconTint
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (enabled)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    if (!enabled) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "Soon",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = if (enabled)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        }
    }
}
