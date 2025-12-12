package com.gate.tracker.ui.mocktest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.MockTestEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State for Mock Tests Screen
 */
data class MockTestUiState(
    val tests: List<MockTestEntity> = emptyList(),
    val averageScore: Float = 0f,
    val averagePercentage: Float = 0f,
    val highestScore: Float = 0f,
    val highestPercentage: Float = 0f,
    val latestPercentage: Float? = null,
    val testCount: Int = 0,
    val isLoading: Boolean = true,
    val isEmpty: Boolean = true
)

/**
 * ViewModel for managing mock test data and business logic
 */
class MockTestViewModel(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModel() {

    // Reactive state flow for UI
    val uiState: StateFlow<MockTestUiState> = repository
        .getMockTestsForBranch(branchId)
        .map { tests -> calculateUiState(tests) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MockTestUiState()
        )
    
    // Get subjects for this branch
    val subjects: StateFlow<List<com.gate.tracker.data.local.entity.SubjectEntity>> = repository
        .getSubjectsByBranch(branchId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Add a new mock test
     */
    fun addMockTest(
        testName: String,
        score: Float,
        maxScore: Float,
        testDate: Long,
        testType: com.gate.tracker.data.local.entity.TestType,
        selectedSubjects: List<Int>,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                // Validation
                if (testName.isBlank()) {
                    onError("Test name cannot be empty")
                    return@launch
                }
                if (score < 0 || maxScore <= 0) {
                    onError("Invalid score values")
                    return@launch
                }
                if (score > maxScore) {
                    onError("Score cannot exceed maximum score")
                    return@launch
                }
                if (testDate > System.currentTimeMillis()) {
                    onError("Test date cannot be in the future")
                    return@launch
                }
                if (testType == com.gate.tracker.data.local.entity.TestType.SELECTED_SUBJECT && selectedSubjects.isEmpty()) {
                    onError("Please select at least one subject")
                    return@launch
                }

                val test = MockTestEntity(
                    branchId = branchId,
                    testName = testName,
                    score = score,
                    maxScore = maxScore,
                    testDate = testDate,
                    testType = testType,
                    selectedSubjects = selectedSubjects
                )
                
                repository.addMockTest(test)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to add test: ${e.message}")
            }
        }
    }

    /**
     * Delete a mock test
     */
    fun deleteMockTest(
        test: MockTestEntity,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteMockTest(test)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to delete test: ${e.message}")
            }
        }
    }

    /**
     * Delete all mock tests for current branch
     */
    fun deleteAllTests(
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteAllMockTestsForBranch(branchId)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to delete all tests: ${e.message}")
            }
        }
    }

    /**
     * Calculate UI state from test list
     */
    private fun calculateUiState(tests: List<MockTestEntity>): MockTestUiState {
        if (tests.isEmpty()) {
            return MockTestUiState(
                tests = emptyList(),
                isLoading = false,
                isEmpty = true
            )
        }

        val totalScore = tests.sumOf { it.score.toDouble() }.toFloat()
        val totalMaxScore = tests.sumOf { it.maxScore.toDouble() }.toFloat()
        val avgScore = totalScore / tests.size
        val avgPercentage = if (totalMaxScore > 0) (totalScore / totalMaxScore) * 100 else 0f

        val highestTest = tests.maxByOrNull { it.percentage }
        val latestTest = tests.firstOrNull() // Already sorted by date DESC

        return MockTestUiState(
            tests = tests,
            averageScore = avgScore,
            averagePercentage = avgPercentage,
            highestScore = highestTest?.score ?: 0f,
            highestPercentage = highestTest?.percentage ?: 0f,
            latestPercentage = latestTest?.percentage,
            testCount = tests.size,
            isLoading = false,
            isEmpty = false
        )
    }

    /**
     * Get chart data points for visualization
     * Returns list of (timestamp, percentage) pairs sorted by date
     */
    fun getChartDataPoints(): List<Pair<Long, Float>> {
        val currentTests = uiState.value.tests
        return currentTests
            .sortedBy { it.testDate } // Sort chronologically for chart
            .map { it.testDate to it.percentage }
    }

    /**
     * Get formatted test history grouped by month (optional future enhancement)
     */
    fun getTestsByMonth(): Map<String, List<MockTestEntity>> {
        val tests = uiState.value.tests
        return tests.groupBy { test ->
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = test.testDate
            "${calendar.get(java.util.Calendar.MONTH)}-${calendar.get(java.util.Calendar.YEAR)}"
        }
    }
}
