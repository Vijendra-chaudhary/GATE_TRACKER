package com.gate.tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to store user notification preferences
 */
@Entity(tableName = "notification_preferences")
data class NotificationPreferencesEntity(
    @PrimaryKey val id: Int = 1, // Single row for app-wide settings
    
    // Daily Study Reminder
    val dailyReminderEnabled: Boolean = true,
    val dailyReminderTime: String = "09:00", // HH:mm format
    
    // Revision Alerts
    val revisionAlertsEnabled: Boolean = true,
    val revisionAlertsTime: String = "18:00",
    val revisionAlertsDays: String = "1,3,5", // Days of week (0=Sun, 1=Mon, etc.)
    val revisionThresholdDays: Int = 3, // Alert if chapter not revised in X days
    
    // Mock Test Reminders
    val mockTestRemindersEnabled: Boolean = true,
    val mockTestRemindersTime: String = "15:00",
    val mockTestRemindersDays: String = "0,6", // Days of week (0=Sun, 6=Sat)
    val mockTestReminderFrequency: Int = 7, // Remind if no test in X days
    
    // Exam Countdown
    val examCountdownEnabled: Boolean = true,
    val examCountdownTime: String = "20:00",
    
    // Inactivity Alerts
    val inactivityAlertsEnabled: Boolean = true,
    val inactivityAlertsTime: String = "19:00",
    val inactivityThresholdDays: Int = 3, // Alert if no activity in X days
    
    // Motivational Notifications
    val motivationalEnabled: Boolean = true,
    val motivationalTime: String = "21:00",
    
    // Achievement Notifications
    val achievementNotificationsEnabled: Boolean = true,
    
    // Do Not Disturb
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: String = "22:00", // HH:mm format
    val quietHoursEnd: String = "08:00" // HH:mm format
)
