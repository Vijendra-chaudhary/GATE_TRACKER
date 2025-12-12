package com.gate.tracker.ui.resources

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.ResourceEntity
import com.gate.tracker.data.local.entity.ResourceType
import java.text.SimpleDateFormat
import java.util.*

/**
 * Card displaying a single resource
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResourceCard(
    resource: ResourceEntity,
    onLongPress: () -> Unit
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    // Type-specific colors
    val (containerColor, contentColor) = when (resource.resourceType) {
        ResourceType.PDF -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        ResourceType.URL -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
        ResourceType.IMAGE -> Color(0xFFF3E5F5) to Color(0xFF6A1B9A)
    }
    
    val icon = when (resource.resourceType) {
        ResourceType.PDF -> Icons.Default.PictureAsPdf
        ResourceType.URL -> Icons.Default.Link
        ResourceType.IMAGE -> Icons.Default.Image
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    try {
                        when (resource.resourceType) {
                            ResourceType.URL -> {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.uri))
                                context.startActivity(intent)
                            }
                            ResourceType.PDF -> {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(Uri.parse(resource.uri), "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Try with chooser if no PDF viewer
                                    val chooser = Intent.createChooser(intent, "Open PDF with...")
                                    context.startActivity(chooser)
                                }
                            }
                            ResourceType.IMAGE -> {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(Uri.parse(resource.uri), "image/*")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Try with chooser if no image viewer
                                    val chooser = Intent.createChooser(intent, "Open image with...")
                                    context.startActivity(chooser)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        android.widget.Toast.makeText(
                            context,
                            "Cannot open: ${e.message}",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onLongClick = onLongPress
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = containerColor,
                modifier = Modifier.size(56.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content  
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Type badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = containerColor
                    ) {
                        Text(
                            text = when (resource.resourceType) {
                                ResourceType.PDF -> "PDF"
                                ResourceType.URL -> "LINK"
                                ResourceType.IMAGE -> "IMAGE"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = contentColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    
                    if (resource.fileSize != null) {
                        Text(
                            text = formatFileSize(resource.fileSize),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = dateFormat.format(Date(resource.createdAt)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (resource.description.isNotBlank()) {
                    Text(
                        text = resource.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Arrow indicator
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Format file size to human-readable format
 */
private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
    }
}
