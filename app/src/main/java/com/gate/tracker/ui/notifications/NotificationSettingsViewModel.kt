package com.gate.tracker.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.NotificationPreferencesEntity
import com.gate.tracker.data.repository.GateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Notification Settings
 */
class NotificationSettingsViewModel(
    private val repository: GateRepository
) : ViewModel() {
    
    val notificationPreferences: StateFlow<NotificationPreferencesEntity?> = 
        repository.getNotificationPreferences()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    
    fun updateDailyReminder(enabled: Boolean, time: String) {
        viewModelScope.launch {
            repository.updateDailyReminder(enabled, time)
        }
    }
    
    fun updateRevisionAlerts(enabled: Boolean, time: String = "18:00", days: String = "1,3,5") {
        viewModelScope.launch {
            repository.updateRevisionAlerts(enabled, time, days)
        }
    }
    
    fun updateMockTestReminders(enabled: Boolean, time: String = "15:00", days: String = "0,6") {
        viewModelScope.launch {
            repository.updateMockTestReminders(enabled, time, days)
        }
    }
    
    fun updateExamCountdown(enabled: Boolean, time: String = "20:00") {
        viewModelScope.launch {
            repository.updateExamCountdown(enabled, time)
        }
    }
    
    fun updateInactivityAlerts(enabled: Boolean, thresholdDays: Int, time: String = "19:00") {
        viewModelScope.launch {
            repository.updateInactivityAlerts(enabled, thresholdDays, time)
        }
    }
    
    fun updateMotivational(enabled: Boolean, time: String = "08:00") {
        viewModelScope.launch {
            repository.updateMotivational(enabled, time)
        }
    }
    
    fun updateAchievementNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAchievementNotifications(enabled)
        }
    }
    
    fun updateQuietHours(enabled: Boolean, start: String, end: String) {
        viewModelScope.launch {
            repository.updateQuietHours(enabled, start, end)
        }
    }
}

/**
 * Factory for NotificationSettingsViewModel
 */
class NotificationSettingsViewModelFactory(
    private val repository: GateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationSettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
