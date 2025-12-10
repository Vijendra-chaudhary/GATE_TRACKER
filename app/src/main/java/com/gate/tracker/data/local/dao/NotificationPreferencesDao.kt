package com.gate.tracker.data.local.dao

import androidx.room.*
import com.gate.tracker.data.local.entity.NotificationPreferencesEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for notification preferences
 */
@Dao
interface NotificationPreferencesDao {
    
    /**
     * Get notification preferences as Flow (reactive)
     */
    @Query("SELECT * FROM notification_preferences WHERE id = 1")
    fun getPreferences(): Flow<NotificationPreferencesEntity?>
    
    /**
     * Get notification preferences (one-time)
     */
    @Query("SELECT * FROM notification_preferences WHERE id = 1")
    suspend fun getPreferencesOnce(): NotificationPreferencesEntity?
    
    /**
     * Insert or update notification preferences
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: NotificationPreferencesEntity)
    
    /**
     * Update notification preferences
     */
    @Update
    suspend fun updatePreferences(preferences: NotificationPreferencesEntity)
    
    /**
     * Update daily reminder settings
     */
    @Query("UPDATE notification_preferences SET dailyReminderEnabled = :enabled, dailyReminderTime = :time WHERE id = 1")
    suspend fun updateDailyReminder(enabled: Boolean, time: String)
    
    /**
     * Update revision alert settings
     */
    @Query("UPDATE notification_preferences SET revisionAlertsEnabled = :enabled, revisionAlertsTime = :time, revisionAlertsDays = :days WHERE id = 1")
    suspend fun updateRevisionAlerts(enabled: Boolean, time: String, days: String)
    
    /**
     * Update mock test reminder settings
     */
    @Query("UPDATE notification_preferences SET mockTestRemindersEnabled = :enabled, mockTestRemindersTime = :time, mockTestRemindersDays = :days WHERE id = 1")
    suspend fun updateMockTestReminders(enabled: Boolean, time: String, days: String)
    
    /**
     * Update exam countdown settings
     */
    @Query("UPDATE notification_preferences SET examCountdownEnabled = :enabled, examCountdownTime = :time WHERE id = 1")
    suspend fun updateExamCountdown(enabled: Boolean, time: String)
    
    /**
     * Update inactivity alert settings
     */
    @Query("UPDATE notification_preferences SET inactivityAlertsEnabled = :enabled, inactivityThresholdDays = :thresholdDays, inactivityAlertsTime = :time WHERE id = 1")
    suspend fun updateInactivityAlerts(enabled: Boolean, thresholdDays: Int, time: String)
    
    /**
     * Update motivational notifications
     */
    @Query("UPDATE notification_preferences SET motivationalEnabled = :enabled, motivationalTime = :time WHERE id = 1")
    suspend fun updateMotivational(enabled: Boolean, time: String)
    
    /**
     * Update achievement notifications
     */
    @Query("UPDATE notification_preferences SET achievementNotificationsEnabled = :enabled WHERE id = 1")
    suspend fun updateAchievementNotifications(enabled: Boolean)
    
    /**
     * Update quiet hours
     */
    @Query("UPDATE notification_preferences SET quietHoursEnabled = :enabled, quietHoursStart = :start, quietHoursEnd = :end WHERE id = 1")
    suspend fun updateQuietHours(enabled: Boolean, start: String, end: String)
}
