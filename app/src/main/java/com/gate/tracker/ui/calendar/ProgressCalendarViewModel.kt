package com.gate.tracker.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.util.DateUtils
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

data class MonthStats(
    val totalChapters: Int = 0,
    val totalDaysStudied: Int = 0,
    val averagePerDay: Float = 0f,
    val bestDay: Pair<String, Int>? = null, // Date and count
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val thisMonthTotal: Int = 0,
    val lastMonthTotal: Int = 0
)

class ProgressCalendarViewModel(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModel() {
    
    // Current month/year state
    private val _currentMonth = MutableStateFlow(Calendar.getInstance())
    val currentMonth: StateFlow<Calendar> = _currentMonth.asStateFlow()
    
    // Current week state for weekly pattern navigation
    private val _currentWeek = MutableStateFlow(Calendar.getInstance())
    val currentWeek: StateFlow<Calendar> = _currentWeek.asStateFlow()
    
    // Selected date for detail view
    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate: StateFlow<Long?> = _selectedDate.asStateFlow()
    
    // All completed chapters in current month
    private val completedChaptersInMonth: StateFlow<List<ChapterEntity>> =
        _currentMonth.flatMapLatest { calendar ->
            val startOfMonth = getStartOfMonth(calendar)
            val endOfMonth = getEndOfMonth(calendar)
            repository.getCompletedChaptersInRange(branchId, startOfMonth, endOfMonth)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Completion counts by day (day of month -> count)
    val completionCounts: StateFlow<Map<Int, Int>> = completedChaptersInMonth
        .map { chapters ->
            chapters
                .groupBy { DateUtils.getDayOfMonth(it.completedDate ?: 0) }
                .mapValues { it.value.size }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
    
    // Chapters for selected date
    val chaptersOnSelectedDate: StateFlow<List<ChapterEntity>> =
        _selectedDate.flatMapLatest { date ->
            if (date != null) {
                repository.getChaptersCompletedOnDate(branchId, date)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Monthly statistics
    val monthStats: StateFlow<MonthStats> = completedChaptersInMonth
        .combine(getAllCompletedChapters()) { currentMonth, allChapters ->
            calculateMonthStats(currentMonth, allChapters)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MonthStats()
        )
    
    // Total chapters count
    private val _totalChaptersCount = MutableStateFlow(0)
    
    // Yearly Activity (last 365 days) - dateKey -> count
    val yearlyActivity: StateFlow<Map<String, Int>> = getAllCompletedChapters()
        .map { chapters ->
            val today = System.currentTimeMillis()
            val oneYearAgo = today - (365L * 24 * 60 * 60 * 1000)
            
            chapters
                .filter { (it.completedDate ?: 0) >= oneYearAgo }
                .groupBy { DateUtils.getDateKey(it.completedDate ?: 0) }
                .mapValues { it.value.size }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
    
    // Weekly Pattern - ALL TIME (kept for reference, not used in UI anymore)
    val weeklyPattern: StateFlow<List<WeekdayActivity>> = getAllCompletedChapters()
        .map { chapters ->
            val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            
            // Group by day of week (1=Sunday, 7=Saturday)
            val weekdayCounts = chapters
                .mapNotNull { chapter ->
                    chapter.completedDate?.let { date ->
                        val calendar = Calendar.getInstance().apply { timeInMillis = date }
                        calendar.get(Calendar.DAY_OF_WEEK)
                    }
                }
                .groupingBy { it }
                .eachCount()
            
            val total = weekdayCounts.values.sum().coerceAtLeast(1)
            
            // Create activity list for all 7 days
            (1..7).map { dayOfWeek ->
                val count = weekdayCounts[dayOfWeek] ?: 0
                WeekdayActivity(
                    dayOfWeek = dayOfWeek,
                    dayName = dayNames[dayOfWeek - 1],
                    count = count,
                    percentage = (count.toFloat() / total) * 100f
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Weekly Pattern for SELECTED WEEK (with week navigation)
    val weeklyPatternForWeek: StateFlow<List<WeekdayActivity>> = _currentWeek
        .flatMapLatest { weekCalendar ->
            val startOfWeek = getStartOfWeek(weekCalendar)
            val endOfWeek = getEndOfWeek(weekCalendar)
            
            repository.getCompletedChaptersInRange(branchId, startOfWeek, endOfWeek)
                .map { chapters ->
                    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                    
                    // Group by day of week (1=Sunday, 7=Saturday)
                    val weekdayCounts = chapters
                        .mapNotNull { chapter ->
                            chapter.completedDate?.let { date ->
                                val calendar = Calendar.getInstance().apply { timeInMillis = date }
                                calendar.get(Calendar.DAY_OF_WEEK)
                            }
                        }
                        .groupingBy { it }
                        .eachCount()
                    
                    val total = weekdayCounts.values.sum().coerceAtLeast(1)
                    
                    // Create activity list for all 7 days
                    (1..7).map { dayOfWeek ->
                        val count = weekdayCounts[dayOfWeek] ?: 0
                        WeekdayActivity(
                            dayOfWeek = dayOfWeek,
                            dayName = dayNames[dayOfWeek - 1],
                            count = count,
                            percentage = (count.toFloat() / total) * 100f
                        )
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Subject Distribution (current month)
    val subjectDistribution: StateFlow<List<SubjectProgress>> = completedChaptersInMonth
        .combine(repository.getSubjectsByBranch(branchId)) { chapters, subjects ->
            // Group chapters by subject
            val subjectCounts = chapters
                .groupBy { it.subjectId }
                .mapValues { it.value.size }
            
            val total = subjectCounts.values.sum().coerceAtLeast(1)
            
            // Predefined colors for subjects
            val colors = listOf(
                androidx.compose.ui.graphics.Color(0xFF3B82F6), // Blue
                androidx.compose.ui.graphics.Color(0xFF10B981), // Green
                androidx.compose.ui.graphics.Color(0xFFF59E0B), // Amber
                androidx.compose.ui.graphics.Color(0xFFEF4444), // Red
                androidx.compose.ui.graphics.Color(0xFF8B5CF6), // Purple
                androidx.compose.ui.graphics.Color(0xFF06B6D4), // Cyan
                androidx.compose.ui.graphics.Color(0xFFEC4899), // Pink
                androidx.compose.ui.graphics.Color(0xFF14B8A6), // Teal
            )
            
            subjects
                .filter { subjectCounts.containsKey(it.id) }
                .sortedByDescending { subjectCounts[it.id] ?: 0 }
                .mapIndexed { index, subject ->
                    val count = subjectCounts[subject.id] ?: 0
                    SubjectProgress(
                        subjectId = subject.id,
                        subjectName = subject.name,
                        completedCount = count,
                        percentage = (count.toFloat() / total) * 100f,
                        color = colors[index % colors.size]
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Initialize
    init {
        viewModelScope.launch {
            _totalChaptersCount.value = repository.getTotalChapterCount(branchId)
        }
    }

    // Exam Date
    val examDate: StateFlow<Long?> = repository.getExamDate(branchId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Daily completions for current MONTH (for month-aware progress trend)
    val dailyCompletionsForMonth: StateFlow<Map<String, Int>> = completedChaptersInMonth
        .combine(_currentMonth) { chapters, calendar ->
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            
            // Initialize all days of month with 0
            val dailyMap = mutableMapOf<String, Int>()
            for (day in 1..daysInMonth) {
                val cal = calendar.clone() as Calendar
                cal.set(Calendar.DAY_OF_MONTH, day)
                val dateKey = DateUtils.getDateKey(cal.timeInMillis)
                dailyMap[dateKey] = 0
            }
            
            // Fill in actual completion counts for the month
            chapters
                .groupBy { DateUtils.getDateKey(it.completedDate ?: 0) }
                .forEach { (dateKey, chapterList) ->
                    dailyMap[dateKey] = chapterList.size
                }
            
            dailyMap.toSortedMap()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
    
    // Daily completions for last 30 days (for velocity calculation in Exam Readiness)
    val dailyCompletionsLast30Days: StateFlow<Map<String, Int>> = getAllCompletedChapters()
        .map { chapters ->
            val today = System.currentTimeMillis()
            // IMPORTANT: Use Long for multiplication to avoid integer overflow
            val thirtyDaysAgo = today - (30L * 24 * 60 * 60 * 1000)
            
            // Initialize all 30 days with 0
            val dailyMap = mutableMapOf<String, Int>()
            for (i in 29 downTo 0) {
                val date = today - (i.toLong() * 24 * 60 * 60 * 1000)
                val dateKey = DateUtils.getDateKey(date)
                dailyMap[dateKey] = 0
            }
            
            // Fill in actual completion counts
            chapters
                .filter { (it.completedDate ?: 0) >= thirtyDaysAgo }
                .groupBy { DateUtils.getDateKey(it.completedDate ?: 0) }
                .forEach { (dateKey, chapterList) ->
                    dailyMap[dateKey] = chapterList.size
                }
            
            dailyMap.toSortedMap()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )
        
    data class ExamReadiness(
        val velocity: Float,
        val projectedFinishDate: Long?,
        val examDate: Long
    )
    
    // Exam Readiness Projection
    val examReadiness: StateFlow<ExamReadiness?> = combine(
        dailyCompletionsLast30Days,
        getAllCompletedChapters(), // For total completed count
        _totalChaptersCount,
        examDate
    ) { dailyMap, allCompleted, totalChapters, examDateValue ->
        if (examDateValue == null || totalChapters == 0) return@combine null
        
        // Calculate velocity (chapters per day over last 30 days)
        val chaptersInLast30Days = dailyMap.values.sum()
        // We use 30 days as the divisor to encourage consistency. 
        // If we used "days active", it might overestimate speed for inconsistent users.
        val velocity = if (chaptersInLast30Days > 0) chaptersInLast30Days / 30f else 0f
        
        val totalCompletedCount = allCompleted.size
        val remaining = (totalChapters - totalCompletedCount).coerceAtLeast(0)
        
        val projectedDate = if (velocity > 0 && remaining > 0) {
            val daysNeeded = (remaining / velocity).toLong()
            System.currentTimeMillis() + (daysNeeded * 24 * 60 * 60 * 1000)
        } else if (remaining == 0) {
            System.currentTimeMillis() // Finished!
        } else {
            null // Infinite time
        }
        
        ExamReadiness(
            velocity = velocity,
            projectedFinishDate = projectedDate,
            examDate = examDateValue
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    
    private fun getAllCompletedChapters(): Flow<List<ChapterEntity>> {
        return repository.getAllCompletedChaptersWithDates(branchId)
    }
    
    fun selectDate(date: Long) {
        _selectedDate.value = date
    }
    
    fun clearSelectedDate() {
        _selectedDate.value = null
    }
    
    fun navigateToNextMonth() {
        val calendar = _currentMonth.value.clone() as Calendar
        calendar.add(Calendar.MONTH, 1)
        _currentMonth.value = calendar
        clearSelectedDate()
    }
    
    fun navigateToPreviousMonth() {
        val calendar = _currentMonth.value.clone() as Calendar
        calendar.add(Calendar.MONTH, -1)
        _currentMonth.value = calendar
        clearSelectedDate()
    }
    
    fun navigateToToday() {
        _currentMonth.value = Calendar.getInstance()
        clearSelectedDate()
    }
    
    fun navigateToNextWeek() {
        val calendar = _currentWeek.value.clone() as Calendar
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        _currentWeek.value = calendar
    }
    
    fun navigateToPreviousWeek() {
        val calendar = _currentWeek.value.clone() as Calendar
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        _currentWeek.value = calendar
    }
    
    fun navigateToCurrentWeek() {
        _currentWeek.value = Calendar.getInstance()
    }
    
    private fun getStartOfMonth(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
    
    private fun getEndOfMonth(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
    
    private fun getStartOfWeek(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
    
    private fun getEndOfWeek(calendar: Calendar): Long {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
    
    private fun calculateMonthStats(
        currentMonthChapters: List<ChapterEntity>,
        allChapters: List<ChapterEntity>
    ): MonthStats {
        if (allChapters.isEmpty()) return MonthStats()
        
        // Group all chapters by date
        val chaptersByDate = allChapters
            .filter { it.completedDate != null }
            .groupBy { DateUtils.getDateKey(it.completedDate!!) }
            .mapValues { it.value.size }
        
        // Calculate total chapters and days studied
        val totalChapters = allChapters.size
        val totalDaysStudied = chaptersByDate.size
        val averagePerDay = if (totalDaysStudied > 0) totalChapters.toFloat() / totalDaysStudied else 0f
        
        // Find best day
        val bestDay = chaptersByDate.maxByOrNull { it.value }?.let {
            DateUtils.formatDate(it.key.toLongOrNull() ?: 0, "dd MMM") to it.value
        }
        
        // Calculate streaks
        val (currentStreak, longestStreak) = calculateStreaks(chaptersByDate.keys.toList())
        
        // This month vs last month
        val thisMonthTotal = currentMonthChapters.size
        val lastMonthTotal = calculateLastMonthTotal(allChapters, _currentMonth.value)
        
        return MonthStats(
            totalChapters = totalChapters,
            totalDaysStudied = totalDaysStudied,
            averagePerDay = averagePerDay,
            bestDay = bestDay,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            thisMonthTotal = thisMonthTotal,
            lastMonthTotal = lastMonthTotal
        )
    }
    
    private fun calculateStreaks(dateKeys: List<String>): Pair<Int, Int> {
        if (dateKeys.isEmpty()) return 0 to 0
        
        val sortedDates = dateKeys.sorted().reversed() // Most recent first
        var currentStreak = 0
        var longestStreak = 0
        var tempStreak = 1
        
        val today = DateUtils.getDateKey(System.currentTimeMillis())
        val yesterday = DateUtils.getDateKey(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        
        // Check if current streak is active
        if (sortedDates.first() == today || sortedDates.first() == yesterday) {
            currentStreak = 1
            
            for (i in 1 until sortedDates.size) {
                val prevDate = sortedDates[i - 1].toLong()
                val currDate = sortedDates[i].toLong()
                
                // Check if dates are consecutive
                if (isConsecutiveDays(currDate, prevDate)) {
                    currentStreak++
                    tempStreak++
                } else {
                    longestStreak = maxOf(longestStreak, tempStreak)
                    tempStreak = 1
                }
            }
        } else {
            // Calculate longest streak from history
            for (i in 1 until sortedDates.size) {
                val prevDate = sortedDates[i - 1].toLong()
                val currDate = sortedDates[i].toLong()
                
                if (isConsecutiveDays(currDate, prevDate)) {
                    tempStreak++
                } else {
                    longestStreak = maxOf(longestStreak, tempStreak)
                    tempStreak = 1
                }
            }
        }
        
        longestStreak = maxOf(longestStreak, tempStreak, currentStreak)
        
        return currentStreak to longestStreak
    }
    
    private fun isConsecutiveDays(date1: Long, date2: Long): Boolean {
        val diff = kotlin.math.abs(date2 - date1)
        return diff == 1L // Dates in yyyyMMdd format differ by 1
    }
    
    private fun calculateLastMonthTotal(
        allChapters: List<ChapterEntity>,
        currentCalendar: Calendar
    ): Int {
        val lastMonthCal = currentCalendar.clone() as Calendar
        lastMonthCal.add(Calendar.MONTH, -1)
        
        val startOfLastMonth = getStartOfMonth(lastMonthCal)
        val endOfLastMonth = getEndOfMonth(lastMonthCal)
        
        return allChapters.count { chapter ->
            val date = chapter.completedDate ?: 0
            date in startOfLastMonth..endOfLastMonth
        }
    }
}
