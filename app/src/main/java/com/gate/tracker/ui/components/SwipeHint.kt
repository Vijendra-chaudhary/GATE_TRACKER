package com.gate.tracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwipeLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SwipeHint(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }
    
    // Auto dismiss after showing for 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        isVisible = false
        delay(300)
        onDismiss()
    }
    
    // Swipe animation - left and right motion
    val infiniteTransition = rememberInfiniteTransition(label = "swipe_hint")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swipe_offset"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "fade"
    )
    
    if (alpha > 0f) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .graphicsLayer { this.alpha = alpha },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.graphicsLayer { translationX = offsetX }
            ) {
                Icon(
                    imageVector = Icons.Default.SwipeLeft,
                    contentDescription = "Swipe",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = "Swipe to view more",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}
