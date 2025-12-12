package com.gate.tracker.notifications.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gate.tracker.data.local.GateDatabase
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.notifications.NotificationHelper
import com.gate.tracker.data.drive.DriveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first


/**
 * Worker for exam countdown notifications
 */
class ExamCountdownWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = GateDatabase.getInstance(applicationContext)
            val driveManager = DriveManager(applicationContext)
            val repository = GateRepository(database, driveManager)
            val notificationHelper = NotificationHelper(applicationContext)
            
            // Check if enabled
            val prefs = repository.getNotificationPreferencesOnce()
            if (prefs?.examCountdownEnabled != true) {
                return Result.success()
            }
            
            // Get user preference to find branch
            val userPref = repository.getUserPreference().first()
            val branchId = userPref?.selectedBranchId ?: return Result.success()
            
            // Get exam date for branch
            val examDateMillis = repository.getExamDate(branchId).first()
            if (examDateMillis == null || examDateMillis == 0L) {
                return Result.success()
            }
            
            // Calculate days remaining
            val currentTime = System.currentTimeMillis()
            val daysRemaining = ((examDateMillis - currentTime) / (1000 * 60 * 60 * 24)).toInt()
            
            // Only send countdown for specific milestones
            val message = when (daysRemaining) {
                60 -> "2 months to go! Stay focused 💪"
                30 -> "1 month left! Time to intensify 🔥"
                14 -> "2 weeks remaining! Final push!"
                7 -> "Final week! Give your best 🎯"
                3 -> "Just 3 days! Stay calm and confident"
                1 -> "Tomorrow is the day! You're ready! 🚀"
                0 -> "Today is GATE day! All the best! 🌟"
                0 -> "Today is GATE day! All the best! 🌟"
                else -> "Focus on your goal! 🎯"
            }
            
            if (message != null && daysRemaining >= 0) {
                notificationHelper.showExamCountdown(daysRemaining, message)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
