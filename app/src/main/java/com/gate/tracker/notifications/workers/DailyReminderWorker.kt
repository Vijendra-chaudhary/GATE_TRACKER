package com.gate.tracker.notifications.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationHelper
import kotlinx.coroutines.flow.first

/**
 * Worker for daily study reminder notifications
 */
class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = GateDatabase.getInstance(applicationContext)
            val repository = GateRepository(database)
            val notificationHelper = NotificationHelper(applicationContext)
            
            // Check if notifications are enabled
            val prefs = repository.getNotificationPreferencesOnce()
            if (prefs?.dailyReminderEnabled != true) {
                return Result.success()
            }
            
            // Check quiet hours
            if (isQuietHours(prefs.quietHoursEnabled, prefs.quietHoursStart, prefs.quietHoursEnd)) {
                return Result.success()
            }
            
            // Get user's selected branch
            val userPref = repository.getUserPreference().first()
            val branchId = userPref?.selectedBranchId ?: return Result.success()
            
            // Count pending chapters
            val subjects = repository.getSubjectsByBranch(branchId).first()
            var totalPending = 0
            subjects.forEach { subject ->
                val chapters = repository.getChaptersBySubject(subject.id).first()
                totalPending += chapters.count { !it.isCompleted }
            }
            
            if (totalPending > 0) {
                notificationHelper.showDailyReminder(totalPending)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    private fun isQuietHours(enabled: Boolean, start: String, end: String): Boolean {
        if (!enabled) return false
        
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE)
        val currentTime = currentHour * 60 + currentMinute
        
        val (startHour, startMinute) = start.split(":").map { it.toInt() }
        val startTime = startHour * 60 + startMinute
        
        val (endHour, endMinute) = end.split(":").map { it.toInt() }
        val endTime = endHour * 60 + endMinute
        
        return if (startTime < endTime) {
            currentTime in startTime..endTime
        } else {
            currentTime >= startTime || currentTime <= endTime
        }
    }
}
