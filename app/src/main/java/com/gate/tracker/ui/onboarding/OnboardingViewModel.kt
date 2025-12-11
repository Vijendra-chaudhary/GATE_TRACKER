package com.gate.tracker.ui.onboarding

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.ChapterEntity
import com.gate.tracker.data.local.entity.SubjectEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val subjects: List<SubjectEntity> = emptyList(),
    val chaptersBySubject: Map<Int, List<ChapterEntity>> = emptyMap(),
    val isLoading: Boolean = true
)

class OnboardingViewModel(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModel() {

    // Store selected chapter IDs
    private val _selectedChapterIds = mutableStateListOf<Int>()
    val selectedChapterIds: List<Int> get() = _selectedChapterIds

    val uiState: StateFlow<OnboardingUiState> = combine(
        repository.getSubjectsByBranch(branchId),
        repository.getAllChaptersForBranch(branchId) // Need a way to get all chapters efficiently
    ) { subjects, chapters ->
        // Filter out already completed chapters so users don't overwrite them or see duplicates
        val incompleteChapters = chapters.filter { !it.isCompleted }
        
        OnboardingUiState(
            subjects = subjects,
            chaptersBySubject = incompleteChapters.groupBy { it.subjectId },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OnboardingUiState()
    )

    fun toggleChapterSelection(chapterId: Int) {
        if (_selectedChapterIds.contains(chapterId)) {
            _selectedChapterIds.remove(chapterId)
        } else {
            _selectedChapterIds.add(chapterId)
        }
    }

    fun toggleSubjectSelection(subjectId: Int, chapterIds: List<Int>) {
        val allSelected = chapterIds.all { _selectedChapterIds.contains(it) }
        if (allSelected) {
            _selectedChapterIds.removeAll(chapterIds)
        } else {
            // Add ones that aren't already selected
            chapterIds.forEach { id ->
                if (!_selectedChapterIds.contains(id)) {
                    _selectedChapterIds.add(id)
                }
            }
        }
    }

    fun savePreciseCompletions(onComplete: () -> Unit) {
        viewModelScope.launch {
            if (_selectedChapterIds.isNotEmpty()) {
                repository.markChaptersAsPreExisting(_selectedChapterIds.toList())
            }
            // Mark onboarding as done (handled by caller navigating away usually, 
            // but we might want to set a flag in pure VM too if logic gets complex)
            onComplete()
        }
    }
}

class OnboardingViewModelFactory(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OnboardingViewModel(repository, branchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
