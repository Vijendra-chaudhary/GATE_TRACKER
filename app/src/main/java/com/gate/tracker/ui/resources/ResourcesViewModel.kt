package com.gate.tracker.ui.resources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.ResourceEntity
import com.gate.tracker.data.local.entity.ResourceType
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State for Resources
 */
data class ResourcesUiState(
    val resources: List<ResourceEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = true
)

/**
 * ViewModel for managing resources
 */
class ResourcesViewModel(
    private val repository: GateRepository,
    private val subjectId: Int
) : ViewModel() {

    // Reactive state flow for UI
    val uiState: StateFlow<ResourcesUiState> = repository
        .getResourcesBySubject(subjectId)
        .map { resources -> calculateUiState(resources) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResourcesUiState()
        )

    /**
     * Add a new resource
     */
    fun addResource(
        title: String,
        uri: String,
        resourceType: ResourceType,
        description: String = "",
        fileSize: Long? = null,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                if (title.isBlank()) {
                    onError("Title cannot be empty")
                    return@launch
                }
                if (uri.isBlank()) {
                    onError("URI/Link cannot be empty")
                    return@launch
                }

                val resource = ResourceEntity(
                    subjectId = subjectId,
                    resourceType = resourceType,
                    title = title,
                    uri = uri,
                    description = description,
                    fileSize = fileSize
                )
                
                repository.addResource(resource)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to add resource: ${e.message}")
            }
        }
    }

    /**
     * Delete a resource
     */
    fun deleteResource(
        resource: ResourceEntity,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteResource(resource)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to delete resource: ${e.message}")
            }
        }
    }

    /**
     * Calculate UI state from resource list
     */
    private fun calculateUiState(resources: List<ResourceEntity>): ResourcesUiState {
        return ResourcesUiState(
            resources = resources,
            isLoading = false,
            isEmpty = resources.isEmpty()
        )
    }
}
