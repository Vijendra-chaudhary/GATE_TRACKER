package com.gate.tracker.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gate.tracker.data.local.entity.ChapterEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterItem(
    chapter: ChapterEntity,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    hasNote: Boolean = false,
    hasNoteText: Boolean = false,
    isImportant: Boolean = false,
    needsRevision: Boolean = false,
    onNoteClick: () -> Unit = {},
    isRevisionMode: Boolean = false // New parameter
) {
    // Use appropriate completion field based on mode
    val isChecked = if (isRevisionMode) chapter.isRevised else chapter.isCompleted
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isChecked) {
            MaterialTheme.colorScheme.secondaryContainer // Theme-aware success background
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "background"
    )
    
    val contentAlpha by animateFloatAsState(
        targetValue = if (isChecked) 0.85f else 1f, // Less aggressive fade
        label = "alpha"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isChecked) 1.dp else 2.dp
        ),
        onClick = onToggle
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Chapter number badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = if (isChecked) {
                            MaterialTheme.colorScheme.primary // Theme-aware success color
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chapter.orderIndex.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isChecked) {
                        MaterialTheme.colorScheme.onPrimary // Better contrast
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
            
            // Chapter name with badges
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = chapter.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isChecked) FontWeight.Normal else FontWeight.Medium,
                    textDecoration = if (isChecked) TextDecoration.LineThrough else null,
                    modifier = Modifier.alpha(contentAlpha),
                    color = if (isChecked) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                
                // Badges row for important, revision, and notes
                // Hide "needs revision" badge in revision mode
                if (isImportant || (needsRevision && !isRevisionMode) || hasNoteText) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isImportant) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "⭐",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        // Only show "needs revision" badge in normal mode
                        if (needsRevision && !isRevisionMode) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "🔄",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        if (hasNoteText) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            ) {
                                Text(
                                    text = "📝",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Note icon button
            IconButton(
                onClick = { onNoteClick() },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (hasNote) {
                        Icons.Filled.StickyNote2
                    } else {
                        Icons.Outlined.StickyNote2
                    },
                    contentDescription = if (hasNote) "Edit note" else "Add note",
                    tint = if (hasNote) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Checkmark icon
            Icon(
                imageVector = if (isChecked) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.RadioButtonUnchecked
                },
                contentDescription = if (isChecked) "Completed" else "Not completed",
                tint = if (isChecked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
