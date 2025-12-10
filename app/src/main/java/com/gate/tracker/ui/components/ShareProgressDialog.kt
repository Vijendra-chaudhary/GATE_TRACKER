package com.gate.tracker.ui.components

import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.view.drawToBitmap
import com.gate.tracker.util.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Dialog for sharing progress
 */
@Composable
fun ShareProgressDialog(
    branchName: String,
    completedChapters: Int,
    totalChapters: Int,
    currentStreak: Int,
    daysUntilExam: Int,
    onDismiss: () -> Unit,
    isRevisionMode: Boolean = false
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isSharing by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Share Your Progress",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Create a beautiful card to share your GATE preparation journey!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (isSharing) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Creating image...", style = MaterialTheme.typography.bodySmall)
                } else {
                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        
                        Button(
                            onClick = {
                                isSharing = true
                                coroutineScope.launch {
                                    try {
                                        withContext(Dispatchers.Default) {
                                            // Create the bitmap programmatically
                                            val bitmap = createProgressBitmap(
                                                branchName,
                                                completedChapters,
                                                totalChapters,
                                                currentStreak,
                                                daysUntilExam,
                                                isRevisionMode
                                            )
                                            
                                            // Save to cache
                                            val uri = BitmapUtils.saveBitmapToCache(context, bitmap)
                                            
                                            withContext(Dispatchers.Main) {
                                                // Share
                                                BitmapUtils.shareImage(context, uri)
                                                onDismiss()
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        isSharing = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Share")
                        }
                    }
                }
            }
        }
    }
}

/**
 * Create progress bitmap using Android Canvas
 */
private fun createProgressBitmap(
    branchName: String,
    completedChapters: Int,
    totalChapters: Int,
    currentStreak: Int,
    daysUntilExam: Int,
    isRevisionMode: Boolean = false
): Bitmap {
    val width = 600
    val height = 600
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    
    // Background gradient - purple for revision, blue for normal
    val paint = android.graphics.Paint().apply {
        shader = android.graphics.LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            if (isRevisionMode) intArrayOf(
                android.graphics.Color.parseColor("#9333EA"),
                android.graphics.Color.parseColor("#7C3AED")
            ) else intArrayOf(
                android.graphics.Color.parseColor("#667eea"),
                android.graphics.Color.parseColor("#764ba2")
            ),
            null,
            android.graphics.Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    
    // Text paint
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textAlign = android.graphics.Paint.Align.CENTER
        isAntiAlias = true
    }
    
    // Title
    textPaint.textSize = 32f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    canvas.drawText("📚 GATE Tracker", width / 2f, 60f, textPaint)
    
    // Branch name
    textPaint.textSize = 20f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
    canvas.drawText(branchName, width / 2f, 95f, textPaint)
    
    // Progress percentage
    val progressPercent = if (totalChapters > 0) (completedChapters * 100) / totalChapters else 0
    textPaint.textSize = 72f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    canvas.drawText("$progressPercent%", width / 2f, 280f, textPaint)
    
    textPaint.textSize = 24f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
    canvas.drawText("Complete", width / 2f, 315f, textPaint)
    
    // Stats
    textPaint.textSize = 28f
    canvas.drawText("📖", 150f, 400f, textPaint)
    canvas.drawText("🔥", 300f, 400f, textPaint)
    canvas.drawText("📅", 450f, 400f, textPaint)
    
    textPaint.textSize = 24f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
    canvas.drawText("$completedChapters/$totalChapters", 150f, 440f, textPaint)
    canvas.drawText("$currentStreak", 300f, 440f, textPaint)
    canvas.drawText("$daysUntilExam", 450f, 440f, textPaint)
    
    textPaint.textSize = 16f
    textPaint.typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
    canvas.drawText("Chapters", 150f, 465f, textPaint)
    canvas.drawText("Day Streak", 300f, 465f, textPaint)
    canvas.drawText("Days Left", 450f, 465f, textPaint)
    
    // Quote
    textPaint.textSize = 18f
    textPaint.alpha = 230
    canvas.drawText("\"Keep pushing! You're making", width / 2f, 520f, textPaint)
    canvas.drawText("great progress!\"", width / 2f, 545f, textPaint)
    
    // Footer
    textPaint.textSize = 14f
    textPaint.alpha = 180
    canvas.drawText("Shared from GATE Tracker", width / 2f, 580f, textPaint)
    
    return bitmap
}

