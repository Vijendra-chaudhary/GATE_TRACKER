package com.gate.tracker.util

/**
 * USAGE EXAMPLES FOR CHAPTER CATEGORIES
 * 
 * This file contains examples of how to display chapters grouped by category.
 * Engineering Mathematics is now organized into 4 categories:
 * 1. Discrete Mathematics (7 chapters)
 * 2. Linear Algebra (4 chapters)
 * 3. Calculus (4 chapters)
 * 4. Probability & Statistics (9 chapters)
 */

/*
====================================================================================
EXAMPLE 1: Getting Categories for a Subject
====================================================================================

In your ViewModel:

```kotlin
class SubjectDetailViewModel(
    private val repository: GateRepository,
    private val subjectId: Int
) : ViewModel() {
    
    val categories: StateFlow<List<String>> = flow {
        emit(repository.getCategoriesForSubject(subjectId))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
```

====================================================================================
EXAMPLE 2: Displaying Chapters Grouped by Category
====================================================================================

In your UI (Compose):

```kotlin
@Composable
fun ChapterListScreen(viewModel: SubjectDetailViewModel) {
    val categories by viewModel.categories.collectAsState()
    val allChapters by viewModel.chapters.collectAsState()
    
    LazyColumn {
        categories.forEach { category ->
            // Category Header
            item {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 8.dp)
                )
            }
            
            // Chapters in this category
            items(allChapters.filter { it.category == category }) { chapter ->
                ChapterItem(
                    chapter = chapter,
                    onToggle = { viewModel.toggleChapter(chapter.id, chapter.isCompleted) }
                )
            }
        }
    }
}
```

====================================================================================
EXAMPLE 3: Alternative - Dedicated Flow per Category
====================================================================================

For better performance with large chapter lists:

```kotlin
class SubjectDetailViewModel(
    private val repository: GateRepository,
    private val subjectId: Int
) : ViewModel() {
    
    // Get all categories first
    val categories = flow {
        emit(repository.getCategoriesForSubject(subjectId))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Create a map of category -> chapters flow
    fun getChaptersForCategory(category: String): Flow<List<ChapterEntity>> {
        return repository.getChaptersByCategory(subjectId, category)
    }
}

// In UI:
@Composable
fun CategorySection(category: String, viewModel: SubjectDetailViewModel) {
    val chapters by viewModel.getChaptersForCategory(category).collectAsState(initial = emptyList())
    
    Column {
        Text(category, style = MaterialTheme.typography.titleMedium)
        chapters.forEach { chapter ->
            ChapterItem(chapter)
        }
    }
}
```

====================================================================================
EXAMPLE 4: Progress by Category
====================================================================================

Track completion progress per category:

```kotlin
data class CategoryProgress(
    val category: String,
    val total: Int,
    val completed: Int,
    val percentage: Int
)

fun calculateCategoryProgress(chapters: List<ChapterEntity>): List<CategoryProgress> {
    return chapters
        .filter { it.category != null }
        .groupBy { it.category!! }
        .map { (category, chapterList) ->
            val total = chapterList.size
            val completed = chapterList.count { it.isCompleted }
            CategoryProgress(
                category = category,
                total = total,
                completed = completed,
                percentage = (completed * 100) / total
            )
        }
        .sortedBy { it.category }
}

// Display progress cards:
@Composable
fun CategoryProgressCards(progress: List<CategoryProgress>) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        progress.forEach { categoryProgress ->
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(categoryProgress.category)
                    LinearProgressIndicator(
                        progress = categoryProgress.percentage / 100f
                    )
                    Text("${categoryProgress.completed}/${categoryProgress.total}")
                }
            }
        }
    }
}
```

====================================================================================
EXAMPLE 5: Expandable Category Sections
====================================================================================

Show categories as expandable/collapsible sections:

```kotlin
@Composable
fun ExpandableCategoryList(
    categories: List<String>,
    allChapters: List<ChapterEntity>
) {
    var expandedCategory by remember { mutableStateOf<String?>(null) }
    
    LazyColumn {
        categories.forEach { category ->
            // Category Header (clickable)
            item {
                val chaptersInCategory = allChapters.filter { it.category == category }
                val completed = chaptersInCategory.count { it.isCompleted }
                val total = chaptersInCategory.size
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            expandedCategory = if (expandedCategory == category) null else category 
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(category, fontWeight = FontWeight.Bold)
                    Text("$completed/$total")
                    Icon(
                        imageVector = if (expandedCategory == category) 
                            Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }
            
            // Expanded chapters
            if (expandedCategory == category) {
                items(allChapters.filter { it.category == category }) { chapter ->
                    ChapterItem(chapter)
                }
            }
        }
    }
}
```

====================================================================================
EXAMPLE 6: Category-Based Filtering
====================================================================================

Add a filter to show only specific categories:

```kotlin
@Composable
fun ChapterListWithFilter(viewModel: SubjectDetailViewModel) {
    val categories by viewModel.categories.collectAsState()
    val allChapters by viewModel.chapters.collectAsState()
    var selectedCategories by remember { mutableStateOf(categories.toSet()) }
    
    Column {
        // Category filter chips
        FlowRow(modifier = Modifier.padding(16.dp)) {
            categories.forEach { category ->
                FilterChip(
                    selected = category in selectedCategories,
                    onClick = {
                        selectedCategories = if (category in selectedCategories) {
                            selectedCategories - category
                        } else {
                            selectedCategories + category
                        }
                    },
                    label = { Text(category) }
                )
            }
        }
        
        // Filtered chapter list
        LazyColumn {
            val filteredChapters = allChapters.filter { 
                it.category in selectedCategories 
            }
            items(filteredChapters) { chapter ->
                ChapterItem(chapter)
            }
        }
    }
}
```

====================================================================================
CATEGORY BREAKDOWN
====================================================================================

Engineering Mathematics Categories:

1. **Discrete Mathematics** (7 chapters)
   - Propositional and First-Order Logic
   - Sets, Relations, Functions
   - Partial Orders and Lattices
   - Monoids, Groups
   - Graphs: Connectivity, Matching, Coloring
   - Combinatorics: Counting, Recurrence Relations
   - Generating Functions

2. **Linear Algebra** (4 chapters)
   - Linear Algebra: Matrices, Determinants
   - System of Linear Equations
   - Eigenvalues and Eigenvectors
   - LU Decomposition

3. **Calculus** (4 chapters)
   - Calculus: Limits, Continuity, Differentiability
   - Maxima and Minima
   - Mean Value Theorem
   - Integration

4. **Probability & Statistics** (9 chapters)
   - Probability: Sample Space, Events
   - Independent Events, Bayes Theorem
   - Random Variables
   - Uniform, Normal, Exponential Distributions
   - Poisson Distributions
   - Mean, Median, Mode, Standard Deviation
   - Conditional Probability and Expectation
   - Central Limit Theorem
   - Hypothesis Testing

====================================================================================
KEY POINTS
====================================================================================

1. Categories are stored as nullable strings in ChapterEntity
2. Only Engineering Mathematics chapters have categories (for CS branch)
3. Other subjects have category = null
4. Use `getCategoriesForSubject()` to get list of unique categories
5. Use `getChaptersByCategory()` to filter chapters by category
6. Categories preserve order by using orderIndex in the query
7. Categories can be used for:
   - Grouped display
   - Progress tracking per category
   - Filtering
   - Last studied category tracking

*/
