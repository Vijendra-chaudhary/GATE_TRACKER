package com.gate.tracker.ui.branch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.gate.tracker.ui.components.BranchCard
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun BranchSelectionScreen(
    viewModel: BranchSelectionViewModel,
    onContinue: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val filteredBranches by viewModel.filteredBranches.collectAsState()
    val selectedBranch by viewModel.selectedBranch.collectAsState()
    var cardsVisible by remember { mutableStateOf(false) }
    
    // Trigger card animations
    LaunchedEffect(Unit) {
        delay(100)
        cardsVisible = true
    }
    
    // Match status bar color
    val view = LocalView.current
    val backgroundColor = MaterialTheme.colorScheme.background
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        window.statusBarColor = backgroundColor.hashCode()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Animated gradient background
        AnimatedGradientBackground()
        
        // Floating particles
        FloatingParticles()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Compact Modern Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // App Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "</>",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    // Title Section
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "GATE Tracker",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp
                        )
                        
                        Text(
                            text = "Choose your branch",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            
            // Branch List with cascading animation
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = if (selectedBranch != null) 100.dp else 20.dp
                )
            ) {
                itemsIndexed(filteredBranches, key = { _, branch -> branch.id }) { index, branch ->
                    AnimatedVisibility(
                        visible = cardsVisible,
                        enter = fadeIn(animationSpec = tween(400, delayMillis = index * 100)) + 
                                slideInVertically(initialOffsetY = { it / 2 })
                    ) {
                        BranchCard(
                            branch = branch,
                            isSelected = selectedBranch?.id == branch.id,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.selectBranch(branch)
                            }
                        )
                    }
                }
            }
        }
        
        // Enhanced Continue Button with Icon
        AnimatedVisibility(
            visible = selectedBranch != null,
            enter = fadeIn(animationSpec = tween(300)) + 
                    slideInVertically(initialOffsetY = { it })
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = { viewModel.onContinueClicked(onContinue) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Continue",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// Animated gradient background
@Composable
fun AnimatedGradientBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_offset"
    )
    
    // Theme-aware gradient colors
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val gradientColors = listOf(
            primaryColor.copy(alpha = 0.08f),
            secondaryColor.copy(alpha = 0.12f),
            tertiaryColor.copy(alpha = 0.08f)
        )
        
        drawRect(
            brush = Brush.linearGradient(
                colors = gradientColors,
                start = Offset(size.width * offset, 0f),
                end = Offset(size.width * (1 - offset), size.height)
            )
        )
    }
}

// Floating particles background
@Composable
fun FloatingParticles() {
    val particles = remember {
        List(8) {
            FloatingParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 6f + 4f,
                speed = Random.nextFloat() * 0.3f + 0.1f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    // Theme-aware particle color
    val particleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val yOffset = (time * particle.speed) % size.height
            val xOffset = particle.x * size.width + sin(time * 0.001f + particle.x) * 30f
            
            drawCircle(
                color = particleColor,
                radius = particle.size,
                center = Offset(xOffset, yOffset)
            )
        }
    }
}

data class FloatingParticle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)
