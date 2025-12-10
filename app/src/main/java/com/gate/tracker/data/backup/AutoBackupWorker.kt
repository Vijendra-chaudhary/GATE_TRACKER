package com.gate.tracker.data.backup

import android.content.Context
import androidx.work.*
import com.gate.tracker.data.repository.GateRepository
import com.gate.tracker.ui.settings.BackupRestoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * WorkManager worker for automatic backups
 * Triggers on progress changes with smart debouncing
 */
class AutoBackupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    companion object {
        const val WORK_NAME = "auto_backup_work"
        const val KEY_BRANCH_ID = "branch_id"
        const val KEY_BRANCH_NAME = "branch_name"
        
        /**
         * Schedule an auto-backup with debouncing
         * If called multiple times within 3 seconds, only the last one executes
         */
        fun scheduleBackup(context: Context, branchId: Int, branchName: String) {
            val workRequest = OneTimeWorkRequestBuilder<AutoBackupWorker>()
                .setInputData(
                    workDataOf(
                        KEY_BRANCH_ID to branchId,
                        KEY_BRANCH_NAME to branchName
                    )
                )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED) // Any network
                        .build()
                )
                .setInitialDelay(3, TimeUnit.SECONDS) // Wait 3s for DB to finish
                .build()
            
            // Replace existing pending backup with this one (debouncing effect)
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE, // Replace = debounce
                    workRequest
                )
        }
        
        /**
         * Cancel pending auto-backup
         */
        fun cancelBackup(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val branchId = inputData.getInt(KEY_BRANCH_ID, -1)
            val branchName = inputData.getString(KEY_BRANCH_NAME) ?: "Unknown"
            
            if (branchId == -1) {
                return@withContext Result.failure()
            }
            
            // Get repository from application
            val app = applicationContext as com.gate.tracker.GateApp
            val repository = app.repository
            
            // Check if user is signed in to Google Drive
            val driveManager = com.gate.tracker.data.drive.DriveManager(applicationContext)
            if (!driveManager.isSignedIn()) {
                // Not signed in, can't backup
                return@withContext Result.failure()
            }
            
            // Export backup data
            val backupData = repository.exportBackupData(branchId)
            
            // Serialize to JSON
            val serializer = com.gate.tracker.data.backup.BackupSerializer()
            val jsonContent = serializer.serialize(backupData)
            
            // Create filename with timestamp
            val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())
                .format(java.util.Date())
            val fileName = "gate_tracker_${branchName}_$timestamp.json"
            
            // Upload to Drive
            val uploadResult = driveManager.uploadBackup(fileName, jsonContent)
            
            if (uploadResult.isSuccess) {
                // Show success notification
                showNotification(
                    context = applicationContext,
                    title = "Auto-Backup Complete",
                    message = "Progress saved to Google Drive"
                )
                Result.success()
            } else {
                // Show error notification
                showNotification(
                    context = applicationContext,
                    title = "Auto-Backup Failed",
                    message = "Will retry when online"
                )
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
    
    private fun showNotification(context: Context, title: String, message: String) {
        val notificationHelper = com.gate.tracker.notifications.NotificationHelper(context)
        notificationHelper.showBackupNotification(title, message)
    }
}
