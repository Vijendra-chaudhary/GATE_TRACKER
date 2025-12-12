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
            android.util.Log.d("AutoBackupWorker", "scheduleBackup called for branch $branchId ($branchName)")
            
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
            
            android.util.Log.d("AutoBackupWorker", "Enqueueing work with 3-second delay")
            
            // Replace existing pending backup with this one (debouncing effect)
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE, // Replace = debounce
                    workRequest
                )
            
            android.util.Log.d("AutoBackupWorker", "Work enqueued successfully")
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
            android.util.Log.d("AutoBackupWorker", "doWork started")
            
            val branchId = inputData.getInt(KEY_BRANCH_ID, -1)
            val branchName = inputData.getString(KEY_BRANCH_NAME) ?: "Unknown"
            
            android.util.Log.d("AutoBackupWorker", "Branch: $branchName (ID: $branchId)")
            
            if (branchId == -1) {
                android.util.Log.e("AutoBackupWorker", "Invalid branch ID, failing")
                return@withContext Result.failure()
            }
            
            // Get repository from application
            val app = applicationContext as com.gate.tracker.GateApp
            val repository = app.repository
            
            // Check if user is signed in to Google Drive
            val driveManager = com.gate.tracker.data.drive.DriveManager(applicationContext)
            val account = driveManager.getSignedInAccount()
            android.util.Log.d("AutoBackupWorker", "Sign-in check: account=${account?.email}")
            
            if (account == null) {
                // Not signed in, can't backup
                android.util.Log.w("AutoBackupWorker", "User not signed in to Drive, cannot backup")
                return@withContext Result.failure()
            }
            
            // Initialize Drive service for this background worker
            android.util.Log.d("AutoBackupWorker", "Initializing Drive service...")
            val initResult = driveManager.initializeDriveService(account)
            if (initResult.isFailure) {
                android.util.Log.e("AutoBackupWorker", "Failed to initialize Drive service: ${initResult.exceptionOrNull()?.message}")
                return@withContext Result.failure()
            }
            android.util.Log.d("AutoBackupWorker", "Drive service initialized successfully")
            
            android.util.Log.d("AutoBackupWorker", "Exporting backup data...")
            // Export backup data
            val backupData = repository.exportBackupData(branchId)
            
            android.util.Log.d("AutoBackupWorker", "Serializing to JSON...")
            // Serialize to JSON
            val serializer = com.gate.tracker.data.backup.BackupSerializer()
            val jsonContent = serializer.serialize(backupData)
            
            // Create filename with timestamp
            val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())
                .format(java.util.Date())
            val fileName = "gate_tracker_${branchName}_$timestamp.json"
            
            android.util.Log.d("AutoBackupWorker", "Uploading to Drive: $fileName")
            // Upload to Drive
            val uploadResult = driveManager.uploadBackup(fileName, jsonContent)
            
            if (uploadResult.isSuccess) {
                android.util.Log.d("AutoBackupWorker", "Backup successful!")
                // Notification removed as per request (silent backup)
                Result.success()
            } else {
                android.util.Log.e("AutoBackupWorker", "Backup failed: ${uploadResult.exceptionOrNull()?.message}")
                // Notification removed as per request (silent backup)
                Result.retry()
            }
        } catch (e: Exception) {
            android.util.Log.e("AutoBackupWorker", "Exception in doWork", e)
            e.printStackTrace()
            Result.retry()
        }
    }
    

}
