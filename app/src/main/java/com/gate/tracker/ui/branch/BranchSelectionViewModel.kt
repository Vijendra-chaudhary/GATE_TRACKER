package com.gate.tracker.ui.branch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.BranchEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BranchSelectionViewModel(
    private val repository: GateRepository
) : ViewModel() {
    
    private val _branches = MutableStateFlow<List<BranchEntity>>(emptyList())
    val branches: StateFlow<List<BranchEntity>> = _branches.asStateFlow()
    
    private val _filteredBranches = MutableStateFlow<List<BranchEntity>>(emptyList())
    val filteredBranches: StateFlow<List<BranchEntity>> = _filteredBranches.asStateFlow()
    
    private val _selectedBranch = MutableStateFlow<BranchEntity?>(null)
    val selectedBranch: StateFlow<BranchEntity?> = _selectedBranch.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        loadBranches()
    }
    
    private fun loadBranches() {
        viewModelScope.launch {
            repository.getAllBranches().collect { branchList ->
                _branches.value = branchList
                filterBranches()
            }
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        filterBranches()
    }
    
    private fun filterBranches() {
        val query = _searchQuery.value.lowercase()
        _filteredBranches.value = if (query.isEmpty()) {
            _branches.value
        } else {
            _branches.value.filter {
                it.name.lowercase().contains(query) ||
                it.code.lowercase().contains(query) ||
                it.description.lowercase().contains(query)
            }
        }
    }
    
    fun selectBranch(branch: BranchEntity) {
        _selectedBranch.value = branch
    }
    
    fun onContinueClicked(onSuccess: () -> Unit) {
        val branch = _selectedBranch.value ?: return
        viewModelScope.launch {
            repository.selectBranch(branch.id)
            onSuccess()
        }
    }
}
