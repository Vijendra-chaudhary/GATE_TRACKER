package com.gate.tracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * Enhanced dialog for enabling/disabling revision mode with smooth animations
 */
@Composable
fun RevisionModeDialog(
    isCurrentlyEnabled: Boolean,
    onDismiss: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    var enabled by remember { mutableStateOf(isCurrentlyEnabled) }
    val isDarkTheme = isSystemInDarkTheme()
    
    // Animated colors for gradient - adjusted for theme
    val topColor by animateColorAsState(
        targetValue = when {
            // Light mode: Use very light, almost-white purples for best text contrast
            !isDarkTheme && enabled -> Color(0xFFF3E8FF)  // Slightly darker than before
            !isDarkTheme && !enabled -> Color(0xFFFAF8FF)  // Nearly white with subtle purple
            // Dark mode: Original darker colors
            enabled -> Color(0xFF9333EA)
            else -> Color(0xFF667eea)
        },
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "topColor"
    )
    
    val bottomColor by animateColorAsState(
        targetValue = when {
            // Light mode: Very light gradient endpoint
            !isDarkTheme && enabled -> Color(0xFFEEE4FF)  // Slightly darker than before
            !isDarkTheme && !enabled -> Color(0xFFF7F5FF)  // Very pale purple
            // Dark mode: Original darker colors
            enabled -> Color(0xFF7C3AED)
            else -> Color(0xFF764ba2)
        },
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "bottomColor"
    )
    
    // Text color - dark for light mode, white for dark mode
    val textColor = if (isDarkTheme) Color.White else Color(0xFF3B0764)
    
    // Icon rotation animation
    val iconRotation by animateFloatAsState(
        targetValue = if (enabled) 360f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "iconRotation"
    )
    
    // Icon scale animation
    val iconScale by animateFloatAsState(
        targetValue = if (enabled) 1.1f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "iconScale"
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(topColor, bottomColor)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Large animated icon
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = if (isDarkTheme) 0.2f else 0.25f),
                        modifier = Modifier
                            .size(80.dp)
                            .scale(iconScale)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (enabled) Icons.Default.Loop else Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .rotate(iconRotation),
                                tint = textColor
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Revision Mode",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Description
                    Text(
                        text = if (enabled) {
                            "Track your revision progress separately. Perfect for exam preparation!"
                        } else {
                            "Ready to revise? Enable this mode to track your syllabus revision independently."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Features card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.15f) else Color(0xFF9333EA).copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FeatureItem("📚", "Separate revision tracking", textColor)
                            FeatureItem("🎨", "Purple theme UI", textColor)
                            FeatureItem("✅", "Independent progress", textColor)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Toggle switch with label
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = if (isDarkTheme) 0.2f else 0.25f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (enabled) "Mode Active" else "Enable Mode",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Switch(
                                checked = enabled,
                                onCheckedChange = { enabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = if (isDarkTheme) Color.White else Color(0xFF7C3AED),
                                    checkedTrackColor = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color(0xFF7C3AED).copy(alpha = 0.3f),
                                    uncheckedThumbColor = if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color(0xFF9333EA).copy(alpha = 0.6f),
                                    uncheckedTrackColor = if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color(0xFF9333EA).copy(alpha = 0.2f)
                                )
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = textColor
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = Brush.linearGradient(listOf(textColor, textColor))
                            )
                        ) {
                            Text("Cancel", fontWeight = FontWeight.SemiBold)
                        }
                        
                        Button(
                            onClick = {
                                onToggle(enabled)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkTheme) Color.White else Color(0xFF7C3AED),
                                contentColor = if (isDarkTheme) {
                                    when {
                                        enabled -> Color(0xFF7C3AED)
                                        else -> Color(0xFF764ba2)
                                    }
                                } else Color.White
                            )
                        ) {
                            Text("Confirm", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(emoji: String, text: String, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor.copy(alpha = 0.9f)
        )
    }
}
