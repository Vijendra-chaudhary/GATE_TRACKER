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
 * Worker for daily study reminder notifications
 */
class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = GateDatabase.getInstance(applicationContext)
            val driveManager = DriveManager(applicationContext)
            val repository = GateRepository(database, driveManager)
            val notificationHelper = NotificationHelper(applicationContext)
            
            // Check if notifications are enabled
            val prefs = repository.getNotificationPreferencesOnce()
            if (prefs?.dailyReminderEnabled != true) {
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
    

}
