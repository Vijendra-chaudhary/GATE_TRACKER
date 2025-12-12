package com.gate.tracker.ui.examdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class ExamDateViewModel(
    private val repository: GateRepository,
    private val branchId: Int
) : ViewModel() {
    
    private val _examDate = MutableStateFlow<Long?>(null)
    val examDate: StateFlow<Long?> = _examDate.asStateFlow()
    
    init {
        loadExamDate()
    }
    
    private fun loadExamDate() {
        viewModelScope.launch {
            repository.getExamDate(branchId).collect { date ->
                _examDate.value = date
            }
        }
    }
    
    fun updateExamDate(year: Int, month: Int, day: Int, onSuccess: () -> Unit) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        viewModelScope.launch {
            repository.updateExamDate(branchId, calendar.timeInMillis)
            onSuccess()
        }
    }
}
