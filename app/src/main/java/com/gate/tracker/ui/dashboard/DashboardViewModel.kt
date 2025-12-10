package com.gate.tracker.ui.dashboard

import android.app.Application
import android.util.Log

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.BranchEntity
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.GoalEntity
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.model.StreakBadge
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DashboardViewModel(
    application: Application,
    private val repository: GateRepository
) : AndroidViewModel(application) {
    
    private val _selectedBranch = MutableStateFlow<BranchEntity?>(null)
    val selectedBranch: StateFlow<BranchEntity?> = _selectedBranch.asStateFlow()
    
    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()
    
    private val _totalCompleted = MutableStateFlow(0)
    val totalCompleted: StateFlow<Int> = _totalCompleted.asStateFlow()
    
    private val _totalChapters = MutableStateFlow(0)
    val totalChapters: StateFlow<Int> = _totalChapters.asStateFlow()
    
    private val _progressPercentage = MutableStateFlow(0)
    val progressPercentage: StateFlow<Int> = _progressPercentage.asStateFlow()
    
    private val _completedSubjects = MutableStateFlow(0)
    val completedSubjects: StateFlow<Int> = _completedSubjects.asStateFlow()
    
    private val _inProgressSubjects = MutableStateFlow(0)
    val inProgressSubjects: StateFlow<Int> = _inProgressSubjects.asStateFlow()
    
    private val _notStartedSubjects = MutableStateFlow(0)
    val notStartedSubjects: StateFlow<Int> = _notStartedSubjects.asStateFlow()
    
    private val _daysRemaining = MutableStateFlow(0)
    val daysRemaining: StateFlow<Int> = _daysRemaining.asStateFlow()
    
    private val _motivationalMessage = MutableStateFlow("")
    val motivationalMessage: StateFlow<String> = _motivationalMessage.asStateFlow()
    
    data class ContinueStudyingData(
        val subject: SubjectEntity,
        val nextChapter: ChapterEntity
    )
    
    private val _continueStudying = MutableStateFlow<ContinueStudyingData?>(null)
    val continueStudying: StateFlow<ContinueStudyingData?> = _continueStudying.asStateFlow()
    
    // Goal tracking
    val currentGoal: StateFlow<GoalEntity?> = repository.getCurrentActiveGoal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    // Revision Mode tracking
    val isRevisionMode: StateFlow<Boolean> = repository.isRevisionMode()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    private val _totalRevised = MutableStateFlow(0)
    val totalRevised: StateFlow<Int> = _totalRevised.asStateFlow()
    
    fun createWeeklyGoal(targetChapters: Int) {
        viewModelScope.launch {
            val startDate = System.currentTimeMillis()
            val endDate = Calendar.getInstance().apply {
                timeInMillis = startDate
                add(Calendar.DAY_OF_YEAR, 7)
            }.timeInMillis
            
            // Deactivate any existing active goal
            currentGoal.value?.let { currentGoal ->
                repository.deactivateGoal(currentGoal.id)
            }
            
            val goal = GoalEntity(
                goalType = "WEEKLY_CHAPTERS",
                targetValue = targetChapters,
                currentProgress = _totalCompleted.value,
                startDate = startDate,
                endDate = endDate,
                isActive = true,
                isCompleted = false
            )
            
            repository.createGoal(goal)
        }
    }
    
    fun createDailyGoal(targetChapters: Int) {
        viewModelScope.launch {
            val startDate = System.currentTimeMillis()
            val endDate = Calendar.getInstance().apply {
                timeInMillis = startDate
                add(Calendar.DAY_OF_YEAR, 1)
            }.timeInMillis
            
            // Deactivate any existing active goal
            currentGoal.value?.let { currentGoal ->
                repository.deactivateGoal(currentGoal.id)
            }
            
            val goal = GoalEntity(
                goalType = "DAILY_CHAPTERS",
                targetValue = targetChapters,
                currentProgress = _totalCompleted.value,
                startDate = startDate,
                endDate = endDate,
                isActive = true,
                isCompleted = false
            )
            
            repository.createGoal(goal)
        }
    }
    
    fun getGoalProgressPercentage(goal: GoalEntity): Float {
        if (goal.targetValue == 0) return 0f
        return (goal.currentProgress.toFloat() / goal.targetValue.toFloat()).coerceIn(0f, 1f)
    }
    
    fun getGoalDaysRemaining(goal: GoalEntity): Int {
        val now = System.currentTimeMillis()
        val remaining = goal.endDate - now
        return (remaining / (1000 * 60 *  60 * 24)).toInt().coerceAtLeast(0)
    }
    
    fun loadDashboard(branchId: Int) {
        Log.d("GATE_TRACKER", "loadDashboard called with branchId: $branchId")
        viewModelScope.launch {
            // Load branch
            val branch = repository.getBranchById(branchId)
            Log.d("GATE_TRACKER", "Loaded branch: ${branch?.name}")
            _selectedBranch.value = branch
            
            // Load subjects and sort by most recently studied
            repository.getSubjectsByBranch(branchId).collect { subjectList ->
                Log.d("GATE_TRACKER", "Received ${subjectList.size} subjects from database")
                
                // Get last completion/revision dates for all subjects based on mode
                val isRevisionMode = repository.isRevisionMode().first()
                val subjectDates = mutableMapOf<Int, Long>()
                subjectList.forEach { subject ->
                    val lastDate = if (isRevisionMode) {
                        repository.getLastRevisionDateForSubject(subject.id)
                    } else {
                        repository.getLastCompletionDateForSubject(subject.id)
                    }
                    subjectDates[subject.id] = lastDate ?: 0L
                }
                
                // Sort subjects by last completion date (most recent first)
                val sortedSubjects = subjectList.sortedByDescending { subject ->
                    subjectDates[subject.id] ?: 0L
                }
                
                _subjects.value = sortedSubjects
                calculateProgress(sortedSubjects)
            }
        }
        
        loadExamCountdown(branchId)
        loadContinueStudying(branchId)
        loadRevisionProgress(branchId)
    }
    
    private fun loadRevisionProgress(branchId: Int) {
        viewModelScope.launch {
            repository.getTotalRevisedChapters(branchId).collect { revised ->
                _totalRevised.value = revised
            }
        }
    }
    
    private fun calculateProgress(subjects: List<SubjectEntity>) {
        val total = subjects.sumOf { it.totalChapters }
        val completed = subjects.sumOf { it.completedChapters }
        
        _totalChapters.value = total
        _totalCompleted.value = completed
        _progressPercentage.value = if (total > 0) (completed * 100 / total) else 0
        
        // Calculate subject status counts
        val completedSubjects = subjects.count { it.completedChapters == it.totalChapters && it.totalChapters > 0 }
        val inProgressSubjects = subjects.count { it.completedChapters > 0 && it.completedChapters < it.totalChapters }
        val notStartedSubjects = subjects.count { it.completedChapters == 0 }
        
        _completedSubjects.value = completedSubjects
        _inProgressSubjects.value = inProgressSubjects
        _notStartedSubjects.value = notStartedSubjects
        
        // Update widget data store
        com.gate.tracker.widget.WidgetDataStore.saveProgressData(
            getApplication(),
            completed,
            total
        )
        com.gate.tracker.widget.WidgetDataStore.saveSubjectCounts(
            getApplication(),
            completedSubjects,
            inProgressSubjects,
            notStartedSubjects
        )
        
        // Update goal progress if there's an active goal
        viewModelScope.launch {
            currentGoal.value?.let { goal ->
                repository.updateGoalProgress(goal.id, completed)
            }
        }
    }
    
    private val _currentStreak = MutableStateFlow(0)
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()
    
    // Longest streak tracking
    val longestStreak: StateFlow<Int> = repository.getUserPreference()
        .map { it?.longestStreak ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    
    // Current badge based on current streak
    val currentBadge: StateFlow<StreakBadge> = currentStreak
        .map { StreakBadge.fromStreak(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StreakBadge.NONE
        )
    
    // ... existing initialization ...

    private fun loadExamCountdown(branchId: Int) {
        Log.d("GATE_TRACKER", "loadExamCountdown called with branchId: $branchId")
        viewModelScope.launch {
            Log.d("GATE_TRACKER", "loadExamCountdown - getting exam date for branchId: $branchId")
            repository.getExamDate(branchId).collect { examDate ->
                Log.d("GATE_TRACKER", "loadExamCountdown - received exam date: $examDate for branchId: $branchId")
                examDate?.let {
                    val currentTime = System.currentTimeMillis()
                    val days = TimeUnit.MILLISECONDS.toDays(it - currentTime).toInt()
                    Log.d("GATE_TRACKER", "loadExamCountdown - calculated days remaining: $days (examDate: $it, currentTime: $currentTime)")
                    _daysRemaining.value = days
                    _motivationalMessage.value = getMotivationalMessage(days)
                    
                    // Update widget data store
                    com.gate.tracker.widget.WidgetDataStore.saveDaysRemaining(
                        getApplication(),
                        days
                    )
                }
            }
        }
        
        // Also load streak data
        viewModelScope.launch {
            repository.getAllCompletedChaptersWithDates(branchId).collect { chapters ->
                calculateStreak(chapters)
            }
        }
    }
    
    private fun calculateStreak(chapters: List<ChapterEntity>) {
        if (chapters.isEmpty()) {
            _currentStreak.value = 0
            return
        }
        
        // Group by date to get unique study days
        val dateKeys = chapters
            .filter { it.completedDate != null }
            .map { com.gate.tracker.util.DateUtils.getDateKey(it.completedDate!!) }
            .distinct()
            .sortedDescending() // Most recent first
            
        if (dateKeys.isEmpty()) {
            _currentStreak.value = 0
            return
        }
        
        val todayKey = com.gate.tracker.util.DateUtils.getDateKey(System.currentTimeMillis())
        val yesterdayKey = com.gate.tracker.util.DateUtils.getDateKey(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        
        var currentStreak = 0
        
        // Check if current streak is active (studied today or yesterday)
        val mostRecentKey = dateKeys.first()
        if (mostRecentKey == todayKey || mostRecentKey == yesterdayKey) {
            currentStreak = 1
            
            // Count consecutive days backwards from most recent
            for (i in 1 until dateKeys.size) {
                val prevDateKey = dateKeys[i - 1]
                val currDateKey = dateKeys[i]
                
                // Check if dates are consecutive (difference of 1 day)
                if (isConsecutiveDays(currDateKey.toLong(), prevDateKey.toLong())) {
                    currentStreak++
                } else {
                    break // Streak broken
                }
            }
        }
        
        _currentStreak.value = currentStreak
        
        // Update longest streak if current streak is a new record
        viewModelScope.launch {
            val currentLongest = longestStreak.value
            if (currentStreak > currentLongest) {
                repository.updateLongestStreak(currentStreak)
            }
        }
    }
    
    private fun isConsecutiveDays(date1: Long, date2: Long): Boolean {
        // date1 and date2 are date keys in format YYYYMMDD
        // Convert back to milliseconds and check if they're exactly 1 day apart
        val diff = kotlin.math.abs(date2 - date1)
        
        // For most consecutive days in same month/year, diff will be exactly 1
        // e.g., 20251209 - 20251208 = 1
        // But we need to handle month/year boundaries too
        // e.g., 20251201 - 20251130 = 71 (not consecutive by subtraction)
        
        // Simple check for same month
        if (diff == 1L) return true
        
        // For month boundaries, check by converting to actual dates
        // This handles cases like Dec 1 and Nov 30
        if (diff in 70L..90L) { // Possible month boundary
            val year1 = (date1 / 10000).toInt()
            val month1 = ((date1 % 10000) / 100).toInt()
            val day1 = (date1 % 100).toInt()
            
            val year2 = (date2 / 10000).toInt()
            val month2 = ((date2 % 10000) / 100).toInt()
            val day2 = (date2 % 100).toInt()
            
            // Check if it's actually consecutive (next day)
            return if (month1 == month2 && year1 == year2) {
                kotlin.math.abs(day2 - day1) == 1
            } else if (year1 == year2) {
                // Different months, check if day2 = 1 and day1 = last day of previous month
                (month2 == month1 + 1 && day2 == 1 && day1 >= 28) ||
                (month1 == month2 + 1 && day1 == 1 && day2 >= 28)
            } else {
                // Different years (Dec 31 -> Jan 1)
                (year2 == year1 + 1 && month2 == 1 && day2 == 1 && month1 == 12 && day1 == 31) ||
                (year1 == year2 + 1 && month1 == 1 && day1 == 1 && month2 == 12 && day2 == 31)
            }
        }
        
        return false
    }

    private fun getMotivationalMessage(days: Int): String = when {
        days > 180 -> "You have plenty of time! Start strong! 💪"
        days in 90..180 -> "Keep up the good work! 📚"
        days in 30..89 -> "Focus mode activated! 🎯"
        days > 0 -> "Final sprint! Give it your all! 🚀"
        else -> "Exam day is here! Best of luck! 🌟"
    }
    
    private fun loadContinueStudying(branchId: Int) {
        viewModelScope.launch {
            
            // Get the last studied subject
            val lastStudiedSubject = repository.getLastStudiedSubject(branchId)
            
            lastStudiedSubject?.let { subject ->
                // Get all chapters for this subject
                repository.getChaptersBySubject(subject.id).collect { chapters ->
                    // Find next incomplete chapter
                    val nextChapter = chapters
                        .filter { !it.isCompleted }
                        .minByOrNull { it.orderIndex }
                    
                    if (nextChapter != null) {
                        _continueStudying.value = ContinueStudyingData(
                            subject = subject,
                            nextChapter = nextChapter
                        )
                    }
                }
            }
        }
    }
}
