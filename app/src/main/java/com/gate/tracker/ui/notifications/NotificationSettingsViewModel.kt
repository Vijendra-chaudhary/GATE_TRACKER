package com.gate.tracker.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gate.tracker.data.local.entity.NotificationPreferencesEntity
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for Notification Settings
 */
class NotificationSettingsViewModel(
    private val app: Application,
    private val repository: GateRepository
) : AndroidViewModel(app) {
    
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
            if (enabled) {
                NotificationScheduler.scheduleDailyReminder(app)
            } else {
                NotificationScheduler.cancelNotification(app, "daily_reminder")
            }
        }
    }
    
    fun updateRevisionAlerts(enabled: Boolean, time: String = "18:00", days: String = "1,3,5") {
        viewModelScope.launch {
            repository.updateRevisionAlerts(enabled, time, days)
            if (enabled) {
                NotificationScheduler.scheduleRevisionAlerts(app)
            } else {
                NotificationScheduler.cancelNotification(app, "revision_alert")
            }
        }
    }
    
    fun updateMockTestReminders(enabled: Boolean, time: String = "15:00", days: String = "0,6") {
        viewModelScope.launch {
            repository.updateMockTestReminders(enabled, time, days)
            if (enabled) {
                NotificationScheduler.scheduleMockTestReminders(app)
            } else {
                NotificationScheduler.cancelNotification(app, "mock_test_reminder")
            }
        }
    }
    
    fun updateExamCountdown(enabled: Boolean, time: String = "20:00") {
        viewModelScope.launch {
            repository.updateExamCountdown(enabled, time)
            if (enabled) {
                NotificationScheduler.scheduleExamCountdown(app)
            } else {
                NotificationScheduler.cancelNotification(app, "exam_countdown")
            }
        }
    }
    
    fun updateInactivityAlerts(enabled: Boolean, thresholdDays: Int, time: String = "19:00") {
        viewModelScope.launch {
            repository.updateInactivityAlerts(enabled, thresholdDays, time)
            if (enabled) {
                NotificationScheduler.scheduleInactivityAlerts(app)
            } else {
                NotificationScheduler.cancelNotification(app, "inactivity_alert")
            }
        }
    }
    
    fun updateMotivational(enabled: Boolean, time: String = "21:00") {
        viewModelScope.launch {
            repository.updateMotivational(enabled, time)
            if (enabled) {
                NotificationScheduler.scheduleMotivational(app)
            } else {
                NotificationScheduler.cancelNotification(app, "motivational")
            }
        }
    }
    
    fun updateAchievementNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAchievementNotifications(enabled)
            // No specific scheduler strictly for achievements (they are event based), but good to track
        }
    }
    
    fun updateQuietHours(enabled: Boolean, start: String, end: String) {
        viewModelScope.launch {
            repository.updateQuietHours(enabled, start, end)
            // Re-schedule all to respect new quiet hours immediately? 
            // Actually workers check quiet hours at runtime, so we don't strictly need to reschedule,
            // but if the *scheduled time* is now inside/outside quiet hours, it might matter.
            // For now, simpler is better. Workers check constraints at runtime.
        }
    }
}

/**
 * Factory for NotificationSettingsViewModel
 */
class NotificationSettingsViewModelFactory(
    private val app: Application,
    private val repository: GateRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificationSettingsViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
