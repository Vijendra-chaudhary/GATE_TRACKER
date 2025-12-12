package com.gate.tracker.ui.components

import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gate.tracker.data.local.entity.TodoWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    todos: List<TodoWithDetails>,
    pendingCount: Int,
    onToggleTodo: (Int, Boolean) -> Unit,
    onAddClick: () -> Unit,
    onShowAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = todos.count { it.todo.isCompleted }
    val totalCount = todos.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Compact Header
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChecklistRtl,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "My To-Do",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                if (totalCount > 0) {
                                    Text(
                                        text = "$pendingCount of 3 slots",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                        
                        // '+' button to add chapters
                        IconButton(
                            onClick = onAddClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Chapter",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    // Progress bar (compact)
                    if (totalCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
            }
            
            // Todo items or empty state
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (todos.isEmpty()) {
                    // Compact empty state
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChecklistRtl,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No items yet",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Add up to 3 chapters",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                } else {
                    todos.take(3).forEach { todoWithDetails ->
                        TodoItem(
                            todoWithDetails = todoWithDetails,
                            onToggle = { onToggleTodo(todoWithDetails.todo.id, !todoWithDetails.todo.isCompleted) }
                        )
                    }
                }
                
            }
        }
    }
}

@Composable
private fun TodoItem(
    todoWithDetails: TodoWithDetails,
    onToggle: () -> Unit
) {
    val isCompleted = todoWithDetails.todo.isCompleted
    val haptic = LocalHapticFeedback.current
    
    // Confetti state
    var showConfetti by remember { mutableStateOf(false) }
    
    // Trigger confetti when completed
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            showConfetti = true
        }
    }
    
    // Animate alpha when completed
    val alpha by animateFloatAsState(
        targetValue = if (isCompleted) 0.5f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )
    
    // Animate slide when completed
    val offsetX by animateDpAsState(
        targetValue = if (isCompleted) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 350),
        label = "offsetX"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX)
            .alpha(alpha)
            .clickable(onClick = onToggle)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { 
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggle() 
                },
                modifier = Modifier.size(20.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            
            if (showConfetti) {
                TodoItemExplosion(
                    onFinished = { showConfetti = false }
                )
            }
        }
        
        Text(
            text = "${todoWithDetails.chapter.name} (${getSubjectShortForm(todoWithDetails.subject.name)})",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textDecoration = if (isCompleted) 
                TextDecoration.LineThrough 
            else 
                null,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun getSubjectShortForm(subjectName: String): String {
    return when {
        subjectName.contains("Programming", ignoreCase = true) -> "DS"
        subjectName.contains("Algorithm", ignoreCase = true) -> "Algo"
        subjectName.contains("Operating System", ignoreCase = true) -> "OS"
        subjectName.contains("Database", ignoreCase = true) -> "DBMS"
        subjectName.contains("Computer Network", ignoreCase = true) -> "CN"
        subjectName.contains("Compiler", ignoreCase = true) -> "CD"
        subjectName.contains("Theory of Computation", ignoreCase = true) -> "TOC"
        subjectName.contains("Digital Logic", ignoreCase = true) -> "DL"
        subjectName.contains("Computer Organization", ignoreCase = true) -> "COA"
        subjectName.contains("Discrete", ignoreCase = true) -> "DM"
        subjectName.contains("Linear Algebra", ignoreCase = true) -> "LA"
        subjectName.contains("Probability", ignoreCase = true) -> "Prob"
        subjectName.contains("Engineering Math", ignoreCase = true) -> "EM"
        else -> subjectName.take(3).uppercase() // Default: first 3 letters
    }
}
