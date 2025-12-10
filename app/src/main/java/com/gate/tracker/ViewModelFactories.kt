package com.gate.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.UserPreferenceEntity
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.ui.branch.BranchSelectionViewModel
import com.gate.tracker.ui.dashboard.DashboardViewModel
import com.gate.tracker.ui.examdate.ExamDateViewModel
import com.gate.tracker.ui.subject.SubjectDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UserPreferenceViewModel
class UserPreferenceViewModel(
    private val repository: GateRepository
) : ViewModel() {
    
    private val _userPreference = MutableStateFlow<UserPreferenceEntity?>(null)
    val userPreference: StateFlow<UserPreferenceEntity?> = _userPreference.asStateFlow()
    
    init {
        viewModelScope.launch {
            repository.getUserPreference().collect { pref ->
                _userPreference.value = pref
            }
        }
    }
    
    fun updateTheme(mode: Int) {
        viewModelScope.launch {
            repository.setThemeMode(mode)
        }
    }
}

// ViewModelFactories
class UserPreferenceViewModelFactory(
    private val repository: GateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferenceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPreferenceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BranchSelectionViewModelFactory(
    private val repository: GateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BranchSelectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BranchSelectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class DashboardViewModelFactory(
    private val application: android.app.Application,
    private val repository: GateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SubjectDetailViewModelFactory(
    private val repository: GateRepository,
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubjectDetailViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ExamDateViewModelFactory(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamDateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamDateViewModel(repository, branchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProgressCalendarViewModelFactory(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.gate.tracker.ui.calendar.ProgressCalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.gate.tracker.ui.calendar.ProgressCalendarViewModel(repository, branchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MockTestViewModelFactory(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.gate.tracker.ui.mocktest.MockTestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.gate.tracker.ui.mocktest.MockTestViewModel(repository, branchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ResourcesViewModelFactory(
    private val repository: GateRepository,
    private val subjectId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(com.gate.tracker.ui.resources.ResourcesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return com.gate.tracker.ui.resources.ResourcesViewModel(repository, subjectId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

