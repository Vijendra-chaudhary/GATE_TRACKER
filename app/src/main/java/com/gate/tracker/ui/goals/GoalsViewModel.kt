package com.gate.tracker.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.GoalEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class GoalsViewModel(private val repository: GateRepository) : ViewModel() {
    
    private val _allGoals = MutableStateFlow<List<GoalEntity>>(emptyList())
    val allGoals: StateFlow<List<GoalEntity>> = _allGoals.asStateFlow()
    
    private val _currentActiveGoal = MutableStateFlow<GoalEntity?>(null)
    val currentActiveGoal: StateFlow<GoalEntity?> = _currentActiveGoal.asStateFlow()
    
    init {
        loadGoals()
        loadCurrentGoal()
    }
    
    private fun loadGoals() {
        viewModelScope.launch {
            repository.getAllGoals().collect { goals ->
                _allGoals.value = goals
            }
        }
    }
    
    private fun loadCurrentGoal() {
        viewModelScope.launch {
            repository.getCurrentActiveGoal().collect { goal ->
                _currentActiveGoal.value = goal
            }
        }
    }
    
    fun createWeeklyGoal(targetChapters: Int) {
        viewModelScope.launch {
            val startDate = System.currentTimeMillis()
            val endDate = Calendar.getInstance().apply {
                timeInMillis = startDate
                add(Calendar.DAY_OF_YEAR, 7)
            }.timeInMillis
            
            // Deactivate any existing active goals
            _currentActiveGoal.value?.let { currentGoal ->
                repository.deactivateGoal(currentGoal.id)
            }
            
            val goal = GoalEntity(
                goalType = "WEEKLY_CHAPTERS",
                targetValue = targetChapters,
                currentProgress = 0,
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
            
            // Deactivate any existing active goals
            _currentActiveGoal.value?.let { currentGoal ->
                repository.deactivateGoal(currentGoal.id)
            }
            
            val goal = GoalEntity(
                goalType = "DAILY_CHAPTERS",
                targetValue = targetChapters,
                currentProgress = 0,
                startDate = startDate,
                endDate = endDate,
                isActive = true,
                isCompleted = false
            )
            
            repository.createGoal(goal)
        }
    }
    
    fun updateGoalProgress(goalId: Int, completedChapters: Int) {
        viewModelScope.launch {
            repository.updateGoalProgress(goalId, completedChapters)
        }
    }
    
    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
        }
    }
    
    fun getProgressPercentage(goal: GoalEntity): Float {
        if (goal.targetValue == 0) return 0f
        return (goal.currentProgress.toFloat() / goal.targetValue.toFloat()).coerceIn(0f, 1f)
    }
    
    fun getDaysRemaining(goal: GoalEntity): Int {
        val now = System.currentTimeMillis()
        val remaining = goal.endDate - now
        return (remaining / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    }
    
    fun isGoalExpired(goal: GoalEntity): Boolean {
        return System.currentTimeMillis() > goal.endDate
    }
}
