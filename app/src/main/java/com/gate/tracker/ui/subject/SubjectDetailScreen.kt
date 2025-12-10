package com.gate.tracker.ui.subject

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.draw.clip
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.ui.components.ChapterItem
import com.gate.tracker.ui.components.CelebrationOverlay
import com.gate.tracker.ui.components.CelebrationBanner
import com.gate.tracker.ui.components.ChapterNoteBottomSheet
import com.gate.tracker.ui.subject.ChapterFilter
import com.gate.tracker.ui.resources.ResourcesContent
import com.gate.tracker.ui.resources.ResourcesViewModel
import com.gate.tracker.ResourcesViewModelFactory
import com.gate.tracker.data.repository.GateRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    subjectId: Int,
    viewModel: SubjectDetailViewModel,
    repository: GateRepository,
    onBackClick: () -> Unit
) {
    val subject by viewModel.subject.collectAsState()
    val chapters by viewModel.chapters.collectAsState()
    val chapterNotes by viewModel.chapterNotes.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    
    // Check if in revision mode
    val isRevisionMode by repository.isRevisionMode().collectAsState(initial = false)
    
    // Tab state
    var selectedTab by remember { mutableStateOf(0) }
    
    // Resources ViewModel
    val resourcesViewModel: ResourcesViewModel = viewModel(
        factory = ResourcesViewModelFactory(repository, subjectId)
    )
    
    // Celebration states
    var showCelebration by remember { mutableStateOf(false) }  // Subject completion
    var showChapterBanner by remember { mutableStateOf(false) }  // Chapter completion
    
    // Note bottom sheet state
    var selectedChapterForNote by remember { mutableStateOf<ChapterEntity?>(null) }
    var showNoteBottomSheet by remember { mutableStateOf(false) }
    
    // Track previous completed count
    val previousCompletedRef = remember { mutableStateOf<Int?>(null) }
    
    LaunchedEffect(subjectId) {
        viewModel.loadSubject(subjectId)
    }
    
    // Monitor subject completion changes
    LaunchedEffect(subject) {
        subject?.let { subj ->
            val currentCompleted = subj.completedChapters
            val totalChapters = subj.totalChapters
            val previousCompleted = previousCompletedRef.value
            
            android.util.Log.d("CELEBRATION", "Subject: ${subj.name}, Current: $currentCompleted, Total: $totalChapters, Previous: $previousCompleted")
            
            // Celebration triggers when:
            // - We have a previous count (not first load)
            // - Subject just hit 100% (current == total)
            // - It wasn't 100% before (previous != total)
            if (previousCompleted != null && 
                previousCompleted != totalChapters &&
                currentCompleted == totalChapters &&
                totalChapters > 0) {
                android.util.Log.d("CELEBRATION", "🎉 TRIGGERING CELEBRATION!")
                showCelebration = true
            }
            
            // Update previous count
            previousCompletedRef.value = currentCompleted
        }
    }
    
    // Calculate progress
    val targetProgress = subject?.let {
        if (it.totalChapters > 0) {
            val chaptersCount = if (isRevisionMode) it.revisedChapters else it.completedChapters
            chaptersCount.toFloat() / it.totalChapters.toFloat()
        } else 0f
    } ?: 0f
    
    // Animate progress for smooth transitions
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )
    
    
    // Match status bar color with app bar
    val view = LocalView.current
    val isDarkTheme = isSystemInDarkTheme()
    val appBarColor = if (isRevisionMode) {
        if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFFF3E8FF)
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val appBarTextColor = if (isRevisionMode) {
        if (isDarkTheme) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color(0xFF3B0764)
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
        
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
        window.statusBarColor = appBarColor.hashCode()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(appBarColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = appBarTextColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject?.name ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = appBarTextColor
                    )
                    subject?.let {
                        val chaptersCount = if (isRevisionMode) it.revisedChapters else it.completedChapters
                        Text(
                            text = "$chaptersCount / ${it.totalChapters} chapters",
                            style = MaterialTheme.typography.bodyMedium,
                            color = appBarTextColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        // Enhanced Tabs with icons and badges
        val resourceCount by resourcesViewModel.uiState.collectAsState()
        
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = if (isRevisionMode) {
                if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFF3B0764)
            } else {
                MaterialTheme.colorScheme.primary
            },
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(
                                color = if (isRevisionMode) {
                                    if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFF3B0764)
                                } else {
                                    MaterialTheme.colorScheme.primary
                                },
                                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                            )
                    )
                }
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { 
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Chapters")
                    }
                },
                selectedContentColor = if (isRevisionMode)
                    androidx.compose.ui.graphics.Color(0xFF9333EA)
                else
                    MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { 
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text("Resources")
                        if (!resourceCount.isEmpty && resourceCount.resources.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isRevisionMode) {
                                    if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA).copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color(0xFFF3E8FF)
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = resourceCount.resources.size.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isRevisionMode) {
                                            if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFF3B0764)
                                        } else {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                },
                selectedContentColor = if (isRevisionMode)
                    androidx.compose.ui.graphics.Color(0xFF9333EA)
                else
                    MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Tab Content
        when (selectedTab) {
            0 -> ChaptersTabContent(
                subject = subject,
                chapters = chapters,
                chapterNotes = chapterNotes,
                currentFilter = currentFilter,
                animatedProgress = animatedProgress,
                onFilterChange = { viewModel.setFilter(it) },
                onNoteClick = { chapter ->
                    selectedChapterForNote = chapter
                    showNoteBottomSheet = true
                },
                onToggleChapter = { chapter ->
                    // Show banner for individual chapter completion (not last chapter)
                    if (!chapter.isCompleted) {
                        val willComplete = subject?.let { it.completedChapters + 1 == it.totalChapters } ?: false
                        if (!willComplete) {
                            showChapterBanner = true
                        }
                    }
                    viewModel.toggleChapter(chapter)
                },
                isRevisionMode = isRevisionMode
            )
            1 -> ResourcesContent(
                viewModel = resourcesViewModel
            )
        }
    }
    
    // Full-screen celebration overlay for subject completion
    if (showCelebration) {
        CelebrationOverlay(
            onDismiss = { showCelebration = false },
            isRevisionMode = isRevisionMode
        )
    }
    
    // Chapter completion banner (for non-final chapters)
    if (showChapterBanner) {
        CelebrationBanner(
            message = if (isRevisionMode) "Chapter Revised!" else "Chapter Completed!",
            onDismiss = { showChapterBanner = false }
        )
    }
    
    // Chapter note bottom sheet
    if (showNoteBottomSheet && selectedChapterForNote != null) {
        val note = chapterNotes[selectedChapterForNote!!.id]
        ChapterNoteBottomSheet(
            chapterName = selectedChapterForNote!!.name,
            existingNote = note?.noteText,
            isImportant = note?.isImportant ?: false,
            needsRevision = note?.needsRevision ?: false,
            onDismiss = {
                showNoteBottomSheet = false
                selectedChapterForNote = null
            },
            onSave = { noteText, isImportant, needsRevision ->
                viewModel.saveNote(
                    chapterId = selectedChapterForNote!!.id,
                    noteText = noteText,
                    isImportant = isImportant,
                    needsRevision = needsRevision
                )
            },
            onDelete = if (note != null) {
                { 
                    viewModel.deleteNote(selectedChapterForNote!!.id)
                    showNoteBottomSheet = false
                    selectedChapterForNote = null
                }
            } else null
        )
    }
}

/**
 * Chapters tab content showing the list of chapters with filters
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChaptersTabContent(
    subject: com.gate.tracker.data.local.entity.SubjectEntity?,
    chapters: List<ChapterEntity>,
    chapterNotes: Map<Int, com.gate.tracker.data.local.entity.ChapterNoteEntity>,
    currentFilter: ChapterFilter,
    animatedProgress: Float,
    onFilterChange: (ChapterFilter) -> Unit,
    onNoteClick: (ChapterEntity) -> Unit,
    onToggleChapter: (ChapterEntity) -> Unit,
    isRevisionMode: Boolean = false
) {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Progress indicator card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isRevisionMode) {
                        if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA).copy(alpha = 0.15f) else androidx.compose.ui.graphics.Color(0xFFF3E8FF)
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isRevisionMode)
                                if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFF3B0764)
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    
                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = if (isRevisionMode) {
                            if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA) else androidx.compose.ui.graphics.Color(0xFF3B0764)
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        trackColor = if (isRevisionMode) {
                            if (isDarkTheme) androidx.compose.ui.graphics.Color(0xFF9333EA).copy(alpha = 0.2f) else androidx.compose.ui.graphics.Color(0xFF3B0764).copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        }
                    )
                }
            }
        }
        
        // Filter chips row 1 - Completion Status
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentFilter == ChapterFilter.ALL,
                    onClick = { onFilterChange(ChapterFilter.ALL) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = currentFilter == ChapterFilter.PENDING,
                    onClick = { onFilterChange(ChapterFilter.PENDING) },
                    label = { Text("○ Pending") }
                )
                FilterChip(
                    selected = currentFilter == ChapterFilter.COMPLETED,
                    onClick = { onFilterChange(ChapterFilter.COMPLETED) },
                    label = { Text("✓ Done") }
                )
            }
        }
        
        // Filter chips row 2 - Chapter Attributes
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = currentFilter == ChapterFilter.IMPORTANT,
                    onClick = { onFilterChange(ChapterFilter.IMPORTANT) },
                    label = { Text("⭐ Important") }
                )
                // Only show "Needs Revision" filter in normal mode
                if (!isRevisionMode) {
                    FilterChip(
                        selected = currentFilter == ChapterFilter.NEEDS_REVISION,
                        onClick = { onFilterChange(ChapterFilter.NEEDS_REVISION) },
                        label = { Text("🔄 Revision") }
                    )
                }
                FilterChip(
                    selected = currentFilter == ChapterFilter.WITH_NOTES,
                    onClick = { onFilterChange(ChapterFilter.WITH_NOTES) },
                    label = { Text("📝 Notes") }
                )
            }
        }
        
        // Filter chapters based on selected filter
        val filteredChapters = chapters.filter { chapter ->
            val note = chapterNotes[chapter.id]
            // Check appropriate completion field based on mode
            val isChecked = if (isRevisionMode) chapter.isRevised else chapter.isCompleted
            
            when (currentFilter) {
                ChapterFilter.ALL -> true
                ChapterFilter.IMPORTANT -> note?.isImportant == true
                ChapterFilter.NEEDS_REVISION -> note?.needsRevision == true
                ChapterFilter.WITH_NOTES -> note != null
                ChapterFilter.COMPLETED -> isChecked
                ChapterFilter.PENDING -> !isChecked
            }
        }
        
        // Chapters grouped by category
        val chaptersWithCategory = filteredChapters.filter { it.category != null }
        val chaptersWithoutCategory = filteredChapters.filter { it.category == null }
        val categories = chaptersWithCategory
            .groupBy { it.category }
            .keys
            .sortedBy { category ->
                chaptersWithCategory.indexOfFirst { it.category == category }
            }
        
        // Show categorized chapters
        if (categories.isNotEmpty()) {
            categories.forEach { category ->
                val categoryChapters = filteredChapters.filter { it.category == category }
                val completedInCategory = categoryChapters.count { it.isCompleted }
                
                // Category header
                item(key = "category_$category") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isRevisionMode)
                                androidx.compose.ui.graphics.Color(0xFF9333EA)
                            else
                                MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "$completedInCategory/${categoryChapters.size}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Chapters in category
                items(categoryChapters, key = { it.id }) { chapter ->
                    val note = chapterNotes[chapter.id]
                    ChapterItem(
                        chapter = chapter,
                        hasNote = note != null,
                        hasNoteText = note?.noteText?.isNotBlank() ?: false,
                        isImportant = note?.isImportant ?: false,
                        needsRevision = note?.needsRevision ?: false,
                        onNoteClick = { onNoteClick(chapter) },
                        onToggle = { onToggleChapter(chapter) },
                        isRevisionMode = isRevisionMode
                    )
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
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            
            items(chaptersWithoutCategory, key = { it.id }) { chapter ->
                val note = chapterNotes[chapter.id]
                ChapterItem(
                    chapter = chapter,
                    hasNote = note != null,
                    hasNoteText = note?.noteText?.isNotBlank() ?: false,
                    isImportant = note?.isImportant ?: false,
                    needsRevision = note?.needsRevision ?: false,
                    onNoteClick = { onNoteClick(chapter) },
                    onToggle = { onToggleChapter(chapter) },
                    isRevisionMode = isRevisionMode
                )
            }
        }
        
        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
