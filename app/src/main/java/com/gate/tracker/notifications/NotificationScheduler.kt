package com.gate.tracker.notifications

import android.content.Context
import androidx.work.*
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.notifications.workers.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Scheduler for all notification types using WorkManager
 */
object NotificationScheduler {
    
    // Work tags for easy cancellation
    private const val TAG_DAILY_REMINDER = "daily_reminder"
    private const val TAG_REVISION_ALERT = "revision_alert"
    private const val TAG_MOCK_TEST_REMINDER = "mock_test_reminder"
    private const val TAG_EXAM_COUNTDOWN = "exam_countdown"
    private const val TAG_INACTIVITY_ALERT = "inactivity_alert"
    private const val TAG_MOTIVATIONAL = "motivational"
    
    /**
     * Schedule all notifications based on user preferences
     */
    fun scheduleAll(context: Context) {
        scheduleDailyReminder(context)
        scheduleRevisionAlerts(context)
        scheduleMockTestReminders(context)
        scheduleExamCountdown(context)
        scheduleInactivityAlerts(context)
        scheduleMotivational(context)
    }
    
    /**
     * Schedule daily study reminder with custom time from preferences
     */
    fun scheduleDailyReminder(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.dailyReminderTime ?: "09:00"
            val (hour, minute) = parseTime(time)
            
            scheduleDailyReminderAtHour(context, hour, minute)
        }
    }
    
    /**
     * Schedule daily reminder at a specific hour (used by AdaptiveSchedulerWorker)
     */
    fun scheduleDailyReminderAtHour(context: Context, hour: Int, minute: Int = 0) {
        val currentTime = java.util.Calendar.getInstance()
        val targetTime = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            
            // If target time is in the past, schedule for tomorrow
            if (before(currentTime)) {
                add(java.util.Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis
        
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag(TAG_DAILY_REMINDER)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG_DAILY_REMINDER,
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest)
    }
    
    /**
     * Schedule revision alerts with custom time and days
     */
    fun scheduleRevisionAlerts(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.revisionAlertsTime ?: "18:00"
            val days = prefs?.revisionAlertsDays ?: "1,3,5"
            val (hour, minute) = parseTime(time)
            
            scheduleAtTimeOnDays(context, hour, minute, days, TAG_REVISION_ALERT) {
                PeriodicWorkRequestBuilder<RevisionAlertWorker>(1, TimeUnit.DAYS)
            }
        }
    }
    
    /**
     * Schedule mock test reminders with custom time and days
     */
    fun scheduleMockTestReminders(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.mockTestRemindersTime ?: "15:00"
            val days = prefs?.mockTestRemindersDays ?: "0,6"
            val (hour, minute) = parseTime(time)
            
            scheduleAtTimeOnDays(context, hour, minute, days, TAG_MOCK_TEST_REMINDER) {
                PeriodicWorkRequestBuilder<MockTestReminderWorker>(1, TimeUnit.DAYS)
            }
        }
    }
    
    /**
     * Schedule exam countdown with custom time
     */
    fun scheduleExamCountdown(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.examCountdownTime ?: "20:00"
            val (hour, minute) = parseTime(time)
            
            scheduleDaily(context, hour, minute, TAG_EXAM_COUNTDOWN) {
                PeriodicWorkRequestBuilder<ExamCountdownWorker>(1, TimeUnit.DAYS)
            }
        }
    }
    
    /**
     * Schedule inactivity alerts with custom time
     */
    fun scheduleInactivityAlerts(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.inactivityAlertsTime ?: "19:00"
            val (hour, minute) = parseTime(time)
            
            scheduleDaily(context, hour, minute, TAG_INACTIVITY_ALERT) {
                PeriodicWorkRequestBuilder<InactivityAlertWorker>(1, TimeUnit.DAYS)
            }
        }
    }
    
    /**
     * Schedule motivational notifications with custom time
     */
    fun scheduleMotivational(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = GateDatabase.getInstance(context)
                .notificationPreferencesDao()
                .getPreferencesOnce()
            
            val time = prefs?.motivationalTime ?: "08:00"
            val (hour, minute) = parseTime(time)
            
            scheduleDaily(context, hour, minute, TAG_MOTIVATIONAL) {
                PeriodicWorkRequestBuilder<MotivationalWorker>(1, TimeUnit.DAYS)
            }
        }
    }
    
    /**
     * Cancel a specific notification type
     */
    fun cancelNotification(context: Context, tag: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag)
    }
    
    /**
     * Cancel all scheduled notifications
     */
    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_DAILY_REMINDER)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_REVISION_ALERT)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_MOCK_TEST_REMINDER)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_EXAM_COUNTDOWN)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_INACTIVITY_ALERT)
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_MOTIVATIONAL)
    }
    
    // ===== Helper Methods =====
    
    /**
     * Parse time string (HH:mm) to hour and minute
     */
    private fun parseTime(time: String): Pair<Int, Int> {
        return try {
            val parts = time.split(":")
            val hour = parts.getOrNull(0)?.toIntOrNull() ?: 9
            val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
            Pair(hour, minute)
        } catch (e: Exception) {
            Pair(9, 0) // Default to 9:00 AM
        }
    }
    
    /**
     * Schedule a daily notification at specific time
     */
    private fun scheduleDaily(
        context: Context,
        hour: Int,
        minute: Int,
        tag: String,
        workRequestBuilder: () -> PeriodicWorkRequest.Builder
    ) {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // If target time is in the past, schedule for tomorrow
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis
        
        val workRequest = workRequestBuilder()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag(tag)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
    
    /**
     * Schedule notification at specific time on selected days
     */
    private fun scheduleAtTimeOnDays(
        context: Context,
        hour: Int,
        minute: Int,
        daysString: String,
        tag: String,
        workRequestBuilder: () -> PeriodicWorkRequest.Builder
    ) {
        val selectedDays = daysString.split(",")
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // Find next occurrence of selected day
            var daysToAdd = 0
            val currentDayOfWeek = get(Calendar.DAY_OF_WEEK) - 1 // Convert to 0-6 (Sun=0)
            
            // Check if today is a selected day and time hasn't passed
            if (selectedDays.contains(currentDayOfWeek) && after(currentTime)) {
                // Schedule for today
                daysToAdd = 0
            } else {
                // Find next selected day
                for (i in 1..7) {
                    val checkDay = (currentDayOfWeek + i) % 7
                    if (selectedDays.contains(checkDay)) {
                        daysToAdd = i
                        break
                    }
                }
            }
            
            add(Calendar.DAY_OF_MONTH, daysToAdd)
        }
        
        val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis
        
        val workRequest = workRequestBuilder()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .addTag(tag)
            .build()
        
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
