package com.gate.tracker.notifications.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationHelper
import kotlinx.coroutines.flow.first

/**
 * Worker for inactivity alert notifications
 */
class InactivityAlertWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = GateDatabase.getInstance(applicationContext)
            val repository = GateRepository(database)
            val notificationHelper = NotificationHelper(applicationContext)
            
            // Check if enabled
            val prefs = repository.getNotificationPreferencesOnce()
            if (prefs?.inactivityAlertsEnabled != true) {
                return Result.success()
            }
            
            // Get user's selected branch
            val userPref = repository.getUserPreference().first()
            val branchId = userPref?.selectedBranchId ?: return Result.success()
            
            // Find last activity time
            val subjects = repository.getSubjectsByBranch(branchId).first()
            var lastActivityTime = 0L
            
            subjects.forEach { subject ->
                val chapters = repository.getChaptersBySubject(subject.id).first()
                chapters.forEach { chapter ->
                    val chapterTime = chapter.completedDate ?: 0L
                    if (chapterTime > lastActivityTime) {
                        lastActivityTime = chapterTime
                    }
                }
            }
            
            // Calculate days since last activity
            val currentTime = System.currentTimeMillis()
            val daysSinceActivity = ((currentTime - lastActivityTime) / (1000 * 60 * 60 * 24)).toInt()
            
            // Send alert if inactive for threshold days
            if (daysSinceActivity >= prefs.inactivityThresholdDays) {
                notificationHelper.showInactivityAlert(daysSinceActivity)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
