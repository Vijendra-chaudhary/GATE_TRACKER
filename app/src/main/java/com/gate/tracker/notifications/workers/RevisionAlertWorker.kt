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
 * Worker for revision alert notifications
 */
class RevisionAlertWorker(
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
            if (prefs?.revisionAlertsEnabled != true) {
                return Result.success()
            }
            
            // Get user's selected branch
            val userPref = repository.getUserPreference().first()
            val branchId = userPref?.selectedBranchId ?: return Result.success()
            
            // Find chapters needing revision
            val subjects = repository.getSubjectsByBranch(branchId).first()
            var revisionsNeeded = 0
            
            val currentTime = System.currentTimeMillis()
            val thresholdTime = currentTime - (prefs.revisionThresholdDays * 24 * 60 * 60 * 1000L)
            
            subjects.forEach { subject ->
                val chapters = repository.getChaptersBySubject(subject.id).first()
                
                // Count completed chapters that haven't been updated recently
                chapters.forEach { chapter ->
                    val chapterTime = chapter.completedDate ?: 0L
                    if (chapter.isCompleted && chapterTime < thresholdTime && chapterTime > 0) {
                        revisionsNeeded++
                    }
                }
            }
            
            if (revisionsNeeded > 0) {
                notificationHelper.showRevisionAlert(revisionsNeeded)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
