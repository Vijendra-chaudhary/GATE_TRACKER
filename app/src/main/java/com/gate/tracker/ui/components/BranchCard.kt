package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.data.local.entity.BranchEntity
import com.gate.tracker.ui.theme.getBranchColor
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun BranchCard(
    branch: BranchEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val branchColor = getBranchColor(branch.code)
    
    // Smooth spring animations
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )
    
    val elevation by animateFloatAsState(
        targetValue = if (isSelected) 8f else 2f,
        animationSpec = tween(300),
        label = "elevation"
    )
    
    // Pulsing glow effect for selected card
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    // Icon floating animation
    val iconOffset by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_float"
    )
    
    // Icon rotation on selection
    val iconRotation by animateFloatAsState(
        targetValue = if (isSelected) 360f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_rotation"
    )
    
    // Icon scale bounce
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "icon_scale"
    )
    
    // Glassmorphic background
    val glassBackground = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        )
    )
    
    val glassBorder = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                branchColor.copy(alpha = glowAlpha),
                branchColor.copy(alpha = glowAlpha * 0.6f)
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.1f)
            )
        )
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = if (isSelected) branchColor.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(glassBackground)
                .border(
                    width = 1.5.dp,
                    brush = glassBorder,
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // Colored accent strip (left side)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                branchColor.copy(alpha = if (isSelected) 1f else 0.6f),
                                branchColor.copy(alpha = if (isSelected) 0.7f else 0.3f)
                            )
                        )
                    )
            )
            
            // Particle effects for selected card
            if (isSelected) {
                IconParticles(branchColor = branchColor)
            }
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Enhanced animated icon box
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .offset(y = if (isSelected) iconOffset.dp else 0.dp)
                        .scale(iconScale),
                    contentAlignment = Alignment.Center
                ) {
                    // Gradient glow background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        branchColor.copy(alpha = if (isSelected) 0.3f else 0.15f),
                                        branchColor.copy(alpha = if (isSelected) 0.1f else 0.05f)
                                    )
                                )
                            )
                    )
                    
                    // Icon container with border
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                color = branchColor.copy(alpha = if (isSelected) 0.2f else 0.12f)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = branchColor.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getBranchIcon(branch.code),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = branchColor
                        )
                    }
                }
                
                // Branch info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Code badge
                    Text(
                        text = branch.code,
                        style = MaterialTheme.typography.labelLarge,
                        color = branchColor.copy(alpha = if (isSelected) 1f else 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.2.sp
                    )
                    
                    Text(
                        text = branch.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = branch.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        maxLines = 2
                    )
                }
                
                // Selection indicator
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = branchColor,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

// Get Material Icon for each branch
fun getBranchIcon(branchCode: String): ImageVector {
    return when (branchCode) {
        "CS" -> Icons.Default.Computer
        "EC" -> Icons.Default.Router
        "ME" -> Icons.Default.Settings
        "CE" -> Icons.Default.Home
        "EE" -> Icons.Default.ElectricBolt
        "DA" -> Icons.Default.Psychology  // Brain icon for AI/ML
        else -> Icons.Default.Stars
    }
}

// Floating particle effects around icon
@Composable
fun IconParticles(branchColor: Color) {
    val particles = remember {
        List(6) {
            Particle(
                angle = it * 60f,
                distance = 45f,
                size = Random.nextFloat() * 3f + 2f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val radius = particle.distance
            val angle = Math.toRadians((particle.angle + time).toDouble())
            val x = size.width / 2 + (radius * kotlin.math.cos(angle)).toFloat()
            val y = size.height / 2 + (radius * kotlin.math.sin(angle)).toFloat()
            
            drawCircle(
                color = branchColor.copy(alpha = 0.4f),
                radius = particle.size,
                center = Offset(x, y)
            )
        }
    }
}

data class Particle(
    val angle: Float,
    val distance: Float,
    val size: Float
)
