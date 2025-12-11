package com.gate.tracker.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.local.entity.TodoWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDialog(
    todos: List<TodoWithDetails>,
    subjects: List<SubjectEntity>,
    chapters: Map<Int, List<ChapterEntity>>,
    recommendations: List<com.gate.tracker.ui.dashboard.DashboardViewModel.Recommendation> = emptyList(),
    existingChapterIds: Set<Int>,
    onAddChapter: (Int) -> Unit,
    onToggleTodo: (Int, Boolean) -> Unit,
    onDeleteTodo: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var showAddMode by remember { mutableStateOf(false) }
    var selectedSubjectId by remember { mutableStateOf<Int?>(null) }
    
    val pendingCount = todos.count { !it.todo.isCompleted }
    val slotsRemaining = 3 - pendingCount
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Compact Top bar
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showAddMode && selectedSubjectId != null) {
                            IconButton(onClick = { selectedSubjectId = null }) {
                                Icon(Icons.Default.ArrowBack, "Back")
                            }
                        } else if (showAddMode) {
                            IconButton(onClick = { showAddMode = false }) {
                                Icon(Icons.Default.ArrowBack, "Back")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = when {
                                    selectedSubjectId != null -> "Select Chapter"
                                    showAddMode -> "Select Subject"
                                    else -> "My To-Do"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (!showAddMode && selectedSubjectId == null) {
                                Text(
                                    text = "$pendingCount of 3 slots used",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        if (!showAddMode) {
                            IconButton(onClick = { showAddMode = true }) {
                                Icon(Icons.Default.Add, "Add chapter")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(48.dp))
                        }
                    }
                }
                
                // Content
                when {
                    selectedSubjectId != null -> {
                        // Chapter selection
                        ChapterSelectionContent(
                            chapters = chapters[selectedSubjectId!!] ?: emptyList(),
                            existingChapterIds = existingChapterIds,
                            onChapterSelected = { chapterId ->
                                onAddChapter(chapterId)
                                selectedSubjectId = null
                                showAddMode = false
                            }
                        )
                    }
                    showAddMode -> {
                        // Subject selection
                        SubjectSelectionContent(
                            subjects = subjects,
                            onSubjectSelected = { subjectId ->
                                selectedSubjectId = subjectId
                            }
                        )
                    }
                    else -> {
                        // Todo list view
                        TodoListContent(
                            todos = todos,
                            recommendations = recommendations,
                            onToggleTodo = onToggleTodo,
                            onDeleteTodo = onDeleteTodo,
                            onRecommendationSelected = { rec -> onAddChapter(rec.chapter.id) }
                        )
                    }
                }
                
                // Close button
                if (!showAddMode) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectSelectionContent(
    subjects: List<SubjectEntity>,
    onSubjectSelected: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(subjects) { subject ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onSubjectSelected(subject.id) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = subject.name,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChapterSelectionContent(
    chapters: List<ChapterEntity>,
    existingChapterIds: Set<Int>,
    onChapterSelected: (Int) -> Unit
) {
    // Group chapters by category
    val chaptersWithCategory = chapters.filter { it.category != null }
    val chaptersWithoutCategory = chapters.filter { it.category == null }
    val categories = chaptersWithCategory
        .groupBy { it.category }
        .keys
        .sortedBy { category ->
            chaptersWithCategory.indexOfFirst { it.category == category }
        }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Show categorized chapters
        if (categories.isNotEmpty()) {
            categories.forEach { category ->
                val categoryChapters = chapters.filter { it.category == category }
                val addedInCategory = categoryChapters.count { existingChapterIds.contains(it.id) }
                
                // Category header
                item(key = "category_$category") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$addedInCategory/${categoryChapters.size} added",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Chapters in category
                categoryChapters.forEach { chapter ->
                    item(key = "chapter_${chapter.id}") {
                        ChapterSelectionCard(
                            chapter = chapter,
                            isAlreadyAdded = existingChapterIds.contains(chapter.id),
                            onChapterSelected = onChapterSelected
                        )
                    }
                }
            }
        }
        
        // Show uncategorized chapters (if any)
        if (chaptersWithoutCategory.isNotEmpty()) {
            item(key = "uncategorized_header") {
                Text(
                    text = if (categories.isEmpty()) "Chapters" else "Other Chapters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            chaptersWithoutCategory.forEach { chapter ->
                item(key = "chapter_${chapter.id}") {
                    ChapterSelectionCard(
                        chapter = chapter,
                        isAlreadyAdded = existingChapterIds.contains(chapter.id),
                        onChapterSelected = onChapterSelected
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChapterSelectionCard(
    chapter: ChapterEntity,
    isAlreadyAdded: Boolean,
    onChapterSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (!isAlreadyAdded) onChapterSelected(chapter.id) },
        colors = CardDefaults.cardColors(
            containerColor = if (isAlreadyAdded)
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Chapter number badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "${chapter.orderIndex}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Text(
                    text = chapter.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isAlreadyAdded)
                        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f)
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            if (isAlreadyAdded) {
                Text(
                    text = "Added",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListContent(
    todos: List<TodoWithDetails>,
    recommendations: List<com.gate.tracker.ui.dashboard.DashboardViewModel.Recommendation> = emptyList(),
    onToggleTodo: (Int, Boolean) -> Unit,
    onDeleteTodo: (Int) -> Unit,
    onRecommendationSelected: (com.gate.tracker.ui.dashboard.DashboardViewModel.Recommendation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (todos.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chapters in your to-do list",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            items(todos) { todoWithDetails ->
                DialogTodoItem(
                    todoWithDetails = todoWithDetails,
                    onToggleTodo = onToggleTodo,
                    onDeleteTodo = onDeleteTodo
                )
            }
        }

        // Suggestions Section
        if (recommendations.isNotEmpty()) {
            if (todos.isNotEmpty()) {
                item {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
            
            item {
                Text(
                    text = "Suggestions",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }
            
            items(recommendations) { rec ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onRecommendationSelected(rec) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = rec.chapter.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Next in ${rec.subject.name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogTodoItem(
    todoWithDetails: TodoWithDetails,
    onToggleTodo: (Int, Boolean) -> Unit,
    onDeleteTodo: (Int) -> Unit
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
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetX"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX)
            .alpha(alpha),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { 
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onToggleTodo(todoWithDetails.todo.id, !isCompleted)
                        }
                    )
                    
                    if (showConfetti) {
                        TodoItemExplosion(
                            onFinished = { showConfetti = false }
                        )
                    }
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = todoWithDetails.chapter.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (isCompleted) 
                            TextDecoration.LineThrough 
                        else 
                            null
                    )
                    Text(
                        text = todoWithDetails.subject.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
                
                IconButton(onClick = { onDeleteTodo(todoWithDetails.todo.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }



