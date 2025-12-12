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
 * Worker for mock test reminder notifications
 */
class MockTestReminderWorker(
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
            if (prefs?.mockTestRemindersEnabled != true) {
                return Result.success()
            }
            
            // Get user's selected branch
            val userPref = repository.getUserPreference().first()
            val branchId = userPref?.selectedBranchId ?: return Result.success()
            
            // Get all mock tests
            val mockTests = repository.getMockTestsForBranch(branchId).first()
            
            if (mockTests.isEmpty()) {
                // No tests taken yet - encourage first test
                notificationHelper.showMockTestReminder(0)
                return Result.success()
            }
            
            // Find days since last test
            val latestTest = mockTests.maxByOrNull { it.testDate }
            val daysSinceLastTest = if (latestTest != null) {
                val daysDiff = (System.currentTimeMillis() - latestTest.testDate) / (1000 * 60 * 60 * 24)
                daysDiff.toInt()
            } else {
                0
            }
            
            // Notify if it's been more than threshold days
            if (daysSinceLastTest >= prefs.mockTestReminderFrequency) {
                notificationHelper.showMockTestReminder(daysSinceLastTest)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
