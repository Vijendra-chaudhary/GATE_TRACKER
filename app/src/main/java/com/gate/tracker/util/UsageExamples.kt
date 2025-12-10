package com.gate.tracker.util

/**
 * USAGE EXAMPLES FOR CHAPTER COMPLETION DATE TRACKING
 * 
 * This file contains examples of how to use the new chapter completion date tracking feature.
 * You can integrate these examples into your ViewModels or UI components.
 */

/*
====================================================================================
EXAMPLE 1: Displaying completion date in Chapter List
====================================================================================

In your Chapter list item layout or composable, you can display when a chapter was completed:

```kotlin
// In your adapter or composable
Text(
    text = if (chapter.isCompleted) {
        "Completed: ${DateUtils.formatDate(chapter.completedDate)}"
        // Output: "Completed: 07 Dec 2025"
        
        // OR use relative time:
        // "Completed ${DateUtils.getRelativeTime(chapter.completedDate)}"
        // Output: "Completed 2 hours ago"
    } else {
        "Not completed"
    }
)
```

====================================================================================
EXAMPLE 2: Getting Last Studied Subject in ViewModel
====================================================================================

In your HomeViewModel or DashboardViewModel:

```kotlin
class HomeViewModel(private val repository: GateRepository) : ViewModel() {
    
    val lastStudiedSubject: StateFlow<SubjectEntity?> = flow {
        emit(repository.getLastStudiedSubject())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    val lastCompletedChapter: StateFlow<ChapterEntity?> = flow {
        emit(repository.getLastCompletedChapter())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}

// In your UI:
lastStudiedSubject.value?.let { subject ->
    Text("Last studied: ${subject.name}")
}

lastCompletedChapter.value?.let { chapter ->
    Text(
        "Last activity: ${chapter.name} - ${DateUtils.getRelativeTime(chapter.completedDate)}"
        // Output: "Last activity: Binary Search Trees - 3 hours ago"
    )
}
```

====================================================================================
EXAMPLE 3: Showing Completion History
====================================================================================

Display all completed chapters sorted by date:

```kotlin
class HistoryViewModel(private val repository: GateRepository) : ViewModel() {
    
    val completedChapters: StateFlow<List<ChapterEntity>> = 
        repository.getAllCompletedChaptersWithDates()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}

// In your UI - show history:
LazyColumn {
    items(completedChapters) { chapter ->
        Row {
            Text(chapter.name)
            Spacer(Modifier.weight(1f))
            Text(
                text = DateUtils.formatDate(chapter.completedDate),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
```

====================================================================================
EXAMPLE 4: Progress Tracking by Date Range
====================================================================================

Track how many chapters were completed this week/month:

```kotlin
fun getChaptersCompletedThisWeek(allChapters: List<ChapterEntity>): Int {
    val weekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
    return allChapters.count { chapter ->
        chapter.completedDate?.let { it >= weekAgo } ?: false
    }
}

// Display in UI:
val weeklyCount = getChaptersCompletedThisWeek(completedChapters.value)
Text("Completed this week: $weeklyCount chapters")
```

====================================================================================
EXAMPLE 5: Subject with Last Completion Date
====================================================================================

For each subject, show when it was last studied:

```kotlin
// In SubjectDao.kt, you can add this query:
@Query("""
    SELECT subjects.*, MAX(chapters.completedDate) as lastCompletedDate
    FROM subjects
    LEFT JOIN chapters ON subjects.id = chapters.subjectId
    WHERE chapters.isCompleted = 1
    GROUP BY subjects.id
    ORDER BY lastCompletedDate DESC NULLS LAST
""")
fun getSubjectsWithLastCompletedDate(): Flow<List<SubjectWithDate>>

// Then use in UI to show "Continue studying" which subject
```

====================================================================================
KEY POINTS
====================================================================================

1. completedDate is stored as Long? (milliseconds since epoch)
2. It's null when chapter is not completed
3. When toggling chapter to completed, current timestamp is automatically saved
4. When toggling back to incomplete, date is cleared (set to null)
5. Use DateUtils.formatDate() to format for display
6. Use DateUtils.getRelativeTime() for "2 hours ago" style
7. repository.getLastStudiedSubject() returns subject with most recent chapter completion

*/
